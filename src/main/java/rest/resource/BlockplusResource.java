
package rest.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class BlockplusResource extends ServerResource {

    @Get
    public String getResource() {
        return "Hello World!";
    }

}