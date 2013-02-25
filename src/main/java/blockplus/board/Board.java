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
import java.util.Set;

import blockplus.Color;
import blockplus.board.Layer.State;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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

        private final ImmutableSortedMap.Builder<Color, Layer> layerByColor = new ImmutableSortedMap.Builder<Color, Layer>(Ordering.natural());

        public Builder(final Set<Color> colors, final int rows, final int columns) {
            this.colors = ImmutableSortedSet.copyOf(check(colors));
            this.rows = check(rows);
            this.columns = check(columns);
        }

        public Builder addLayer(final Color color, final Layer layer) {
            Preconditions.checkArgument(this.getColors().contains(color));
            Preconditions.checkArgument(this.rows == layer.rows());
            Preconditions.checkArgument(this.columns == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Builder addLayer(final Color color, final Map<PositionInterface, State> layerMutation) {
            return this.addLayer(color, new Layer(this.rows, this.columns).apply(layerMutation));
        }

        public Builder addLayer(final Color color) {
            return this.addLayer(color, new Layer(this.rows, this.columns));
        }

        public Board build() {
            ImmutableSortedMap<Color, Layer> layerByColor = this.layerByColor.build();
            if (layerByColor.isEmpty()) {
                /*
                 * ImmutableSortedMap.Builder instances can be reused - it is safe to call build()
                 * multiple times to build multiple maps in series. Each map is
                 * a superset of the maps created before it.
                 */
                for (final Color color : this.getColors())
                    this.layerByColor.put(color, new Layer(this.rows, this.columns));
                layerByColor = this.layerByColor.build();
            }
            else Preconditions.checkState(this.getColors().size() == layerByColor.size());
            return new Board(this.getRows(), this.getColumns(), layerByColor);
        }

    }

    public static Builder builder(final Set<Color> colors, final int rows, final int columns) {
        return new Board.Builder(colors, rows, columns);
    }

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

    private Board(final int rows, final int columns, final ImmutableSortedMap<Color, Layer> layerByColor) {
        this.rows = rows;
        this.columns = columns;
        this.layerByColor = layerByColor;
    }

    public Layer getLayer(final Color color) {
        //Preconditions.checkArgument(color != null);
        //Preconditions.checkState(this.getColors().contains(color));
        return this.layerByColor.get(color);
    }

    @Override
    public boolean isLegal(final MoveInterface moveInterface) {
        final Move move = (Move) moveInterface;
        return move.isNull() ? true : this.getLayer(move.getColor()).isLegal(move.getPiece().getSelfPositions());
    }

    @Override
    public Board apply(final MoveInterface moveInterface) {
        if (moveInterface.isNull()) return this;
        final Move move = (Move) moveInterface;
        final PieceInterface piece = move.getPiece();
        final Color color = move.getColor();
        final Map<PositionInterface, State> selfMutation = new LayerMutationBuilder()
                .setSelfPositions(piece.getSelfPositions())
                .setShadowPositions(piece.getShadowPositions())
                .setLightPositions(piece.getLightPositions()).build();
        final Map<PositionInterface, State> otherMutation = new LayerMutationBuilder()
                .setOtherPositions(piece.getSelfPositions())
                .build();
        final ImmutableSortedMap.Builder<Color, Layer> newLayers = new ImmutableSortedMap.Builder<Color, Layer>(Ordering.natural());
        for (final Color anotherColor : this.getColors()) {
            if (anotherColor.equals(color)) newLayers.put(color, this.getLayer(color).apply(selfMutation));
            else newLayers.put(anotherColor, this.getLayer(anotherColor).apply(otherMutation));
        }
        return new Board(this.rows(), this.columns(), newLayers.build());
    }

    // TODO use toString
    // TODO memoize
    @Override
    public int hashCode() {
        return Objects.hashCode(this.layerByColor);
    }

    // TODO use toString
    @Override
    public boolean equals(final Object object) {
    	if(object==null) return false;
    	Preconditions.checkArgument(object instanceof Board);
        final Board that = (Board) object;
        return this.layerByColor.equals(that.layerByColor);
    }

    // TODO memoize
    @Override
    public String toString() {
        final ToStringHelper toStringHelper = Objects.toStringHelper(this)
                .add("rows", this.rows())
                .add("columns", this.columns());
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Color color : this.getColors()) {
            stringBuilder.append(this.layerByColor.get(color).get().mutations());
            stringBuilder.append(this.layerByColor.get(color).get().mutations());
        }
        final JsonObject data = new JsonObject();
        for (final Color color : this.getColors()) {
            final JsonArray jsonArray = new JsonArray();
            final Layer layer = this.getLayer(color);
            final Set<PositionInterface> positions = layer.getSelves().keySet();
            for (final PositionInterface position : positions)
                jsonArray.add(new JsonPrimitive(position.toString()));
            data.add(color.toString(), jsonArray);
        }
        toStringHelper.add("data", data);
        return toStringHelper.toString();
    }
}