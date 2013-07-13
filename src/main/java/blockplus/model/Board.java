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

package blockplus.model;

import static blockplus.model.Board.State.Metta;
import static blockplus.model.Board.State.Mudita;
import static blockplus.model.Board.State.Nirvana;
import static blockplus.model.Board.State.Upekkha;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import components.cells.Cells;
import components.cells.ICells;
import components.cells.IPosition;
import components.cells.Positions;

public final class Board {

    /**
     * Possible states for a cell in a layer of a board. Since each color is
     * mapped on its own layer, the board is made of 4 layers : one for Blue,
     * one for Yellow, one for Red and one for Green.
     */
    enum State {

        /**
         * State for a cell that could contain this layer's color
         */
        Metta,

        /**
         * State for a cell that can not contain this layer's color
         */
        Karuna,

        /**
         * State for a cell that contains a different layer's color
         */
        Mudita,

        /**
         * State for a cell that contains this layer's color
         */
        Upekkha,

        /**
         * State for a stateless cell
         */
        Nirvana;

    }

    public final static class Layer implements Supplier<ICells<State>> {

        private final static class IsMutablePredicate implements Predicate<IPosition> {

            private final ICells<State> stateBoard;

            public IsMutablePredicate(final ICells<State> stateBoard) {
                this.stateBoard = stateBoard;
            }

            /*
            private boolean isDefined(final int row, final int column) {
                return row > -1 && column > -1 && row < this.stateBoard.rows() && column < this.stateBoard.columns();
            }
            */

            @Override
            public boolean apply(@Nullable final IPosition position) {
                final State state = this.stateBoard.get(position);
                return //this.isDefined(position.row(), position.column()) && 
                state.equals(State.Nirvana) || state.equals(Metta);
            }

        };

        private final static Predicate<Entry<IPosition, State>> SELF_PREDICATE = new Predicate<Map.Entry<IPosition, State>>() {

            @Override
            public boolean apply(final Entry<IPosition, State> entry) {
                return entry.getValue().equals(Upekkha);
            }

        };

        private final static Predicate<Entry<IPosition, State>> LIGHT_PREDICATE = new Predicate<Map.Entry<IPosition, State>>() {

            @Override
            public boolean apply(final Entry<IPosition, State> entry) {
                return entry.getValue().equals(Metta);
            }

        };

        private final ICells<State> stateBoard;
        private final IsMutablePredicate isMutablePredicate;

        private volatile Map<IPosition, State> selves;
        private volatile Map<IPosition, State> lights;

        private Layer(final ICells<State> stateBoard) {
            this.stateBoard = stateBoard;
            this.isMutablePredicate = new IsMutablePredicate(stateBoard);
        }

        public Layer(final Positions cellPositions) {
            this(Cells.from(cellPositions, Nirvana, Mudita));
        }

        public IPosition position(final int i, final int j) {
            return this.get().position(i, i);
        }

        public boolean isMutable(final IPosition position) {
            return this.isMutablePredicate.apply(position);
        }

        public boolean isMutable(final int i, final int j) {
            return this.isMutable(this.get().position(i, j));
        }

        public boolean isLegal(final Iterable<IPosition> positions) {
            boolean containsLight = false;
            for (final IPosition position : positions) {
                if (!this.isMutable(position)) return false;
                if (this.isLight(position)) containsLight = true;
            }
            return containsLight;
        }

        @Override
        public ICells<State> get() {
            return this.stateBoard;
        }

        public int rows() {
            return this.get().rows();
        }

        public int columns() {
            return this.get().columns();
        }

        public boolean isLight(final IPosition position) {
            return this.getLights().containsKey(position);
        }

        public Board apply(final Set<IPosition> positions, final Set<IPosition> shadows, final Set<IPosition> lights) {
            return null;
        }

        public Layer apply(final Map<IPosition, State> layerMutation) {
            final Map<IPosition, State> consistentMutations = Maps.newHashMap();
            for (final Entry<IPosition, State> mutation : layerMutation.entrySet())
                if (this.isMutable(mutation.getKey())) consistentMutations.put(mutation.getKey(), mutation.getValue());
            return new Layer(this.get().apply(consistentMutations));
        }

        public Layer apply(final IPosition position, final State state) {
            if (!this.isMutable(position)) return this;
            final Map<IPosition, State> consistentMutation = Maps.newHashMap();
            consistentMutation.put(position, state);
            return new Layer(this.get().apply(consistentMutation));
        }

        public Map<IPosition, State> getLights() {
            Map<IPosition, State> value = this.lights;
            if (value == null) synchronized (this) {
                if ((value = this.lights) == null) this.lights = value = this.get().filter(LIGHT_PREDICATE);
            }
            return value;
        }

        public Map<IPosition, State> getSelves() {
            Map<IPosition, State> value = this.selves;
            if (value == null) synchronized (this) {
                if ((value = this.selves) == null) this.selves = value = this.get().filter(SELF_PREDICATE);
            }
            return value;
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null) return false;
            Preconditions.checkArgument(object instanceof Layer);
            return this.get().equals(((Layer) object).get());
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("rows", this.rows())
                    .add("columns", this.columns())
                    .add("initial", this.get().initialSymbol())
                    .add("undefined", this.get().undefinedSymbol())
                    .add("mutations", this.get().get())
                    .toString();
        }

    }

    public final static class LayerMutationBuilder {

        private Iterable<IPosition> potentialPositions = Sets.newHashSet();
        private Iterable<IPosition> selfPositions = Sets.newHashSet();
        private Iterable<IPosition> otherPositions = Sets.newHashSet();
        private Iterable<IPosition> shadowPositions = Sets.newHashSet();

        public LayerMutationBuilder setLightPositions(final Iterable<IPosition> positions) {
            this.potentialPositions = positions;
            return this;
        }

        public LayerMutationBuilder setLightPositions(final IPosition... positions) {
            return this.setLightPositions(Sets.newHashSet(positions));
        }

        public LayerMutationBuilder setSelfPositions(final Iterable<IPosition> positions) {
            this.selfPositions = positions;
            return this;
        }

        public LayerMutationBuilder setShadowPositions(final Iterable<IPosition> positions) {
            this.shadowPositions = positions;
            return this;
        }

        public LayerMutationBuilder setOtherPositions(final Iterable<IPosition> positions) {
            this.otherPositions = positions;
            return this;
        }

        public Map<IPosition, State> build() {
            final ImmutableMap.Builder<IPosition, State> builder = new ImmutableMap.Builder<IPosition, State>();
            for (final IPosition position : this.selfPositions)
                builder.put(position, State.Upekkha);
            for (final IPosition position : this.shadowPositions)
                builder.put(position, State.Karuna);
            for (final IPosition position : this.potentialPositions)
                builder.put(position, State.Metta);
            for (final IPosition position : this.otherPositions)
                builder.put(position, State.Mudita);
            return builder.build();
        }

    }

    public final static class Builder {

        private static Set<Colors> check(final Set<Colors> colors) {
            Preconditions.checkArgument(colors != null);
            Preconditions.checkArgument(!colors.isEmpty());
            return colors;
        }

        private final Set<Colors> colors;

        private Set<Colors> colors() {
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

        private final ImmutableSortedMap.Builder<Colors, Layer> layerByColor = new ImmutableSortedMap.Builder<Colors, Layer>(Ordering.natural());

        public Builder(final Set<Colors> colors, final Positions positions) {
            this.colors = ImmutableSortedSet.copyOf(check(colors));
            this.positions = positions;
        }

        public Builder addLayer(final Colors color, final Layer layer) {
            Preconditions.checkArgument(this.colors().contains(color));
            Preconditions.checkArgument(this.rows() == layer.rows());
            Preconditions.checkArgument(this.columns() == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Builder addLayer(final Colors color, final Map<IPosition, State> layerMutation) {
            return this.addLayer(color, new Layer(this.positions()).apply(layerMutation));
        }

        public Builder addLayer(final Colors color) {
            return this.addLayer(color, new Layer(this.positions()));
        }

        public Board build() {
            ImmutableSortedMap<Colors, Layer> layerByColor = this.layerByColor.build();
            if (layerByColor.isEmpty()) {
                for (final Colors color : this.colors())
                    this.layerByColor.put(color, new Layer(this.positions()));
                layerByColor = this.layerByColor.build();
            }
            else Preconditions.checkState(this.colors().size() == layerByColor.size());
            return new Board(this.positions(), layerByColor);
        }

    }

    public static Builder builder(final Set<Colors> colors, final Positions cellPositions) {
        return new Board.Builder(colors, cellPositions);
    }

    private final Map<Colors, Layer> layers;
    private final Positions positions;

    private Board(final Positions positions, final Map<Colors, Layer> layerByColor) {
        this.positions = positions;
        this.layers = layerByColor;
    }

    public Set<Colors> getColors() {
        return this.layers.keySet();
    }

    public int rows() {
        return this.positions.rows();
    }

    public int columns() {
        return this.positions.columns();
    }

    public Layer get(final Colors color) {
        return this.layers.get(color);
    }

    public Iterable<IPosition> neighbours(final IPosition position, final int radius) {
        return this.positions.neighbours(position, radius);
    }

    public Board apply(final Colors color, final Iterable<IPosition> positions, final Iterable<IPosition> shadows, final Iterable<IPosition> lights) {
        final Map<IPosition, State> selvesMutation = new LayerMutationBuilder()
                .setSelfPositions(positions)
                .setShadowPositions(shadows)
                .setLightPositions(lights)
                .build();
        final Map<IPosition, State> othersMutation = new LayerMutationBuilder()
                .setOtherPositions(positions)
                .build();
        final Map<Colors, Layer> layers = Maps.newTreeMap();
        for (final Colors c : this.getColors())
            layers.put(c, this.get(c).apply(c.equals(color) ? selvesMutation : othersMutation));
        return new Board(this.positions, layers);
    }

    public Board apply(final Colors color, final PolyominoTranslatedInstance instance) {
        return instance == null ? this : this.apply(color, instance.positions(), instance.shadows(), instance.lights());
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
        return null; //TODO
        /*
        final ToStringHelper toStringHelper = Objects.toStringHelper(this).add("rows", this.rows()).add("columns", this.columns());
        final JsonObject data = new JsonObject();
        for (final Colors color : this.getColors()) {
            final JsonArray jsonArray = new JsonArray();
            final Layer layer = this.get(color);
            final Set<IPosition> positions = layer.getSelves().keySet();
            for (final IPosition position : positions)
                jsonArray.add(new JsonPrimitive(position.id()));
            data.add(color.toString(), jsonArray);
        }
        toStringHelper.add("data", data);
        return toStringHelper.toString();
        */
    }

}