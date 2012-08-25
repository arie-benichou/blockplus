
package rest.resource;

import static blockplus.model.board.State.*;
import static components.position.Position.*;

import java.util.Map;
import java.util.Random;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import rest.json.CellEncodingDemo;
import blockplus.model.board.State;

import com.google.common.collect.Maps;
import components.board.Board;
import components.board.BoardInterface;
import components.position.PositionInterface;

public class BlockplusNewBoardEventStreamResource extends ServerResource {

    private final static MediaType TEXT_EVENT_STREAM = new MediaType("text/event-stream");

    @Get
    public Representation getRepresentation() {
        this.setStatus(Status.SUCCESS_OK);

        final BoardInterface<State> board = Board.from(3, 3, None, Other);
        final Map<PositionInterface, State> mutation = Maps.newHashMap();
        mutation.put(Position(0, 0), Shadow);
        mutation.put(Position(1, 1), Self);
        mutation.put(Position(2, 2), Other);

        final Random random = new Random();
        mutation.put(Position(random.nextInt(1024) % 3, random.nextInt(1024) % 3), Light);

        final BoardInterface<State> newBoard = board.apply(mutation);
        final String json = CellEncodingDemo.encode(newBoard);

        final Representation representation = new StringRepresentation("" +
                "retry:1000\n" +
                "data:" + json + "\n" +
                "\n",
                TEXT_EVENT_STREAM);

        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}