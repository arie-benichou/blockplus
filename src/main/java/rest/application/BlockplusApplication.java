
package rest.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import rest.resource.BlockplusResource;

public class BlockplusApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        final Router router = new Router(this.getContext());
        router.attach("/", BlockplusResource.class);
        return router;
    }

}