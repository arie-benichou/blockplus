
package services.resources;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import serialization.PiecesBagEncoding;
import services.applications.BlockplusApplicationInterface;
import blockplus.model.color.Colors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;

public class AvailablePieces extends ServerResource {

    @Get
    public Representation getRepresentation() {

        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame();
        
        GameContext context = game.getInitialContext();
        PiecesBag bag = context.getPlayers().get(Colors.Green).getPieces();
        PiecesBag effectiveBag = bag.remove(Pieces.PIECE0);
        String jsonBag = PiecesBagEncoding.encode(effectiveBag);

        this.setStatus(Status.SUCCESS_OK);
        StringRepresentation representation = new StringRepresentation(jsonBag);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;

    }
}