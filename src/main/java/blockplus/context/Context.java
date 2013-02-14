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
import java.util.Objects;

import javax.annotation.Nullable;

import blockplus.Color;
import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.move.Moves;
import blockplus.player.Player;
import blockplus.player.Players;

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

    Context(final Color side, final Board board, final Players players, final AdversityInterface<Color> adversity) {
        this.side = side;
        this.board = board;
        this.players = players;
        this.adversity = adversity;
    }

    @Override
    public Color getNextSide() {
        return this.getAdversity().getOpponent(this.getSide());
    }

    private Context(final Context context) {
        this(context.getNextSide(), context.getBoard(), context.getPlayers(), context.getAdversity());
    }

    @Override
    public Context apply(final MoveInterface move) {
        return new ContextBuilder()
                .setSide(this.getSide())
                .setAdversity(this.getAdversity())
                .setPlayers(this.getPlayers().apply(this.getPlayer().apply(move)))
                .setBoard(this.getBoard().apply(move))
                .build();
    }

    @Override
    public boolean isTerminal() {
        return TERMINATION_PREDICATE.apply(this);
    }

    @Override
    public List<MoveInterface> options() { // TODO Scala lazy
        return this.getReferee().getLegalMoves(this);
    }

    @Override
    public Context forward(final boolean skipOnNullOption) {
        if (this.isTerminal()) return this;
        Context nextContext = new Context(this);
        if (skipOnNullOption) {
            final List<MoveInterface> nextOptions = nextContext.options();
            if (nextOptions.size() == 1 && nextOptions.get(0).isNull()) {// TODO ? extract Options class
                if (nextContext.getPlayer().isAlive()) nextContext = nextContext.apply(Moves.getNullMove(nextContext.getSide()));
                nextContext = nextContext.forward();
            }
        }
        return nextContext;
    }

    @Override
    public Context forward() {
        return this.forward(true);
    }

    public Player getPlayer() {
        return this.getPlayers().get(this.getSide());
    }

    // TODO
    // TODO memoize
    @Override
    public String toString() {
        return "";
    }

    // TODO use toString
    // TODO memoize
    @Override
    public int hashCode() {
        return Objects.hash(TERMINATION_PREDICATE, REFEREE, this.side, this.adversity, this.players, this.board);
    }

    // TODO use hashCode
    // TODO memoize
    @Override
    public boolean equals(final Object object) {
        Preconditions.checkArgument(object instanceof Context);
        final Context that = (Context) object;
        return this.hashCode() == that.hashCode();
    }

}