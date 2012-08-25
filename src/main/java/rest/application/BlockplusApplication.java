
package rest.application;

import java.io.File;
import java.util.List;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.service.ConnectorService;

import rest.resource.BlockplusEventStreamResource;
import rest.resource.BlockplusResource;
import freemarker.template.Configuration;

public class BlockplusApplication extends Application {

    private final static String WEB_ROOT_PATH = "src/main/resources/web"; // TODO à injecter
    private static final String TEMPLATES_DIRECTORY_NAME = "/templates"; // TODO à injecter

    private final Configuration freeMarkerConfiguration;

    public Configuration getFreeMarkerConfiguration() {
        return this.freeMarkerConfiguration;
    }

    private Configuration freeMarkerConfiguration() {
        Configuration freeMarkerConfiguration = null;
        try {
            final File templatesDirectory = new File(BlockplusApplication.WEB_ROOT_PATH, TEMPLATES_DIRECTORY_NAME);
            freeMarkerConfiguration = new freemarker.template.Configuration();
            freeMarkerConfiguration.setDirectoryForTemplateLoading(templatesDirectory);
        }
        catch (final Exception e) {
            this.getLogger().severe("Unable to configure FreeMarker.");
            e.printStackTrace();
        }
        return freeMarkerConfiguration;
    }

    public BlockplusApplication() {

        final ConnectorService connectorService = this.getConnectorService();
        final List<Protocol> clientProtocols = connectorService.getClientProtocols();
        clientProtocols.add(Protocol.FILE);

        this.freeMarkerConfiguration = this.freeMarkerConfiguration();

        //this.getTunnelService().setExtensionsTunnel(true);
    }

    @Override
    public synchronized Restlet createInboundRoot() {

        final Router router = new Router(this.getContext());
        router.attach("/", BlockplusResource.class);

        {
            // Add route for image resources
            final LocalReference localReference = LocalReference.createFileReference(new File(WEB_ROOT_PATH, "images"));
            final Directory imgDirectory = new Directory(this.getContext(), localReference);
            router.attach("/images", imgDirectory);
        }

        {
            // Add route for stylesheets resources
            final LocalReference localReference = LocalReference.createFileReference(new File(WEB_ROOT_PATH, "stylesheets"));
            final Directory cssDirectory = new Directory(this.getContext(), localReference);
            router.attach("/stylesheets", cssDirectory);
        }

        {
            // Add route for static html content
            final LocalReference localReference = LocalReference.createFileReference(new File(WEB_ROOT_PATH, "static"));
            final Directory staticDirectory = new Directory(this.getContext(), localReference);
            router.attach("/static", staticDirectory);
        }

        {
            // Add route for server sent-event mock
            router.attach("/data", BlockplusEventStreamResource.class);
        }

        return router;
    }
}