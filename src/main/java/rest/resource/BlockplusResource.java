
package rest.resource;

import java.util.Map;
import java.util.TreeMap;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import rest.application.BlockplusApplication;
import freemarker.template.Configuration;

public class BlockplusResource extends ServerResource {

    @Get
    public Representation getRepresentation() {

        final BlockplusApplication application = (BlockplusApplication) this.getApplication();
        final Configuration freeMarkerConfiguration = application.getFreeMarkerConfiguration();

        final Map<String, Object> dataModel = new TreeMap<String, Object>();
        dataModel.put("resourceRef", this.getRequest().getResourceRef());
        dataModel.put("rootRef", this.getRequest().getRootRef());

        return new TemplateRepresentation("home.html", freeMarkerConfiguration, dataModel, MediaType.TEXT_HTML);
    }

}