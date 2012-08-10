/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.board;

import static blockplus.position.Position.Position;

import java.util.Map;

import blockplus.color.ColorInterface;
import blockplus.color.PrimeColors;
import blockplus.piece.matrix.Matrix;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public final class BoardRenderer {

    private final static Map<ColorInterface, Character> COLOR_BY_STRING = Maps.newHashMap();

    static {
        for (final ColorInterface color : PrimeColors.values())
            COLOR_BY_STRING.put(color, color.toString().charAt(0));
        COLOR_BY_STRING.put(ColorInterface.blue, '.');
        COLOR_BY_STRING.put(ColorInterface.red, '.');
        COLOR_BY_STRING.put(ColorInterface.yellow, '.');
        COLOR_BY_STRING.put(ColorInterface.green, '.');
        COLOR_BY_STRING.put(ColorInterface.white, '*');
    }

    public static String getRendering(final Board<ColorInterface> board) {
        final String lineSeparator = "\n" + " " + Strings.repeat("----", board.columns()) + "-" + "\n";
        final String columnSeparator = " | ";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.rows(); ++i) {
            sb.append(lineSeparator);
            for (int j = 0; j < board.columns(); ++j) {
                sb.append(columnSeparator);
                final ColorInterface color = board.get(Position(i, j)); // TODO ? isMixed()
                Character c = COLOR_BY_STRING.get(color);
                if (c == null) c = '#';
                sb.append(c);
            }
            sb.append(columnSeparator);
        }
        sb.append(lineSeparator);
        return sb.toString();
    }

    public static void debug(final Board<ColorInterface> board) {
        final int[][] data = new int[board.rows()][board.columns()];
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.columns(); ++j) {
                final ColorInterface color = board.get(Position(i, j));
                data[i][j] = color.value();
            }
        }
        final Matrix matrix = new Matrix(board.rows(), board.columns(), data);
        matrix.debug();
    }

    public static void render(final Board<ColorInterface> board) {
        System.out.println(getRendering(board));
        //System.out.println(IO.render(board)); // TODO pouvoir passer un mapping des symboles
    }

    private BoardRenderer() {}

}