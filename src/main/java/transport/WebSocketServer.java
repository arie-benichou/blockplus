
package transport;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import transport.events.JoinRoomInterface;
import transport.events.UserInterface;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;

import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public class WebSocketServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter

    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, UserInterface> connectedUsersByIO = Maps.newConcurrentMap();

    private final Map<Integer, Set<UserInterface>> connectedUsersByRoom = Maps.newConcurrentMap();

    public Collection<UserInterface> getConnectedUsers() {
        return this.connectedUsersByIO.values();
    }

    private final EventBus eventBus = new EventBus();

    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.getEventBus().register(this);
        for (int i = 1; i <= 10; ++i) {
            this.connectedUsersByRoom.put(i, new CopyOnWriteArraySet<UserInterface>());
        }
    }

    /**
     * Doing the upgrade of the http request
     */
    @Override
    public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
        return new IO(this);
    }

    /**
     * Here happens the _real_ communication, outside of vanilla HTTP...
     */
    private class IO implements IOinterface {

        private final WebSocketServer server;

        private Connection connection = null;

        public IO(final WebSocketServer server) {
            this.server = server;
        }

        @Override
        public void say(final String message) {
            try {
                this.connection.sendMessage(message);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void say(final Object object) {
            this.say(object.toString());
        }

        @Override
        public void onOpen(final Connection connection) {
            this.connection = connection;
            this.say("Who is there ?");
        }

        @Override
        public void onClose(final int closeCode, final String message) {
            WebSocketServer.this.disconnect(this);
        }

        @Override
        public void onMessage(final String data) {
            try {
                final MessageInterface message = WebSocketServer.this.messageDecoder.decode(data);
                final Object object = WebSocketServer.this.messageHandler.handle(this, message);
                this.getServer().getEventBus().post(object);
            }
            catch (final Exception e) {
                e.printStackTrace();
                this.say(e);
            }
        }

        @Override
        public WebSocketServer getServer() {
            return this.server;
        }

        @Override
        public Connection getConnection() {
            return this.connection;
        }
    }

    public void connect(final UserInterface user) {
        this.connectedUsersByIO.put(user.getIO(), user);
    }

    public UserInterface getUser(final IOinterface io) {
        return this.connectedUsersByIO.get(io);
    }

    public void disconnect(final IOinterface io) {
        this.connectedUsersByIO.remove(io);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleNewUserEvent(final UserInterface newUserEvent) {
        this.connect(newUserEvent);
        newUserEvent.getIO().say("Welcome " + newUserEvent.getName() + " !");
        //newUserEvent.getIO().say(this.getConnectedUsers());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleJoinRoomEvent(final JoinRoomInterface joinRoomEvent) {
        final Set<UserInterface> connectedRoomUsers = this.connectedUsersByRoom.get(joinRoomEvent.getOrdinal());
        final int n = connectedRoomUsers.size();
        if (n < 4) {
            final UserInterface user = this.getUser(joinRoomEvent.getIO());
            connectedRoomUsers.add(user);
            for (final UserInterface connectedRoomUser : connectedRoomUsers) {
                connectedRoomUser.getIO().say(user.getName() + " has joined room " + joinRoomEvent.getOrdinal());
            }
            if (n == 3) {
                for (final UserInterface connectedRoomUser : connectedRoomUsers) {
                    connectedRoomUser.getIO().say("Let's kick some ass ! :)");
                }
            }
        }
        else {
            joinRoomEvent.getIO().say("Room " + joinRoomEvent.getOrdinal() + " is full :(");
        }
    }
}