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

package blockplus.board;

import static blockplus.board.State.Light;
import static blockplus.board.State.Self;
import static blockplus.color.Colors.Blue;
import static blockplus.color.Colors.Green;
import static blockplus.color.Colors.Red;
import static blockplus.color.Colors.Yellow;
import static components.position.Position.Position;

import java.util.Map;
import java.util.Set;

import blockplus.board.Board.Builder;
import blockplus.color.ColorInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

public final class BoardParser {

    private final static Map<Character, ColorInterface> COLOR_BY_SYMBOL = Maps.newHashMap();
    static {
        COLOR_BY_SYMBOL.put('b', Blue);
        COLOR_BY_SYMBOL.put('B', Blue);
        COLOR_BY_SYMBOL.put('y', Yellow);
        COLOR_BY_SYMBOL.put('Y', Yellow);
        COLOR_BY_SYMBOL.put('r', Red);
        COLOR_BY_SYMBOL.put('R', Red);
        COLOR_BY_SYMBOL.put('g', Green);
        COLOR_BY_SYMBOL.put('G', Green);
    }

    private final static Map<Character, State> STATE_BY_SYMBOL = Maps.newHashMap();
    static {
        STATE_BY_SYMBOL.put('b', Light);
        STATE_BY_SYMBOL.put('B', Self);
        STATE_BY_SYMBOL.put('y', Light);
        STATE_BY_SYMBOL.put('Y', Self);
        STATE_BY_SYMBOL.put('r', Light);
        STATE_BY_SYMBOL.put('R', Self);
        STATE_BY_SYMBOL.put('g', Light);
        STATE_BY_SYMBOL.put('G', Self);
    }

    private final static Character INITIAL_SYMBOL = '.';

    private final static class Cell {

        private final PositionInterface position;
        private final State state;

        public Cell(final PositionInterface position, final State state) {
            this.position = position;
            this.state = state;
        }

        public PositionInterface getPosition() {
            return this.position;
        }

        public State getState() {
            return this.state;
        }

    }

    private ColorInterface getColor(final Character symbol) {
        final ColorInterface color = COLOR_BY_SYMBOL.get(symbol);
        Preconditions.checkState(color != null, "Illegal symbol: " + symbol);
        return color;
    }

    private State getState(final Character symbol) {
        final State state = STATE_BY_SYMBOL.get(symbol);
        Preconditions.checkState(state != null, "Illegal symbol");
        return state;
    }

    private Map<ColorInterface, Set<Cell>> collectCells(final String[][] data, final int rows, final int columns) {
        final Map<ColorInterface, Set<Cell>> cellByColor = Maps.newHashMap();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final Character symbol = data[i][0].charAt(j);
                if (!symbol.equals(INITIAL_SYMBOL)) {
                    final ColorInterface color = this.getColor(symbol);
                    Set<Cell> cells = cellByColor.get(color);
                    if (cells == null) cellByColor.put(color, cells = Sets.newHashSet());
                    cells.add(new Cell(Position(i, j), this.getState(symbol)));
                }
            }
        }
        return cellByColor;
    }

    private Board buildBoard(final Map<ColorInterface, Set<Cell>> cellByColor, final Builder builder) {
        for (final ColorInterface color : builder.getColors()) {
            final Set<Cell> cells = cellByColor.get(color);
            final Map<PositionInterface, State> layerMutation = Maps.newHashMap();
            for (final Cell cell : cells)
                layerMutation.put(cell.getPosition(), cell.getState());
            final BoardLayer layer = new BoardLayer(builder.getRows(), builder.getColumns());
            builder.set(color, layer.apply(layerMutation));
        }
        return builder.build();
    }

    public Board parse(final String[][] data) {
        final int rows = data.length;
        final int columns = data[0][0].length();
        final Map<ColorInterface, Set<Cell>> cellByColor = this.collectCells(data, rows, columns);
        return this.buildBoard(cellByColor, Board.builder(cellByColor.keySet(), rows, columns));
    }

}