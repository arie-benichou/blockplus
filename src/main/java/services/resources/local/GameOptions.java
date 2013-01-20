
package services.resources.local;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import serialization.JSONSerializer;
import services.applications.BlockplusApplicationInterface;
import blockplus.model.game.BlockplusGame;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import components.position.PositionInterface;

public class GameOptions extends ServerResource {

    @Get
    public Representation getRepresentation() {

        final String room = (String) this.getRequest().getAttributes().get("room");
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final BlockplusGame game = application.getGame(room);
        final List<Move> legalMoves = game.getInitialContext().options();

        final Map<Pieces, List<Set<PositionInterface>>> legalPositionsByPiece = Maps.newTreeMap();
        for (final Move move : legalMoves) {
            if (!move.isNull()) { // TODO à revoir
                final PieceInterface piece = move.getPiece();
                final Pieces key = Pieces.get(piece.getId());
                List<Set<PositionInterface>> playablePositions = legalPositionsByPiece.get(key);
                if (playablePositions == null) {
                    playablePositions = Lists.newArrayList();
                    legalPositionsByPiece.put(key, playablePositions);
                }
                playablePositions.add(piece.getSelfPositions());
            }
        }

        final Gson gson = JSONSerializer.getInstance();
        final String json = gson.toJson(legalPositionsByPiece);

        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation(json);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;

    }
}