/*
 * Copyright 2012 Arie Benichou
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

import static blockplus.model.board.State.*;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;



import blockplus.model.piece.PieceInterface;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import components.board.Board;
import components.board.BoardInterface;
import components.position.PositionInterface;

public final class BoardLayer implements Supplier<BoardInterface<State>> {

    private final static class IsMutablePredicate implements Predicate<PositionInterface> {

        private final BoardInterface<State> stateBoard;

        public IsMutablePredicate(final BoardInterface<State> stateBoard) {
            this.stateBoard = stateBoard;
        }

        @Override
        public boolean apply(@Nullable final PositionInterface position) {
            final State state = this.stateBoard.get(position);
            return state.is(None) || state.is(Light);
        }

    };

    private final static Predicate<Entry<PositionInterface, State>> SELF_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(@Nullable final Entry<PositionInterface, State> entry) {
            return entry.getValue().is(Self);
        }

    };

    private final static Predicate<Entry<PositionInterface, State>> OTHER_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(@Nullable final Entry<PositionInterface, State> entry) {
            return entry.getValue().is(Other);
        }

    };

    private final static Predicate<Entry<PositionInterface, State>> SHADOW_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(@Nullable final Entry<PositionInterface, State> entry) {
            return entry.getValue().is(Shadow);
        }
    };

    private final static Predicate<Entry<PositionInterface, State>> LIGHT_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(@Nullable final Entry<PositionInterface, State> entry) {
            return entry.getValue().is(Light);
        }

    };

    private final BoardInterface<State> stateBoard;
    private final IsMutablePredicate isMutablePredicate;

    private transient volatile Map<PositionInterface, State> selves;
    private transient volatile Map<PositionInterface, State> others;
    private transient volatile Map<PositionInterface, State> shadows;
    private transient volatile Map<PositionInterface, State> lights;

    // TODO static factory method, then make it private
    private BoardLayer(final BoardInterface<State> stateBoard) {
        this.stateBoard = stateBoard;
        this.isMutablePredicate = new IsMutablePredicate(stateBoard);
    }

    public BoardLayer(final int rows, final int columns) {
        this(Board.from(rows, columns, None, Other));
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

    public boolean isNone(final PositionInterface position) {
        return this.get().get(position).is(None);
    }

    public boolean isSelf(final PositionInterface position) {
        return this.getSelves().containsKey(position);
    }

    public boolean isShadow(final PositionInterface position) {
        return this.getShadows().containsKey(position);
    }

    public boolean isLight(final PositionInterface position) {
        return this.getLights().containsKey(position);
    }

    public boolean isOther(final PositionInterface position) {
        return this.getOthers().containsKey(position);
    }

    public boolean isSelfOrOther(final PositionInterface position) {
        return this.isSelf(position) || this.isOther(position);
    }

    public BoardLayer apply(final PieceInterface piece) {
        final MutationBuilder boardMutationBuilder = new MutationBuilder()
                .setSelfPositions(piece.getSelfPositions())
                .setShadowPositions(piece.getShadowPositions())
                .setLightPositions(piece.getLightPositions());
        return this.apply(boardMutationBuilder.build());
    }

    public BoardLayer apply(final Map<PositionInterface, State> boardMutation) {
        final Map<PositionInterface, State> consistentMutations = Maps.newHashMap();
        for (final Entry<PositionInterface, State> mutation : boardMutation.entrySet())
            if (this.isMutable(mutation.getKey())) consistentMutations.put(mutation.getKey(), mutation.getValue());
        return new BoardLayer(this.get().apply(consistentMutations));
    }

    public BoardLayer apply(final PositionInterface position, final State state) {
        if (!this.isMutable(position)) return this;
        final Map<PositionInterface, State> consistentMutation = Maps.newHashMap();
        consistentMutation.put(position, state);
        return new BoardLayer(this.get().apply(consistentMutation));
    }

    public Map<PositionInterface, State> getShadows() {
        Map<PositionInterface, State> value = this.shadows;
        if (value == null) synchronized (this) {
            if ((value = this.shadows) == null) this.shadows = value = this.get().filter(SHADOW_PREDICATE);
        }
        return value;
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

    public Map<PositionInterface, State> getOthers() {
        Map<PositionInterface, State> value = this.others;
        if (value == null) synchronized (this) {
            if ((value = this.others) == null) this.others = value = this.get().filter(OTHER_PREDICATE);
        }
        return value;
    }

}