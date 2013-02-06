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

import java.util.List;

import javax.annotation.Nullable;

import blockplus.adversity.Adversity;
import blockplus.adversity.Side;
import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.move.Move;
import blockplus.player.PlayerInterface;
import blockplus.player.Players;

import com.google.common.base.Predicate;

public class Context implements ContextInterface {

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
    private final static Referee REFEREE = new Referee();

    private Referee getReferee() {
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
    public ColorInterface get() {
        return this.adversity.get(this.getSide());
    }

    @Override
    public ColorInterface getNext() {
        return this.adversity.getNext(this.getSide());
    }

    @Override
    public List<Move> options() {
        return this.getReferee().getOrderedLegalMoves(this);
    }

    @Override
    public Context forward() {
        return new Context(this);
    }

    private PlayerInterface getPlayer(final ColorInterface color) {
        return this.getPlayers().get(color);
    }

    public PlayerInterface getPlayer() {
        return this.getPlayer(this.get());
    }

    public PlayerInterface getNextPlayer() {
        return this.getPlayer(this.getNext());
    }

}