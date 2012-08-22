
package blockplus.model.game;

import java.util.List;

import javax.annotation.Nullable;

import blockplus.model.arbitration.Referee;
import blockplus.model.board.Board;
import blockplus.model.color.ColorInterface;
import blockplus.model.move.Move;
import blockplus.model.move.MoveHandler;
import blockplus.model.player.PlayerInterface;
import blockplus.model.player.PlayersInterface;

import com.google.common.base.Predicate;

public class GameContext {

    public final static GameContext DEFAULT = new GameContextBuilder().build();

    private final static MoveHandler MOVE_HANDLER = new MoveHandler(); // TODO à injecter
    private final static Referee REFEREE = new Referee(); // TODO à injecter
    //private final static EstimationInterface ESTIMATION_FUNCTION = new Estimation1(); // TODO à injecter

    public final static Predicate<GameContext> DEFAULT_PREDICATE = new Predicate<GameContext>() {

        @Override
        public boolean apply(@Nullable final GameContext context) {
            return context.getPlayers().hasAlivePlayers();
        }

    };

    private final Board board;
    private final ColorInterface color;
    private final PlayersInterface players;
    private final PlayerInterface player;
    private final PlayerInterface opponent;

    public GameContext(final Board board, final PlayersInterface players, final ColorInterface color) {
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

    public GameContext apply(final Move move) {
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

    public GameContext next() {
        return new GameContext(this.getBoard(), this.getPlayers(), this.getOpponent().getColor());
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

    public GameContext update(final PlayersInterface newPlayers) {
        return new GameContext(this.getBoard(), newPlayers, this.getColor());
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