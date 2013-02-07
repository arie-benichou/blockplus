/*
 * Copyright 2012-2013 Arie Benichou
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

package components.board;

import static components.position.Position.Position;

import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;

public final class BoardStringRendering {

    private final Map<?, Character> symbolByObject;

    public Map<?, Character> getSymbols() {
        return this.symbolByObject;
    }

    public Character getSymbol(final Object object) {
        final Character symbol = this.symbolByObject.get(object);
        return symbol == null ? '?' : symbol;
    }

    public BoardStringRendering(final Map<?, Character> symbols) {
        this.symbolByObject = symbols;
    }

    public String apply(final BoardInterface<?> board) {
        final String lineSeparator = "\n" + " " + Strings.repeat("----", board.columns()) + "-" + "\n";
        final String columnSeparator = " | ";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.rows(); ++i) {
            sb.append(lineSeparator);
            for (int j = 0; j < board.columns(); ++j) {
                sb.append(columnSeparator);
                sb.append(this.getSymbol(board.get(Position(i, j))));
            }
            sb.append(columnSeparator);
        }
        sb.append(lineSeparator);
        return sb.toString();
    }

    public String apply(final Supplier<BoardInterface<?>> boardSupplier) {
        return this.apply(boardSupplier.get());
    }

}