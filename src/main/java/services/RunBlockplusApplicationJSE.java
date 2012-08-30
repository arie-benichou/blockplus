
package services;

import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;
import org.restlet.util.ClientList;
import org.restlet.util.ServerList;

import services.applications.BlockplusApplicationInterface;
import services.applications.BlockplusApplicationJSE;
import blockplus.model.game.Game;

import com.google.common.base.Supplier;

// TODO tester l'option controllerDaemon
// http://www.restlet.org/documentation/2.1/jse/engine/index.html?org/restlet/engine/connector/BaseHelper.html
public final class RunBlockplusApplicationJSE {

    private final static RunBlockplusApplicationJSE INSTANCE = new RunBlockplusApplicationJSE();

    public static RunBlockplusApplicationJSE getInstance() {
        return INSTANCE;
    }

    private final static class ComponentSupplier implements Supplier<Component> {

        @Override
        public Component get() {
            final Component component = new Component();
            final ServerList servers = component.getServers();
            final ClientList clients = component.getClients();
            clients.add(Protocol.FILE);
            final VirtualHost defaultHost = component.getDefaultHost();
            final BlockplusApplicationInterface application = new BlockplusApplicationJSE("src/main/resources/web/public/components/blockplus");
            application.setGame(new Game());
            defaultHost.attach("/blockplus", (Restlet) application);
            servers.add(Protocol.HTTP, 8080);
            /*
            for (final Server server : servers) {
                final Series<Parameter> parameters = server.getContext().getParameters();
                parameters.add("persistingConnections", "true");
            }
            */
            return component;
        }
    }

    private final Component component;

    public Component getComponent() {
        return this.component;
    }

    private RunBlockplusApplicationJSE() {
        this.component = new ComponentSupplier().get();
    }

    public RunBlockplusApplicationJSE start() throws Exception {
        if (!this.getComponent().isStarted()) this.getComponent().start();
        return this;
    }

    public RunBlockplusApplicationJSE stop() throws Exception {
        if (this.getComponent().isStarted()) this.getComponent().stop();
        return this;
    }

    private final static class ShutdownHook extends Thread {

        private final RunBlockplusApplicationJSE service;

        public ShutdownHook(final RunBlockplusApplicationJSE service) {
            this.service = service;
        }

        @Override
        public void run() {
            try {
                this.service.stop();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    // http://localhost:8080/blockplus/
    public static void main(final String[] args) {
        try {
            final RunBlockplusApplicationJSE exposedService = RunBlockplusApplicationJSE.getInstance();
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(exposedService.start()));
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
}