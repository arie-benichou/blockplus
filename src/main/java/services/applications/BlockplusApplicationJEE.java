
package services.applications;

import java.util.List;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.service.ConnectorService;

import services.resources.NewBoardEvent;
import blockplus.model.game.Game;

public class BlockplusApplicationJEE extends Application implements BlockplusApplicationInterface {

    private Game game;

    public BlockplusApplicationJEE() {
        super();
        this.game = new Game(); // TODO utiliser le contexte d'application
    }

    public BlockplusApplicationJEE(final Context parentContext) {
        super(parentContext);
    }

    public BlockplusApplicationJEE(final String root) {
        final ConnectorService connectorService = this.getConnectorService();
        final List<Protocol> clientProtocols = connectorService.getClientProtocols();
        clientProtocols.add(Protocol.HTTP);
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        final Router router = new Router(this.getContext());
        router.attach("/data", NewBoardEvent.class);
        return router;
    }

    @Override
    public synchronized Game getGame() {
        return this.game;
    }

    @Override
    public synchronized void setGame(final Game game) {
        this.game = game;
    }

}