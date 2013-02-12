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

import interfaces.board.BoardInterface;
import interfaces.move.MoveInterface;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import blockplus.Color;
import blockplus.board.layer.Layer;
import blockplus.board.layer.State;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;

import com.google.common.base.Equivalences;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import components.position.PositionInterface;

public final class Board implements BoardInterface {

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

        private final Map<Color, Layer> layerByColor = Maps.newHashMap();

        public Builder(final Set<Color> colors, final int rows, final int columns) {
            this.colors = check(colors);
            this.rows = check(rows);
            this.columns = check(columns);
        }

        public Builder set(final Color color, final Layer layer) {
            Preconditions.checkArgument(this.getColors().contains(color));
            Preconditions.checkArgument(this.rows == layer.rows());
            Preconditions.checkArgument(this.columns == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Board build() {
            if (this.layerByColor.isEmpty())
                for (final Color color : this.getColors()) {
                    this.layerByColor.put(color, new Layer(this.rows, this.columns));
                }
            Preconditions.checkState(this.getColors().size() == this.layerByColor.size());
            return new Board(this);
        }
    }

    public static Builder builder(final Set<Color> colors, final int rows, final int columns) {
        return new Board.Builder(colors, rows, columns);
    }

    // TODO sorted map
    private final Map<Color, Layer> layerByColor;

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

    private Board(final Map<Color, Layer> stateBoardByColor, final int rows, final int columns) {
        this.layerByColor = ImmutableMap.copyOf(stateBoardByColor);
        this.rows = rows;
        this.columns = columns;
    }

    private Board(final Builder builder) {
        this(builder.layerByColor, builder.rows, builder.columns);
    }

    public Layer getLayer(final Color color) {
        //Preconditions.checkArgument(color != null);
        //Preconditions.checkState(this.getColors().contains(color));
        return this.layerByColor.get(color);
    }

    @Override
    public boolean isLegal(final MoveInterface moveInterface) {
        final Move move = (Move) moveInterface;
        return this.getLayer(move.getColor()).isLegal(move.getPiece().getSelfPositions());
    }

    @Override
    public Board apply(final MoveInterface moveInterface) {
        if (moveInterface.isNull()) return this;
        final Move move = (Move) moveInterface;
        final PieceInterface piece = move.getPiece();
        final Color color = move.getColor();
        final Map<Color, Layer> newLayers = Maps.newHashMap();
        final Map<PositionInterface, State> mutation = new BoardMutationBuilder()
                .setOtherPositions(piece.getSelfPositions())
                .build();
        for (final Color anotherColor : this.getColors()) {
            if (anotherColor.equals(color)) {
                newLayers.put(color, this.getLayer(color).apply(piece));
            }
            else {
                newLayers.put(anotherColor, this.getLayer(anotherColor).apply(mutation));
            }
        }
        return new Board(newLayers, this.rows(), this.columns());
    }

    // TODO memoize
    // TODO use toString
    @Override
    public int hashCode() {
        return Objects.hashCode(this.layerByColor);
    }

    // TODO use toString
    @Override
    public boolean equals(final Object object) {
        Preconditions.checkArgument(object instanceof Board);
        final Board that = (Board) object;
        return Equivalences.equals().equivalent(this.layerByColor, that.layerByColor);
    }

    // TODO
    // TODO memoize
    // TODO use toString
    @Override
    public String toString() {
        return "";
    }

}