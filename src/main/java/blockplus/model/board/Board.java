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

package blockplus.model.board;

import java.util.Map;
import java.util.Set;

import blockplus.model.board.Layer.State;
import blockplus.model.context.Color;
import blockplus.model.entity.entity.IEntity;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import components.cells.Positions;
import components.cells.Positions.Position;

public final class Board {

    public final static class Builder {

        private static Set<Color> check(final Set<Color> colors) {
            Preconditions.checkArgument(colors != null);
            Preconditions.checkArgument(!colors.isEmpty());
            return colors;
        }

        private final Set<Color> colors;

        private Set<Color> colors() {
            return this.colors;
        }

        private final Positions positions;

        private Positions positions() {
            return this.positions;
        }

        public int rows() {
            return this.positions().rows();
        }

        public int columns() {
            return this.positions().columns();
        }

        private final ImmutableSortedMap.Builder<Color, Layer> layerByColor = new ImmutableSortedMap.Builder<Color, Layer>(Ordering.natural());

        public Builder(final Set<Color> colors, final Positions positions) {
            this.colors = ImmutableSortedSet.copyOf(check(colors));
            this.positions = positions;
        }

        public Builder addLayer(final Color color, final Layer layer) {
            Preconditions.checkArgument(this.colors().contains(color));
            Preconditions.checkArgument(this.rows() == layer.rows());
            Preconditions.checkArgument(this.columns() == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Builder addLayer(final Color color, final Map<Position, State> layerMutation) {
            return this.addLayer(color, new Layer(this.positions()).apply(layerMutation));
        }

        public Builder addLayer(final Color color) {
            return this.addLayer(color, new Layer(this.positions()));
        }

        public Board build() {
            ImmutableSortedMap<Color, Layer> layerByColor = this.layerByColor.build();
            if (layerByColor.isEmpty()) {
                /*
                 * ImmutableSortedMap.Builder instances can be reused - it is safe to call build()
                 * multiple times to build multiple maps in series. Each map is
                 * a superset of the maps created before it.
                 */
                for (final Color color : this.colors())
                    this.layerByColor.put(color, new Layer(this.positions()));
                layerByColor = this.layerByColor.build();
            }
            else Preconditions.checkState(this.colors().size() == layerByColor.size());
            return new Board(this.positions(), layerByColor);
        }

    }

    public static Builder builder(final Set<Color> colors, final Positions cellPositions) {
        return new Board.Builder(colors, cellPositions);
    }

    private final Map<Color, Layer> layers;
    private final Positions positions;

    private Board(final Positions positions, final Map<Color, Layer> layerByColor) {
        this.positions = positions;
        this.layers = layerByColor;
    }

    public Set<Color> getColors() {
        return this.layers.keySet();
    }

    public int rows() {
        return this.positions.rows();
    }

    public int columns() {
        return this.positions.columns();
    }

    public Layer get(final Color color) {
        return this.layers.get(color);
    }

    public Iterable<Position> neighbours(final Position position, final int radius) {
        return this.positions.neighbours(position, radius);
    }

    public Board apply(final Color color, final Iterable<Position> positions, final Iterable<Position> shadows, final Iterable<Position> lights) {
        final Map<Position, State> selvesMutation = new LayerMutationBuilder()
                .setSelfPositions(positions)
                .setShadowPositions(shadows)
                .setLightPositions(lights)
                .build();
        final Map<Position, State> othersMutation = new LayerMutationBuilder()
                .setOtherPositions(positions)
                .build();
        final Map<Color, Layer> layers = Maps.newTreeMap();
        for (final Color c : this.getColors())
            layers.put(c, this.get(c).apply(c.equals(color) ? selvesMutation : othersMutation));
        return new Board(this.positions, layers);
    }

    public Board apply(final Color color, final IEntity entity) {
        return entity.isNull() ? this : this.apply(color, entity.positions(), entity.shadows(), entity.lights());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.layers);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof Board);
        final Board that = (Board) object;
        return this.layers.equals(that.layers);
    }

    @Override
    public String toString() {
        // TODO à revoir
        final ToStringHelper toStringHelper = Objects.toStringHelper(this).add("rows", this.rows()).add("columns", this.columns());
        final JsonObject data = new JsonObject();
        for (final Color color : this.getColors()) {
            final JsonArray jsonArray = new JsonArray();
            final Layer layer = this.get(color);
            final Set<Position> positions = layer.getSelves().keySet();
            for (final Position position : positions)
                jsonArray.add(new JsonPrimitive(position.id()));
            data.add(color.toString(), jsonArray);
        }
        toStringHelper.add("data", data);
        return toStringHelper.toString();
    }

}