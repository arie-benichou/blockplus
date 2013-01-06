
package services.resources.local;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import serialization.PiecesBagEncoding;
import services.applications.BlockplusApplicationInterface;
import blockplus.model.color.PrimeColors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.PlayersInterface;

public class GameStatePiecesByColor extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame();
        final GameContext context = game.getInitialContext();
        final String color = this.getQueryValue("color");
        final PlayersInterface players = context.getPlayers();
        final PiecesBag bag = players.get(PrimeColors.get(color)).getPieces();
        final PiecesBag effectiveBag = bag.remove(Pieces.PIECE0);
        final String jsonBag = PiecesBagEncoding.encode(effectiveBag);
        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation(jsonBag);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}