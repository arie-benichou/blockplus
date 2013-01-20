
package services.resources.local;

import java.util.HashSet;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import services.applications.BlockplusApplicationInterface;
import blockplus.model.game.BlockplusGame;
import blockplus.model.game.BlockplusGameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInterface;

import components.position.Position;
import components.position.PositionInterface;

public class GameNullMove extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final String room = (String) this.getRequest().getAttributes().get("room");
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final BlockplusGame game = application.getGame(room);
        final BlockplusGameContext context = game.getInitialContext();
        final PieceInterface nullPiece = PieceComposite.from(0, Position.from(), new HashSet<PositionInterface>()); // TODO extract constant
        final Move move = new Move(context.getColor(), nullPiece);
        // TODO ! check if move is legal
        final BlockplusGameContext newGameContext = context.apply(move);
        application.setGame(room, new BlockplusGame(newGameContext.next()));
        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation("NULL MOVE");
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}