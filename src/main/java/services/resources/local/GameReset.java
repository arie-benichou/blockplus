
package services.resources.local;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import services.applications.BlockplusApplicationInterface;
import blockplus.model.game.BlockplusGame;

public class GameReset extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final String room = (String) this.getRequest().getAttributes().get("room");
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        application.setGame(room, new BlockplusGame());
        this.setStatus(Status.SUCCESS_OK);
        final StringRepresentation representation = new StringRepresentation("NEW GAME");
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}