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

package game.blockplus.board;

import static game.blockplus.board.Layer.State.Metta;
import static game.blockplus.board.Layer.State.Mudita;
import static game.blockplus.board.Layer.State.Nirvana;
import static game.blockplus.board.Layer.State.Upekkha;
import game.blockplus.board.Layer.State;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import components.cells.Cells;
import components.cells.ICells;
import components.cells.Positions;
import components.cells.Positions.Position;

public final class Layer implements Supplier<ICells<State>> {

    /**
     * Possible states for a cell in a layer of a board. Since each color is
     * mapped on its own layer, the board is made of 4 layers : one for Blue,
     * one for Yellow, one for Red and one for Green.
     */
    public enum State {

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

        static {
            // only here for code coverage noise elimination
            State.valueOf(Nirvana.toString());
        }

    }

    private final static class IsMutablePredicate implements Predicate<Position> {

        private final ICells<State> stateBoard;

        public IsMutablePredicate(final ICells<State> stateBoard) {
            this.stateBoard = stateBoard;
        }

        @Override
        public boolean apply(@Nullable final Position position) {
            final State state = this.stateBoard.get(position);
            return state.equals(Nirvana) || state.equals(Metta);
        }

    };

    private final static Predicate<Entry<Position, State>> SELF_PREDICATE = new Predicate<Map.Entry<Position, State>>() {

        @Override
        public boolean apply(final Entry<Position, State> entry) {
            return entry.getValue().equals(Upekkha);
        }

    };

    private final static Predicate<Entry<Position, State>> LIGHT_PREDICATE = new Predicate<Map.Entry<Position, State>>() {

        @Override
        public boolean apply(final Entry<Position, State> entry) {
            return entry.getValue().equals(Metta);
        }

    };

    private final ICells<State> stateBoard;
    private final IsMutablePredicate isMutablePredicate;

    private volatile Map<Position, State> selves;
    private volatile Map<Position, State> lights;

    private Layer(final ICells<State> stateBoard) {
        this.stateBoard = stateBoard;
        this.isMutablePredicate = new IsMutablePredicate(stateBoard);
    }

    public Layer(final Positions cellPositions) {
        this(Cells.from(cellPositions, Nirvana, Mudita));
    }

    public boolean isMutable(final Position position) {
        return this.isMutablePredicate.apply(position);
    }

    public boolean isLegal(final Iterable<Position> positions) {
        boolean containsLight = false;
        for (final Position position : positions) {
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

    public boolean isLight(final Position position) {
        return this.getLights().containsKey(position);
    }

    public Board apply(final Set<Position> positions, final Set<Position> shadows, final Set<Position> lights) {
        return null;
    }

    public Layer apply(final Map<Position, State> layerMutation) {
        final Map<Position, State> consistentMutations = Maps.newHashMap();
        for (final Entry<Position, State> mutation : layerMutation.entrySet())
            if (this.isMutable(mutation.getKey())) consistentMutations.put(mutation.getKey(), mutation.getValue());
        return new Layer(this.get().apply(consistentMutations));
    }

    public Layer apply(final Position position, final State state) {
        if (!this.isMutable(position)) return this;
        final Map<Position, State> consistentMutation = Maps.newHashMap();
        consistentMutation.put(position, state);
        return new Layer(this.get().apply(consistentMutation));
    }

    public Map<Position, State> getLights() {
        Map<Position, State> value = this.lights;
        if (value == null) synchronized (this) {
            if ((value = this.lights) == null) this.lights = value = this.get().filter(LIGHT_PREDICATE);
        }
        return value;
    }

    public Map<Position, State> getSelves() {
        Map<Position, State> value = this.selves;
        if (value == null) synchronized (this) {
            if ((value = this.selves) == null) this.selves = value = this.get().filter(SELF_PREDICATE);
        }
        return value;
    }

    // TODO memoize
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

    // TODO memoize
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

    public Position position(final int i, final int j) {
        return this.get().position(i, i);
    }

    public Position position(final Position position) {
        return this.get().position(position.id());
    }

    public boolean isMutable(final int i, final int j) {
        return this.isMutable(this.get().position(i, j));
    }

}