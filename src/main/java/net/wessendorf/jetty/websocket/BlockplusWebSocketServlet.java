
package net.wessendorf.jetty.websocket;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.google.common.collect.Lists;

@SuppressWarnings("serial")
public class BlockplusWebSocketServlet extends WebSocketServlet {

    private final Set<BlockplusWebSocket> connectedClients = new CopyOnWriteArraySet<BlockplusWebSocket>();

    @Override
    public boolean checkOrigin(final HttpServletRequest request, final String origin) {
        //return false;
        return true;
    }

    /**
     * Doing the upgrade of the http request
     */
    @Override
    public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
        System.out.println("new BlockplusWebSocket()");
        return new BlockplusWebSocket(request);
    }

    /**
     * Here happens the _real_ communication, outside of vanilla HTTP...
     */
    private class BlockplusWebSocket implements OnTextMessage {

        private final HttpServletRequest request;
        private Connection connection;

        public BlockplusWebSocket(final HttpServletRequest request) {
            this.request = request;
            System.out.println(Lists.newArrayList());

            final Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                System.out.println(headerNames.nextElement());
            }

            System.out.println(request.getRemoteAddr());
            System.out.println(request.getRemoteHost());
            System.out.println(request.getRemotePort());
            System.out.println(request.getRemoteUser());

            System.out.println(request.getLocalAddr());
            System.out.println(request.getLocalPort());
            System.out.println(request.getLocalName());
        }

        @Override
        public void onOpen(final Connection connection) {
            this.connection = connection;
            try {
                this.connection.sendMessage("Knock...Knock... Who is there ?");
                this.connection.setMaxIdleTime((int) TimeUnit.HOURS.toMillis(3));
                //System.out.println(this.connection.getMaxIdleTime());
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
            BlockplusWebSocketServlet.this.connectedClients.add(this);
        }

        @Override
        public void onClose(final int closeCode, final String message) {
            BlockplusWebSocketServlet.this.connectedClients.remove(this);
        }

        @Override
        public void onMessage(final String data) {
            try {
                this.connection.sendMessage(data);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }

    }

}