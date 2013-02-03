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

package blockplus.game;

import java.util.List;

import javax.annotation.Nullable;

import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.player.PlayerInterface;
import blockplus.player.PlayersInterface;

import com.google.common.base.Predicate;

public class BlockplusGameContext {

    public final static BlockplusGameContext DEFAULT = new BlockplusGameContextBuilder().build();

    private final static MoveHandler MOVE_HANDLER = new MoveHandler(); // TODO à injecter
    private final static Referee REFEREE = new Referee(); // TODO à injecter
    //private final static EstimationInterface ESTIMATION_FUNCTION = new Estimation1(); // TODO à injecter

    public final static Predicate<BlockplusGameContext> DEFAULT_PREDICATE = new Predicate<BlockplusGameContext>() {

        @Override
        public boolean apply(@Nullable final BlockplusGameContext context) {
            return context.getPlayers().hasAlivePlayers();
        }

    };

    private final Board board;
    private final ColorInterface color;
    private final PlayersInterface players;
    private final PlayerInterface player;
    private final PlayerInterface opponent;

    public BlockplusGameContext(final Board board, final PlayersInterface players, final ColorInterface color) {
        this.board = board;
        this.players = players;
        this.color = color;
        this.player = players.get(color);
        this.opponent = players.get(this.player.getOpponentColor());
    }

    public Board getBoard() {
        return this.board;
    }

    public PlayersInterface getPlayers() {
        return this.players;
    }

    public ColorInterface getColor() {
        return this.color;
    }

    public PlayerInterface getPlayer() {
        return this.player;
    }

    public PlayerInterface getOpponent() {
        return this.opponent;
    }

    public BlockplusGameContext apply(final Move move) {
        return MOVE_HANDLER.handle(this, move);
    }

    public Double evaluate() {
        return this.evaluate(true);
    }

    public Double evaluate(final boolean isAbsolute) {
        int evaluation = this.getOpponent().getPieces().getWeight() - this.getPlayer().getPieces().getWeight();
        if (isAbsolute) {
            if (evaluation > 0) evaluation = 1000;
            else if (evaluation < 0) evaluation = -1000;
        }
        return 0.0 + evaluation;
    }

    public BlockplusGameContext next() {
        return new BlockplusGameContext(this.getBoard(), this.getPlayers(), this.getOpponent().getColor());
    }

    public List<Move> options() { // TODO !!! caching
        return REFEREE.getOrderedLegalMoves(this.getBoard(), this.getPlayer());
    }

    public Move getMove() {
        return this.getPlayer().getStrategy().chooseMove(this);
    }

    public boolean hasNext() {
        return DEFAULT_PREDICATE.apply(this);
    }

    @Override
    public String toString() {
        return null; // TODO
    }

    public BlockplusGameContext update(final PlayersInterface newPlayers) {
        return new BlockplusGameContext(this.getBoard(), newPlayers, this.getColor());
    }

    public Referee getReferee() {
        return REFEREE; // TODO
    }

    /*
    public Double estimate() { // TODO injecter la fonction d'évaluation et la fonction d'estimation
        return ESTIMATION_FUNCTION.estimate(this);
    }
    */

    public List<Move> sortedOptions() {
        final List<Move> options = this.options(); // TODO !! caching
        final List<Move> sortedOptions = this.getPlayer().getStrategy().sort(this, options);
        return sortedOptions;
    }

}