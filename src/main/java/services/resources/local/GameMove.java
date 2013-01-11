
package services.resources.local;

import java.util.Set;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import services.applications.BlockplusApplicationInterface;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInterface;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import components.position.Position;
import components.position.PositionInterface;

public class GameMove extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final String room = (String) this.getRequest().getAttributes().get("room");
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame(room);
        final GameContext context = game.getInitialContext();
        // TODO checks...
        final int id = Integer.parseInt(this.getQueryValue("id"));
        final String json = this.getQueryValue("positions");
        final Gson gson = new Gson();
        final JsonParser parser = new JsonParser();
        final JsonArray array = parser.parse(json).getAsJsonArray();
        final Set<PositionInterface> positions = Sets.newLinkedHashSet();
        for (final JsonElement jsonElement : array) {
            final JsonArray asJsonArray = jsonElement.getAsJsonArray();
            final int row = gson.fromJson(asJsonArray.get(0), int.class);
            final int column = gson.fromJson(asJsonArray.get(1), int.class);
            positions.add(Position.from(row, column));
        }
        PieceInterface piece;
        if (positions.isEmpty()) { // TODO !!! à revoir
            piece = PieceComposite.from(id, Position.from(), positions);
        }
        else {
            piece = PieceComposite.from(id, positions.iterator().next(), positions);
        }
        final Move move = new Move(context.getColor(), piece);
        // TODO ! check if move is legal
        final GameContext newGameContext = context.apply(move);
        application.setGame(room, new Game(newGameContext.next()));
        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation(json);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}