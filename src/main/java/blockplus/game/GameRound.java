
package blockplus.game;

import static blockplus.piece.Piece.Piece;

import java.util.List;
import java.util.Random;

import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.board.BoardRenderer;
import blockplus.color.ColorInterface;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.player.Player;
import blockplus.player.Players;
import blockplus.player.PlayersInterface;

import com.google.common.collect.Lists;

public class GameRound {

    private final Board<ColorInterface> board;
    private final PlayersInterface players;
    private final MoveHandler moveHandler; // TODO
    private final Referee referee; // TODO
    private final Random random; // TODO

    public GameRound(
            final Board<ColorInterface> board,
            final PlayersInterface players,
            final MoveHandler moveHandler, // TODO
            final Referee referee, // TODO
            final Random random // TODO
    ) {
        this.board = board;
        this.players = players;
        this.moveHandler = moveHandler;
        this.referee = referee;
        this.random = random;

    }

    public GameRound(final GameRoundResult previousGameRoundResult) {
        this.board = previousGameRoundResult.getBoard();
        this.players = previousGameRoundResult.getPlayers();
        this.moveHandler = new MoveHandler(); // TODO
        this.referee = new Referee(); // TODO
        this.random = new Random(); // TODO
    }

    public Board<ColorInterface> getBoard() {
        return this.board;
    }

    public PlayersInterface getPlayers() {
        return this.players;
    }

    public static class GameRoundResult {

        private final Board<ColorInterface> board;
        private final PlayersInterface players;

        public GameRoundResult(final Board<ColorInterface> board, final PlayersInterface players) {
            this.board = board;
            this.players = players;
        }

        public Board<ColorInterface> getBoard() {
            return this.board;
        }

        public PlayersInterface getPlayers() {
            return this.players;
        }

    }

    public GameRoundResult start() {
        Board<ColorInterface> board = this.getBoard();
        final List<Player> alivePlayers = Lists.newArrayList();
        final List<Player> deadPlayers = Lists.newArrayList(this.players.getDeadPlayers());
        for (final Player player : this.getPlayers().getAlivePlayers()) {
            final List<Move> legalMoves = this.referee.getOrderedLegalMoves(board, player);
            // TODO il devrait y avoir toujours le coup null 
            if (legalMoves.isEmpty()) deadPlayers.add(player);
            else {
                final Move randomLegalMove = legalMoves.get(this.random.nextInt(legalMoves.size()));
                // TODO MoveHandler doit notifier le board et les players
                board = this.moveHandler.handle(board, randomLegalMove);
                alivePlayers.add(new Player(player.getColor(), player.getAvailablePieces().remove(Piece(randomLegalMove.getPiece().getId()))));
            }
            // TODO MoveHandler doit notifier la vue
            BoardRenderer.render(board);

        }
        return new GameRoundResult(board, Players.Players(alivePlayers, deadPlayers));
    }

}