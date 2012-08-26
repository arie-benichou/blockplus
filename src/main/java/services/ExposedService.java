
package services;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;
import org.restlet.util.ClientList;
import org.restlet.util.ServerList;

import services.applications.Main;

import com.google.common.base.Supplier;

public final class ExposedService {

    private final static ExposedService INSTANCE = new ExposedService();

    public static ExposedService getInstance() {
        return INSTANCE;
    }

    private final static class ComponentSupplier implements Supplier<Component> {

        @Override
        public Component get() {
            final Component component = new Component();
            final ServerList servers = component.getServers();
            servers.add(Protocol.HTTP, 8080);
            final ClientList clients = component.getClients();
            clients.add(Protocol.FILE);
            final VirtualHost defaultHost = component.getDefaultHost();
            defaultHost.attach("/blockplus", new Main("src/main/resources/web/public/components/blockplus"));
            return component;
        }

    }

    private final Component component;

    public Component getComponent() {
        return this.component;
    }

    private ExposedService() {
        this.component = new ComponentSupplier().get();
    }

    public ExposedService start() throws Exception {
        if (!this.getComponent().isStarted()) this.getComponent().start();
        return this;
    }

    public ExposedService stop() throws Exception {
        if (this.getComponent().isStarted()) this.getComponent().stop();
        return this;
    }

    private final static class ShutdownHook extends Thread {

        private final ExposedService service;

        public ShutdownHook(final ExposedService service) {
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
            Runtime.getRuntime().addShutdownHook(new ShutdownHook(ExposedService.getInstance().start()));
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
}