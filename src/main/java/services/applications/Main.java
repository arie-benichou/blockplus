
package services.applications;

import java.io.File;
import java.util.List;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.service.ConnectorService;

import services.resources.NewBoardEvent;
import blockplus.model.game.Game;

import com.google.common.base.Preconditions;

public class Main extends Application {

    private final File root;

    private Game game;

    public File getRoot() {
        return this.root;
    }

    private static File checkRoot(final String root) {
        final File file = new File(root);
        Preconditions.checkState(file.exists(), root + " does not exist.");
        return file;
    }

    public Main(final String root) {
        this.root = checkRoot(root);
        final ConnectorService connectorService = this.getConnectorService();
        final List<Protocol> clientProtocols = connectorService.getClientProtocols();
        clientProtocols.add(Protocol.FILE);
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        final Router router = new Router(this.getContext());
        router.attach("/data", NewBoardEvent.class); //Add route for new board server-sent-event final mock
        final LocalReference localReference = LocalReference.createFileReference(this.getRoot());
        final Directory staticDirectory = new Directory(this.getContext(), localReference);
        router.attach("/", staticDirectory); //Add route for home directory
        return router;
    }

    public synchronized Game getGame() {
        return this.game;
    }

    public synchronized void setGame(final Game game) {
        this.game = game;
    }

}