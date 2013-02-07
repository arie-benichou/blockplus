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

import java.util.Map;
import java.util.Set;

import blockplus.color.ColorInterface;
import blockplus.piece.PieceInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import components.position.PositionInterface;

public final class Board {

    public final static class Builder {

        private static Set<ColorInterface> check(final Set<ColorInterface> colors) {
            Preconditions.checkArgument(colors != null);
            Preconditions.checkArgument(!colors.isEmpty());
            return colors;
        }

        private static int check(final int naturalInteger) {
            Preconditions.checkArgument(naturalInteger > 0);
            return naturalInteger;
        }

        private final Set<ColorInterface> colors;

        private Set<ColorInterface> getColors() {
            return this.colors;
        }

        private final int rows;

        public int getRows() {
            return this.rows;
        }

        public int getColumns() {
            return this.columns;
        }

        private final int columns;

        private final Map<ColorInterface, BoardLayer> layerByColor = Maps.newHashMap();

        public Builder(final Set<ColorInterface> colors, final int rows, final int columns) {
            this.colors = check(colors);
            this.rows = check(rows);
            this.columns = check(columns);
        }

        public Builder set(final ColorInterface color, final BoardLayer layer) {
            Preconditions.checkArgument(this.getColors().contains(color));
            Preconditions.checkArgument(this.rows == layer.rows());
            Preconditions.checkArgument(this.columns == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Board build() {
            if (this.layerByColor.isEmpty())
                for (final ColorInterface color : this.getColors()) {
                    this.layerByColor.put(color, new BoardLayer(this.rows, this.columns));
                }
            Preconditions.checkState(this.getColors().size() == this.layerByColor.size());
            return new Board(this);
        }
    }

    public static Builder builder(final Set<ColorInterface> colors, final int rows, final int columns) {
        return new Board.Builder(colors, rows, columns);
    }

    // TODO sorted map
    private final Map<ColorInterface, BoardLayer> layerByColor;

    public Set<ColorInterface> getColors() {
        return this.layerByColor.keySet();
    }

    private final int rows;

    public int rows() {
        return this.rows;
    }

    private final int columns;

    public int columns() {
        return this.columns;
    }

    private Board(final Map<ColorInterface, BoardLayer> stateBoardByColor, final int rows, final int columns) {
        this.layerByColor = ImmutableMap.copyOf(stateBoardByColor);
        this.rows = rows;
        this.columns = columns;
    }

    private Board(final Builder builder) {
        this(builder.layerByColor, builder.rows, builder.columns);
    }

    public BoardLayer getLayer(final ColorInterface color) {
        return this.layerByColor.get(color);
    }

    public boolean isLegal(final ColorInterface color, final PieceInterface piece) {
        return this.getLayer(color).isLegal(piece.getSelfPositions());
    }

    public Board apply(final ColorInterface color, final PieceInterface piece) {
        final Map<ColorInterface, BoardLayer> newLayers = Maps.newHashMap();
        final Map<PositionInterface, State> mutation = new BoardMutationBuilder()
                .setOtherPositions(piece.getSelfPositions())
                .build();
        for (final ColorInterface anotherColor : this.getColors()) {
            if (!anotherColor.is(color)) newLayers.put(anotherColor, this.getLayer(anotherColor).apply(mutation));
            else newLayers.put(color, this.getLayer(color).apply(piece));
        }
        return new Board(newLayers, this.rows(), this.columns());
    }
}