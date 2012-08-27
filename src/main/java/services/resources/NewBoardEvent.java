
package services.resources;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import serialization.CellEncoding;
import services.applications.Main;
import blockplus.model.board.Board;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;

// TODO à continuer...
public class NewBoardEvent extends ServerResource {

    private final static MediaType TEXT_EVENT_STREAM = new MediaType("text/event-stream");

    @Get
    public Representation getRepresentation() {

        this.setStatus(Status.SUCCESS_OK);

        final Main application = (Main) this.getApplication();
        final Game game = application.getGame();

        final GameContext newGameContext = game.start(1);
        application.setGame(new Game(newGameContext));

        final Board board = newGameContext.getBoard();
        final String json = CellEncoding.encode(board.colorize());

        final Representation representation = new StringRepresentation("" +
                "retry:1000\n" +
                "data:" + json + "\n" +
                "\n",
                TEXT_EVENT_STREAM);

        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}