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

import interfaces.arbitration.RefereeInterface;
import interfaces.context.ContextInterface;

import java.util.List;

import javax.annotation.Nullable;

import blockplus.adversity.Adversity;
import blockplus.adversity.Side;
import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.move.Move;
import blockplus.piece.NullPieceComponent;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.base.Predicate;

public final class Context implements ContextInterface<ColorInterface> {

    // TODO à injecter
    private final static Predicate<Context> DEFAULT_PREDICATE = new Predicate<Context>() {

        @Override
        public boolean apply(@Nullable final Context context) {
            return !context.getPlayers().hasAlivePlayer();
        }

    };

    // TODO à injecter
    private final static ContextMutationHandler MOVE_HANDLER = new ContextMutationHandler();

    private ContextMutationHandler getMoveHandler() {
        return MOVE_HANDLER;
    }

    // TODO à injecter
    private final static RefereeInterface REFEREE = new Referee();

    private RefereeInterface getReferee() {
        return REFEREE;
    }

    private final Side side;

    @Override
    public Side getSide() {
        return this.side;
    }

    private final Adversity adversity;

    @Override
    public Adversity getAdversity() {
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

    public Context(
            final Side side,
            final Adversity adversity,
            final Players players,
            final Board board) {
        this.side = side;
        this.adversity = adversity;
        this.board = board;
        this.players = players;
    }

    private Context(final Context context) {
        this(context.getSide().next(), context.getAdversity(), context.getPlayers(), context.getBoard());
    }

    @Override
    public Context apply(final Move move) {
        return this.getMoveHandler().apply(this, move);
    }

    @Override
    public boolean isTerminal() {
        return DEFAULT_PREDICATE.apply(this);
    }

    @Override
    public List<Move> options() {
        return this.getReferee().getLegalMoves(this);
    }

    public ColorInterface getColor() {
        return this.adversity.get(this.getSide());
    }

    public Player getPlayer(final ColorInterface color) {
        return this.getPlayers().get(color);
    }

    public Player getPlayer() {
        return this.getPlayer(this.getColor());
    }

    @Override
    public Context forward(final boolean skipOnNullOption) {
        if (this.isTerminal()) return this;
        Context nextContext = new Context(this);
        if (skipOnNullOption) {
            final List<Move> nextOptions = nextContext.options();
            if (nextOptions.size() == 1 && nextOptions.get(0).isNull()) {// TODO extract Options class
                if (nextContext.getPlayer().isAlive()) nextContext = nextContext.apply(new Move(nextContext.getColor(), NullPieceComponent.getInstance()));
                nextContext = nextContext.forward();
            }
        }
        return nextContext;
    }

    @Override
    public Context forward() {
        return this.forward(true);
    }

}