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

import blockplus.color.Color;
import blockplus.color.ColorInterface;
import blockplus.color.PrimeColors;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

public final class BoardBuilder {

    private final static Map<Character, ColorInterface> COLOR_BY_STRING = Maps.newTreeMap();
    private final static Map<Integer, ColorInterface> COLOR_BY_VALUE = Maps.newTreeMap();

    static {
        for (final ColorInterface color : PrimeColors.values()) {
            COLOR_BY_STRING.put(color.toString().charAt(0), color);
            COLOR_BY_VALUE.put(color.value(), color);
        }
        COLOR_BY_STRING.put('.', ColorInterface.TRANSPARENT);
    }

    // TODO !? utiliser Move
    private static Map<PositionInterface, ColorInterface> updatePreDefinedPositions(
            final Map<PositionInterface, ColorInterface> definedPositions,
            final int rows,
            final int columns) {

        {
            final PositionInterface p = Position(0, 0);
            final ColorInterface color = definedPositions.get(p);
            if (color == null || color.equals(ColorInterface.TRANSPARENT))
                definedPositions.put(p, ColorInterface.white);
        }

        {
            final PositionInterface p = Position(0, columns - 1);
            final ColorInterface color = definedPositions.get(p);
            if (color == null || color.equals(ColorInterface.TRANSPARENT))
                definedPositions.put(p, ColorInterface.white);
        }

        {
            final PositionInterface p = Position(rows - 1, 0);
            final ColorInterface color = definedPositions.get(p);
            if (color == null || color.equals(ColorInterface.TRANSPARENT))
                definedPositions.put(p, ColorInterface.white);
        }

        {
            final PositionInterface p = Position(rows - 1, columns - 1);
            final ColorInterface color = definedPositions.get(p);
            if (color == null || color.equals(ColorInterface.TRANSPARENT))
                definedPositions.put(p, ColorInterface.white);
        }

        definedPositions.put(Position(-1, -1), ColorInterface.WHITE);
        definedPositions.put(Position(-1, columns), ColorInterface.WHITE);
        definedPositions.put(Position(rows, -1), ColorInterface.WHITE);
        definedPositions.put(Position(rows, columns), ColorInterface.WHITE);

        return definedPositions;
    }

    public static Board<ColorInterface> parse(final String[][] data) {
        final int rows = data.length;
        final int columns = data[0][0].length();
        final Map<PositionInterface, ColorInterface> definedPositions = Maps.newHashMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                final char c = data[i][0].charAt(j);
                ColorInterface color = COLOR_BY_STRING.get(c);
                if (color == null) color = ColorInterface.UNKNOWN;
                final PositionInterface position = Position(i, j);
                definedPositions.put(position, color);
            }
        updatePreDefinedPositions(definedPositions, rows, columns);
        return Board.from(rows, columns, ColorInterface.TRANSPARENT, ColorInterface.OPAQUE, definedPositions);
    }

    public static Board<ColorInterface> parse(final int[][] data) {
        final int rows = data.length;
        final int columns = data[0].length;
        final Map<PositionInterface, ColorInterface> definedPositions = Maps.newHashMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                final int value = data[i][j];
                ColorInterface color = COLOR_BY_VALUE.get(value);
                //if (color == null) color = ColorInterface.UNKNOWN;
                if (color == null) color = new Color(-value); // TODO à revoir
                final PositionInterface position = Position(i, j);
                definedPositions.put(position, color);
            }
        updatePreDefinedPositions(definedPositions, rows, columns);
        return Board.from(rows, columns, ColorInterface.TRANSPARENT, ColorInterface.OPAQUE, definedPositions);
    }

    public static Board<ColorInterface> from(final int rows, final int columns) {
        final Map<PositionInterface, ColorInterface> definedPositions = Maps.newHashMap();
        updatePreDefinedPositions(definedPositions, rows, columns);
        return Board.from(rows, columns, ColorInterface.TRANSPARENT, ColorInterface.OPAQUE, definedPositions);
    }

    private BoardBuilder() {}

}