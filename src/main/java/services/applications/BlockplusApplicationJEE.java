
package services.applications;

import static blockplus.model.board.State.Light;
import static blockplus.model.color.Colors.Blue;
import static blockplus.model.color.Colors.Green;
import static blockplus.model.color.Colors.Red;
import static blockplus.model.color.Colors.Yellow;
import static components.position.Position.Position;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.service.ConnectorService;

import services.resources.local.GameMove;
import services.resources.local.GameNullMove;
import services.resources.local.GameOptions;
import services.resources.local.GameRandomMove;
import services.resources.local.GameReset;
import services.resources.local.GameState;
import services.resources.local.GameStatePieces;
import services.resources.local.GameStatePiecesByColor;
import services.resources.local.NewGameRoom;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.game.BlockplusGame;
import blockplus.model.game.BlockplusGameContext;
import blockplus.model.game.BlockplusGameContextBuilder;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class BlockplusApplicationJEE extends Application implements BlockplusApplicationInterface {

    private final static Map<String, BlockplusGame> GAME_BY_ROOM = Maps.newConcurrentMap();

    static {
        // network game test
        final int rows = 20, columns = 20;
        final BoardLayer blueLayer = new BoardLayer(rows, columns).apply(Position(0, 0), Light);
        final BoardLayer yellowLayer = new BoardLayer(rows, columns).apply(Position(0, columns - 1), Light);
        final BoardLayer redLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, columns - 1), Light);
        final BoardLayer greenLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, 0), Light);
        final Board board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                .set(Blue, blueLayer)
                .set(Yellow, yellowLayer)
                .set(Red, redLayer)
                .set(Green, greenLayer)
                .build();

        final BlockplusGameContext gameContext1 = new BlockplusGameContextBuilder().setBoard(board).build();
        final BlockplusGame game1 = new BlockplusGame(gameContext1);

        GAME_BY_ROOM.put("0", game1);
    }

    //private Game game;

    private AtomicInteger counter;

    public BlockplusApplicationJEE() {
        super();
        // local game
        //this.game = new Game(); // TODO utiliser le contexte d'application
        this.counter = new AtomicInteger();
    }

    public BlockplusApplicationJEE(final Context parentContext) {
        super(parentContext);
    }

    public BlockplusApplicationJEE(final String root) {
        final ConnectorService connectorService = this.getConnectorService();
        final List<Protocol> clientProtocols = connectorService.getClientProtocols();
        clientProtocols.add(Protocol.HTTP);
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        final Router router = new Router(this.getContext());

        /* network
        router.attach("/data", NewBoardEvent.class);
        router.attach("/submit", NewPositionsSubmit.class);
        router.attach("/options", LegalMoves.class);
        router.attach("/pieces", AvailablePieces.class);
        */

        /* local */
        router.attach("/game-room/{room}/game-state", GameState.class);
        router.attach("/game-room/{room}/game-state-pieces", GameStatePieces.class);
        router.attach("/game-room/{room}/game-state-pieces-color", GameStatePiecesByColor.class);
        router.attach("/game-room/{room}/game-options", GameOptions.class);
        router.attach("/game-room/{room}/game-move", GameMove.class);
        router.attach("/game-room/{room}/game-random-move", GameRandomMove.class);
        router.attach("/game-room/{room}/game-null-move", GameNullMove.class);
        router.attach("/game-room/{room}/game-reset", GameReset.class);
        router.attach("/new-game-room", NewGameRoom.class);
        //router.attach("/game-room", GameRoom.class);
        //router.attach("/game-room/{location}", GameRoom.class);

        return router;
    }

    @Override
    public/*synchronized*/BlockplusGame getGame(final String room) {
        //if (room.equals("0")) return this.getGame();
        return GAME_BY_ROOM.get(room);
    }

    /*
    //@Override
    private synchronized Game getGame() {
        return this.game;
    }
    */

    @Override
    public/*synchronized*/void setGame(final String room, final BlockplusGame game) {
        //if (room.equals("0")) this.setGame(game);
        //else
        GAME_BY_ROOM.put(room, game);
    }

    /*
    //@Override
    private synchronized void setGame(final Game game) {
        this.game = game;
    }
    */

    @Override
    public AtomicInteger getCounter() {
        return this.counter;
    }

}