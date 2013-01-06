
package services.resources.local;

import java.util.HashSet;

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

import components.position.Position;
import components.position.PositionInterface;

public class GameNullMove extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame();
        final GameContext context = game.getInitialContext();
        final PieceInterface nullPiece = PieceComposite.from(0, Position.from(), new HashSet<PositionInterface>()); // TODO extract constant
        final Move move = new Move(context.getColor(), nullPiece);
        // TODO ! check if move is legal
        final GameContext newGameContext = context.apply(move);
        application.setGame(new Game(newGameContext.next()));
        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation("NULL MOVE");
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}