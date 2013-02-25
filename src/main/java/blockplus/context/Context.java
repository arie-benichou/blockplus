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

package blockplus.context;

import interfaces.adversity.AdversityInterface;
import interfaces.arbitration.RefereeInterface;
import interfaces.context.ContextInterface;
import interfaces.move.MoveInterface;

import java.util.List;

import javax.annotation.Nullable;

import blockplus.Color;
import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.move.Move;
import blockplus.move.Moves;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

public final class Context implements ContextInterface<Color> {

    // TODO inject
    private final static Predicate<Context> TERMINATION_PREDICATE = new Predicate<Context>() {

        @Override
        public boolean apply(@Nullable final Context context) {
            return !context.getPlayers().hasAlivePlayer();
        }

    };

    // TODO inject
    private final static RefereeInterface REFEREE = new Referee();

    private RefereeInterface getReferee() {
        return REFEREE;
    }

    private final MoveHistory moveHistory;

    public MoveHistory getMoveHistory() {
        return this.moveHistory;
    }

    private final Color side;

    @Override
    public Color getSide() {
        return this.side;
    }

    private final AdversityInterface<Color> adversity;

    @Override
    public AdversityInterface<Color> getAdversity() {
        return this.adversity;
    }

    private final Players players;

    @Override
    public Players getPlayers() {
        return this.players;
    }

    private final Board board;

    @Override
    public Board getBoard() {
        return this.board;
    }

    private volatile List<MoveInterface> options;

    Context(final Color side,
            final MoveHistory moveHistory,
            final Board board,
            final Players players,
            final AdversityInterface<Color> adversity) {
        this.side = side;
        this.moveHistory = moveHistory;
        this.board = board;
        this.players = players;
        this.adversity = adversity;
    }

    @Override
    public Color getNextSide() {
        return this.getAdversity().getOpponent(this.getSide());
    }

    private Context(final Context context) {
        this(
                context.getNextSide(),
                context.getMoveHistory(),
                context.getBoard(),
                context.getPlayers(),
                context.getAdversity());
    }

    private Context(final Context context, final MoveInterface move) {
        this(
                context.getSide(),
                context.getMoveHistory().play((Move) move),
                context.getBoard().apply(move),
                context.getPlayers().apply(context.getPlayer().apply(move)),
                context.getAdversity());
    }

    @Override
    public Context apply(final MoveInterface move) {
        // could use options().contains(move) if Options data structure was a sorted map...
        Preconditions.checkState(this.getReferee().isLegal(this, move));
        return new Context(this, move);
    }

    @Override
    public boolean isTerminal() {
        return TERMINATION_PREDICATE.apply(this);
    }

    @Override
    public List<MoveInterface> options() {
        List<MoveInterface> value = this.options;
        if (value == null) synchronized (this) {
            if ((value = this.options) == null) this.options = value = this.getReferee().getLegalMoves(this);
        }
        return value;
    }

    @Override
    public Context forward() {
        if (this.isTerminal()) return this;
        Context nextContext = new Context(this);
        final List<MoveInterface> nextOptions = nextContext.options();
        if (nextOptions.size() == 1 && nextOptions.get(0).isNull()) {// TODO ? extract Options class
            if (nextContext.getPlayer().isAlive()) nextContext = nextContext.apply(Moves.getNullMove(nextContext.getSide()));
            nextContext = nextContext.forward();
        }
        return nextContext;
    }

    public Player getPlayer() {
        return this.getPlayers().get(this.getSide());
    }

    // TODO memoize
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getBoard())
                .toString();
    }

    // TODO ? use toString output
    // TODO memoize
    @Override
    public int hashCode() {
        return Objects.hashCode(TERMINATION_PREDICATE, REFEREE, this.side, this.adversity, this.players, this.board);
    }

    // TODO check collisions
    @Override
    public boolean equals(final Object object) {
        Preconditions.checkArgument(object instanceof Context);
        final Context that = (Context) object;
        return this.hashCode() == that.hashCode();
    }

}