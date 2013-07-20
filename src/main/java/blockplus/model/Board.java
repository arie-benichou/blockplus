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

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;
import static components.cells.Positions.Position;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import blockplus.model.Board.Layer.State;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import components.cells.Cells;
import components.cells.ICells;
import components.cells.IPosition;
import components.cells.Positions;

public final class Board {

    final static class Layer {

        /**
         * Possible states for a cell in a layer of a board. Since each color is
         * mapped on its own layer, the board is made of 4 layers : one for
         * Blue, one for Yellow, one for Red and one for Green.
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

        private final static Predicate<Entry<IPosition, State>> SELF_PREDICATE = new Predicate<Map.Entry<IPosition, State>>() {
            @Override
            public boolean apply(final Entry<IPosition, State> entry) {
                return entry.getValue().equals(State.Upekkha);
            }

        };

        private final static Predicate<Entry<IPosition, State>> LIGHT_PREDICATE = new Predicate<Map.Entry<IPosition, State>>() {
            @Override
            public boolean apply(final Entry<IPosition, State> entry) {
                return entry.getValue().equals(State.Metta);
            }

        };

        private final ICells<State> stateBoard;

        private volatile Map<IPosition, State> selves;
        private volatile Map<IPosition, State> lights;

        private Layer(final ICells<State> stateBoard) {
            this.stateBoard = stateBoard;
        }

        public Layer(final int rows, final int columns) {
            this(Cells.from(rows, columns, State.Nirvana, State.Mudita));
        }

        public boolean isMutable(final IPosition position) {
            final State state = this.stateBoard.get(position);
            return state.equals(State.Nirvana) || state.equals(State.Metta);
        }

        public boolean isLegal(final Iterable<IPosition> positions) {
            boolean containsLight = false;
            for (final IPosition position : positions) {
                if (!this.isMutable(position)) return false;
                if (this.isLight(position)) containsLight = true;
            }
            return containsLight;
        }

        private ICells<State> get() {
            return this.stateBoard;
        }

        private int rows() {
            return this.get().rows();
        }

        private int columns() {
            return this.get().columns();
        }

        public boolean isLight(final IPosition position) {
            return this.getLights().containsKey(position);
        }

        public Layer apply(final Map<IPosition, State> layerMutation) {
            final Map<IPosition, State> consistentMutations = Maps.newHashMap();
            for (final Entry<IPosition, State> mutation : layerMutation.entrySet())
                if (this.isMutable(mutation.getKey())) consistentMutations.put(mutation.getKey(), mutation.getValue());
            return new Layer(this.get().apply(consistentMutations));
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

        public State get(final IPosition position) {
            return this.get().get(position);
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

    public final static class Builder {

        private final int rows;

        private final int columns;

        private final Map<Colors, Layer> layerByColor = Maps.newHashMap();

        public Builder(final int rows, final int columns) {
            this.rows = rows;
            this.columns = columns;
            final Layer layer = new Layer(rows, columns);
            {
                final Map<IPosition, State> data = Maps.newHashMap();
                data.put(Position(0, 0), State.Metta);
                this.layerByColor.put(Blue, layer.apply(data));
            }
            {
                final Map<IPosition, State> data = Maps.newHashMap();
                data.put(Position(0, columns - 1), State.Metta);
                this.layerByColor.put(Yellow, layer.apply(data));
            }
            {
                final Map<IPosition, State> data = Maps.newHashMap();
                data.put(Position(rows - 1, columns - 1), State.Metta);
                this.layerByColor.put(Red, layer.apply(data));
            }
            {
                final Map<IPosition, State> data = Maps.newHashMap();
                data.put(Position(rows - 1, 0), State.Metta);
                this.layerByColor.put(Green, layer.apply(data));
            }
        }

        public Board build() {
            return new Board(this.rows, this.columns, this.layerByColor);
        }

    }

    private final Map<Colors, Layer> layers;

    private final int rows;
    private final int columns;

    private Board(final int rows, final int columns, final Map<Colors, Layer> layerByColor) {
        this.rows = rows;
        this.columns = columns;
        this.layers = layerByColor;
    }

    public Set<Colors> getColors() {
        return this.layers.keySet();
    }

    public int rows() {
        return this.rows;
    }

    public int columns() {
        return this.columns;
    }

    private Layer get(final Colors color) {
        return this.layers.get(color);
    }

    public Iterable<IPosition> neighbours(final IPosition position, final int radius) {
        return Positions.neighbours(position, radius);
    }

    private Board apply(final Colors side, final Iterable<IPosition> positions, final Iterable<IPosition> shadows, final Iterable<IPosition> lights) {

        final Map<IPosition, State> selfMutation = Maps.newHashMap();
        final Map<IPosition, State> othersMutation = Maps.newHashMap();

        for (final IPosition position : positions) {
            selfMutation.put(position, State.Upekkha);
            othersMutation.put(position, State.Mudita);
        }

        for (final IPosition position : shadows) {
            selfMutation.put(position, State.Karuna);
        }

        for (final IPosition position : lights) {
            selfMutation.put(position, State.Metta);
        }

        final Map<Colors, Layer> newLayers = Maps.newHashMap();
        for (final Colors color : this.getColors()) {
            final Layer newLayer = this.get(color).apply(color.equals(side) ? selfMutation : othersMutation);
            newLayers.put(color, newLayer);
        }
        return new Board(this.rows(), this.columns(), newLayers);
    }

    public Board apply(final Colors color, final PolyominoTranslatedInstance instance) {
        return instance == null ? this : this.apply(color, instance.positions(), instance.shadows(), instance.lights());
    }

    public boolean isMutable(final Colors color, final IPosition position) {
        return this.get(color).isMutable(position);
    }

    public boolean isLegal(final Colors color, final SortedSet<IPosition> positions) {
        return this.get(color).isLegal(positions);
    }

    public Iterable<IPosition> getLights(final Colors color) {
        return this.get(color).getLights().keySet();
    }

    public Iterable<IPosition> getSelves(final Colors color) {
        return this.get(color).getSelves().keySet();
    }

}