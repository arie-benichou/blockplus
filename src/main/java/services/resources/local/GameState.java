
package services.resources.local;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import serialization.CellEncoding;
import serialization.JSONSerializer;
import services.applications.BlockplusApplicationInterface;
import blockplus.model.board.Board;
import blockplus.model.color.ColorInterface;
import blockplus.model.color.Colors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import components.board.BoardInterface;
import components.position.PositionInterface;

// TODO no cache header
// TODO EventStreamRepresentation
public class GameState extends ServerResource {

    private final static MediaType TEXT_EVENT_STREAM = new MediaType("text/event-stream");

    public GameState() {
        super();
    }

    public GameState(final Context context, final Request request, final Response response) {
        this.getVariants().add(new Variant(TEXT_EVENT_STREAM));
    }

    @Get
    public Representation getRepresentation() {

        this.setStatus(Status.SUCCESS_OK);

        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame();
        final GameContext context = game.getInitialContext();
        final ColorInterface color = context.getColor();
        final boolean isGameNotOver = context.hasNext();
        final Board board = context.getBoard();

        if (isGameNotOver) { // TODO GameRepresentation
            final List<Move> options = context.options();
            final Set<PositionInterface> potentialPositions = Sets.newHashSet();
            for (final Move move : options)
                potentialPositions.addAll(move.getPiece().getSelfPositions());
            final Gson gson = JSONSerializer.getInstance();
            final String playablePositionsData = gson.toJson(potentialPositions);
            final BoardInterface<ColorInterface> coloredBoard = board.colorize();
            final String json = CellEncoding.encode(coloredBoard);
            final Representation representation = new StringRepresentation("" +
                    "retry:1000\n" +
                    "data:" + "[[\"" + color + "\"]]" + "\n\n" +
                    "event:gamenotover\n" +
                    "data:" + json + "\n\n" +
                    "event:options\n" +
                    "data:" + playablePositionsData + "\n\n",
                    TEXT_EVENT_STREAM);
            representation.setCharacterSet(CharacterSet.UTF_8);
            return representation;
        }
        else { // TODO GameOverRepresentation
            final Map<PositionInterface, ColorInterface> mutations = board.colorize().filter(null);
            final BoardInterface<ColorInterface> coloredBoard =
                                                                components.board.Board.from(board.rows(), board.columns(), Colors.Black, Colors.Black,
                                                                        mutations);
            final String json = CellEncoding.encode(coloredBoard);
            final Representation representation = new StringRepresentation("" +
                    //"data:" + "[[\"Game Over\"]]" + "\n\n" +
                    "event:gameover\n" +
                    "data:" + json + "\n\n",
                    TEXT_EVENT_STREAM);
            representation.setCharacterSet(CharacterSet.UTF_8);
            return representation;
        }
    }

}