
package services.resources.local;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import services.applications.BlockplusApplicationInterface;
import blockplus.model.game.BlockplusGame;
import blockplus.model.game.BlockplusGameContext;

public class GameRandomMove extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final String room = (String) this.getRequest().getAttributes().get("room");
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final BlockplusGame game = application.getGame(room);
        final BlockplusGameContext newGameContext = game.start(1);
        application.setGame(room, new BlockplusGame(newGameContext));
        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation("RANDOM MOVE");
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}