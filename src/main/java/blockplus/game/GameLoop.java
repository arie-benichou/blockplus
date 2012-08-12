
package blockplus.game;

import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.game.GameRound.GameRoundResult;
import blockplus.player.PlayersInterface;

public class GameLoop {

    private final Board<ColorInterface> board;

    public Board<ColorInterface> getBoard() {
        return this.board;
    }

    private final PlayersInterface players;

    public PlayersInterface getPlayers() {
        return this.players;
    }

    public GameLoop(final Board<ColorInterface> board, final PlayersInterface players) {
        this.board = board;
        this.players = players;
    }

    public class GameLoopResult {

        private final GameRoundResult gameRoundResult;

        public GameLoopResult(final GameRoundResult gameRoundResult) {
            this.gameRoundResult = gameRoundResult;
        }

        public GameRoundResult getLastRoundResult() {
            return this.gameRoundResult;
        }

    }

    public GameLoopResult start() {
        GameRoundResult gameRoundResult = new GameRoundResult(this.getBoard(), this.getPlayers());
        while (gameRoundResult.getPlayers().hasAlivePlayers()) {
            final GameRound gameRound = new GameRound(gameRoundResult);
            gameRoundResult = gameRound.start();
        }
        return new GameLoopResult(gameRoundResult);
    }

}