
package services.resources.local;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class GameRoom extends ServerResource {

    @Get
    public Representation getRepresentation() {
        final String location = (String) this.getRequest().getAttributes().get("location");
        //final int id = Integer.parseInt(this.getQueryValue("id"));
        this.setStatus(Status.SUCCESS_OK);
        System.out.println(location);
        final StringRepresentation representation = new StringRepresentation(location);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }

}