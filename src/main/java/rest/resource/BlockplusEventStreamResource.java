
package rest.resource;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class BlockplusEventStreamResource extends ServerResource {

    private final static MediaType TEXT_EVENT_STREAM = new MediaType("text/event-stream");

    @Get
    public Representation getRepresentation() {
        this.setStatus(Status.SUCCESS_OK);
        final Representation representation = new StringRepresentation("" +
                "retry: 4000\n" +
                "data: some message1\n" +
                "\n",
                TEXT_EVENT_STREAM);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}