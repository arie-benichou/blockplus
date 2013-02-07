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

import blockplus.context.Color;
import blockplus.piece.PieceInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import components.position.PositionInterface;

public final class Board {

    public final static class Builder {

        private static Set<Color> check(final Set<Color> colors) {
            Preconditions.checkArgument(colors != null);
            Preconditions.checkArgument(!colors.isEmpty());
            return colors;
        }

        private static int check(final int naturalInteger) {
            Preconditions.checkArgument(naturalInteger > 0);
            return naturalInteger;
        }

        private final Set<Color> colors;

        private Set<Color> getColors() {
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

        private final Map<Color, BoardLayer> layerByColor = Maps.newHashMap();

        public Builder(final Set<Color> colors, final int rows, final int columns) {
            this.colors = check(colors);
            this.rows = check(rows);
            this.columns = check(columns);
        }

        public Builder set(final Color color, final BoardLayer layer) {
            Preconditions.checkArgument(this.getColors().contains(color));
            Preconditions.checkArgument(this.rows == layer.rows());
            Preconditions.checkArgument(this.columns == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Board build() {
            if (this.layerByColor.isEmpty())
                for (final Color color : this.getColors()) {
                    this.layerByColor.put(color, new BoardLayer(this.rows, this.columns));
                }
            Preconditions.checkState(this.getColors().size() == this.layerByColor.size());
            return new Board(this);
        }
    }

    public static Builder builder(final Set<Color> colors, final int rows, final int columns) {
        return new Board.Builder(colors, rows, columns);
    }

    // TODO sorted map
    private final Map<Color, BoardLayer> layerByColor;

    public Set<Color> getColors() {
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

    private Board(final Map<Color, BoardLayer> stateBoardByColor, final int rows, final int columns) {
        this.layerByColor = ImmutableMap.copyOf(stateBoardByColor);
        this.rows = rows;
        this.columns = columns;
    }

    private Board(final Builder builder) {
        this(builder.layerByColor, builder.rows, builder.columns);
    }

    public BoardLayer getLayer(final Color color) {
        return this.layerByColor.get(color);
    }

    public boolean isLegal(final Color color, final PieceInterface piece) {
        return this.getLayer(color).isLegal(piece.getSelfPositions());
    }

    public Board apply(final Color color, final PieceInterface piece) {
        final Map<Color, BoardLayer> newLayers = Maps.newHashMap();
        final Map<PositionInterface, State> mutation = new BoardMutationBuilder()
                .setOtherPositions(piece.getSelfPositions())
                .build();
        for (final Color anotherColor : this.getColors()) {
            if (!anotherColor.equals(color)) newLayers.put(anotherColor, this.getLayer(anotherColor).apply(mutation));
            else newLayers.put(color, this.getLayer(color).apply(piece));
        }
        return new Board(newLayers, this.rows(), this.columns());
    }
}