
package services;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;
import org.restlet.util.ClientList;
import org.restlet.util.ServerList;

import services.applications.Main;

public class ExposeServices extends Application {

    // http://localhost:8080/blockplus/
    public static void main(final String[] args) throws Exception {
        final Component component = new Component();
        final ServerList servers = component.getServers();
        servers.add(Protocol.HTTP, 8080);
        final ClientList clients = component.getClients();
        clients.add(Protocol.FILE);
        final VirtualHost defaultHost = component.getDefaultHost();
        defaultHost.attach("/blockplus", new Main("src/main/resources/web/public/components/blockplus"));
        component.start();
    }

}