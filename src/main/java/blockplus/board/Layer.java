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

import static blockplus.board.Layer.State.Metta;
import static blockplus.board.Layer.State.Mudita;
import static blockplus.board.Layer.State.Nirvana;
import static blockplus.board.Layer.State.Upekkha;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import blockplus.board.Layer.State;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import components.board.Board;
import components.board.BoardInterface;
import components.board.Symbol;
import components.position.PositionInterface;

public final class Layer implements Supplier<BoardInterface<State>> {

    /**
     * Possible states for a cell in a layer of a board. Since each color is
     * mapped on its own layer, the board is made of 4 layers : one for Blue,
     * one for Yellow, one for Red and one for Green.
     */
    public enum State implements Symbol {

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
        Nirvana

    }

    private final static class IsMutablePredicate implements Predicate<PositionInterface> {

        private final BoardInterface<State> stateBoard;

        public IsMutablePredicate(final BoardInterface<State> stateBoard) {
            this.stateBoard = stateBoard;
        }

        @Override
        public boolean apply(@Nullable final PositionInterface position) {
            final State state = this.stateBoard.get(position);
            return state.equals(Nirvana) || state.equals(Metta);
        }

    };

    private final static Predicate<Entry<PositionInterface, State>> SELF_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(@Nullable final Entry<PositionInterface, State> entry) {
            return entry.getValue().equals(Upekkha);
        }

    };

    private final static Predicate<Entry<PositionInterface, State>> LIGHT_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(@Nullable final Entry<PositionInterface, State> entry) {
            return entry.getValue().equals(Metta);
        }

    };

    private final BoardInterface<State> stateBoard;
    private final IsMutablePredicate isMutablePredicate;

    private volatile Map<PositionInterface, State> selves;
    private volatile Map<PositionInterface, State> lights;

    private Layer(final BoardInterface<State> stateBoard) {
        this.stateBoard = stateBoard;
        this.isMutablePredicate = new IsMutablePredicate(stateBoard);
    }

    public Layer(final int rows, final int columns) {
        this(Board.from(rows, columns, Nirvana, Mudita));
    }

    public boolean isMutable(final PositionInterface position) {
        return this.isMutablePredicate.apply(position);
    }

    public boolean isLegal(final Iterable<PositionInterface> positions) {
        boolean containsLight = false;
        for (final PositionInterface position : positions) {
            if (!this.isMutable(position)) return false;
            if (this.isLight(position)) containsLight = true;
        }
        return containsLight;
    }

    @Override
    public BoardInterface<State> get() {
        return this.stateBoard;
    }

    public int rows() {
        return this.get().rows();
    }

    public int columns() {
        return this.get().columns();
    }

    public boolean isLight(final PositionInterface position) {
        return this.getLights().containsKey(position);
    }

    public Layer apply(final Map<PositionInterface, State> boardMutation) {
        final Map<PositionInterface, State> consistentMutations = Maps.newHashMap();
        for (final Entry<PositionInterface, State> mutation : boardMutation.entrySet())
            if (this.isMutable(mutation.getKey())) consistentMutations.put(mutation.getKey(), mutation.getValue());
        return new Layer(this.get().apply(consistentMutations));
    }

    public Layer apply(final PositionInterface position, final State state) {
        if (!this.isMutable(position)) return this;
        final Map<PositionInterface, State> consistentMutation = Maps.newHashMap();
        consistentMutation.put(position, state);
        return new Layer(this.get().apply(consistentMutation));
    }

    public Map<PositionInterface, State> getLights() {
        Map<PositionInterface, State> value = this.lights;
        if (value == null) synchronized (this) {
            if ((value = this.lights) == null) this.lights = value = this.get().filter(LIGHT_PREDICATE);
        }
        return value;
    }

    public Map<PositionInterface, State> getSelves() {
        Map<PositionInterface, State> value = this.selves;
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
    public boolean equals(final Object object) { // TODO use toString output
        Preconditions.checkArgument(object instanceof Layer);
        boolean isEqual = false;
        if (object == this) isEqual = true;
        else {
            final Layer that = (Layer) object;
            if (this.rows() == that.rows() && this.columns() == that.columns()) {
                if (this.get().initialSymbol().equals(that.get().initialSymbol())) {
                    if (this.get().undefinedSymbol().equals(that.get().undefinedSymbol())) {
                        isEqual = this.get().filter(null).equals(that.get().filter(null));
                    }
                }
            }
        }
        return isEqual;
    }

    @Override
    public String toString() {
        return this.get().toString();
    }

}