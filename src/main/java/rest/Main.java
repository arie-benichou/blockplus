
package rest;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;
import org.restlet.util.ClientList;
import org.restlet.util.ServerList;

import rest.application.BlockplusApplication;

public class Main {

    /**
     * http://localhost:8080/blockplus/
     * http://localhost:8080/blockplus/static/new-board-server-sent-event.html
     */
    public static void main(final String[] args) throws Exception {

        final Component component = new Component();

        final ServerList servers = component.getServers();
        @SuppressWarnings("unused")
        final Server server = servers.add(Protocol.HTTP, 8080);

        final ClientList clients = component.getClients();
        clients.add(Protocol.FILE);

        final VirtualHost defaultHost = component.getDefaultHost();
        defaultHost.attach("/blockplus", new BlockplusApplication());

        component.start();
    }

}