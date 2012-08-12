
package blockplus.game;

import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.game.GameLoop.GameLoopResult;
import blockplus.player.PlayersInterface;

public class Game {

    private final Board<ColorInterface> board;

    public Board<ColorInterface> getBoard() {
        return this.board;
    }

    private final PlayersInterface players;

    public PlayersInterface getPlayers() {
        return this.players;
    }

    private Game(final Board<ColorInterface> board, final PlayersInterface players) {
        this.board = board;
        this.players = players;
    }

    public Game(final GameConfiguration configuration) {
        this(configuration.getBoard(), configuration.getPlayers());
    }

    public Game() {
        this(GameConfiguration.DEFAULT);
    }

    public class GameResult {

        private final GameLoopResult gameLoopResult;

        public GameResult(final GameLoopResult gameLoopResult) {
            this.gameLoopResult = gameLoopResult;
        }

        public GameLoopResult getLoopResult() {
            return this.gameLoopResult;
        }

    }

    public GameResult start() {
        final GameLoop gameLoop = new GameLoop(this.getBoard(), this.getPlayers());
        final GameLoopResult gameLoopResult = gameLoop.start();
        return new GameResult(gameLoopResult);
    }

}