
package services.resources;

import java.util.Map;

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
import blockplus.model.color.ColorInterface;
import blockplus.model.color.Colors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;

import components.board.BoardInterface;
import components.position.PositionInterface;

// TODO à continuer...
public class NewBoardEvent extends ServerResource {

    private final static MediaType TEXT_EVENT_STREAM = new MediaType("text/event-stream");

    @Get
    public Representation getRepresentation() {

        this.setStatus(Status.SUCCESS_OK);

        final Main application = (Main) this.getApplication();
        final Game game = application.getGame();

        final GameContext initialContext = game.getInitialContext();
        final boolean isGameNotOver = initialContext.hasNext();

        final ColorInterface color = initialContext.getColor().iterator().next();

        final GameContext newGameContext = game.start(1);
        application.setGame(new Game(newGameContext));

        final Board board = newGameContext.getBoard();
        BoardInterface<ColorInterface> coloredBoard = board.colorize();

        if (!isGameNotOver) {
            final Map<PositionInterface, ColorInterface> mutations = coloredBoard.filter(null);
            coloredBoard = components.board.Board.from(coloredBoard.rows(), coloredBoard.columns(), Colors.Black, Colors.Black, mutations);
        }

        final String json = CellEncoding.encode(coloredBoard);

        // TODO no cache header
        Representation representation = null;

        // TODO EventStreamRepresentation
        if (isGameNotOver) {
            representation = new StringRepresentation("" +
                    "retry:1000\n" +
                    "data:" + "[[\"" + color + " has just played\"]]" + "\n\n" +
                    "event:gamenotover\n" +
                    "data:" + json + "\n\n",
                    TEXT_EVENT_STREAM);
        }
        else {
            representation = new StringRepresentation("" +
                    "data:" + "[[\"Game is over\"]]" + "\n\n" +
                    "event:gameover\n" +
                    "data:" + json + "\n\n",
                    TEXT_EVENT_STREAM);
        }

        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}