
package transport;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import transport.events.ClientInterface;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@SuppressWarnings("serial")
public class WebSocketServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter
    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, ClientInterface> clientByIO = Maps.newConcurrentMap();

    public void updateClients(final IOinterface io, final ClientInterface user) {
        this.clientByIO.put(io, user);
    }

    public void removeFromClients(final IOinterface io) {
        this.clientByIO.remove(io);
    }

    private final Map<Integer, RoomInterface> roomByOrdinal = Maps.newConcurrentMap();

    public RoomInterface getRoom(final Integer ordinal) {
        return this.roomByOrdinal.get(ordinal);
    }

    public void updateRooms(final Integer ordinal, final Room newRoom) {
        this.roomByOrdinal.put(ordinal, newRoom);
    }

    private final Map<Integer, List<ClientInterface>> clientsByRoom = Maps.newConcurrentMap(); // TODO à virer

    public List<ClientInterface> getClientsByRoom(final Integer room) {
        return this.clientsByRoom.get(room);
    }

    public void updateRoom(final Integer room, final ImmutableList<ClientInterface> roomUsers) {
        this.clientsByRoom.put(room, roomUsers);
    }

    public Collection<ClientInterface> getClients() {
        return this.clientByIO.values();
    }

    private final EventBus eventBus = new EventBus();

    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.getEventBus().register(this);

        final BlockplusServerEvents blockplusServerEvents = new BlockplusServerEvents(this); // TODO à injecter
        this.getEventBus().register(blockplusServerEvents);

        for (int i = 1; i <= 10; ++i) {
            final ImmutableList<ClientInterface> empty = ImmutableList.of();
            this.clientsByRoom.put(i, empty);
        }
    }

    @Override
    public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
        return new IO(this);
    }

    public void connect(final ClientInterface user) {
        this.clientByIO.put(user.getIO(), user);
    }

    public void disconnect(final IOinterface io) {
        this.clientByIO.remove(io);
    }

    private void say(final IOinterface io, final String data) {
        io.say(data);
    }

    public MessageInterface decode(final String data) {
        return this.messageDecoder.decode(data);
    }

    public Object handle(final IO io, final MessageInterface message) {
        return this.messageHandler.handle(io, message);
    }

    public ClientInterface getClient(final IOinterface io) {
        return this.clientByIO.get(io);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleNewUserEvent(final ClientInterface newClient) {
        this.connect(newClient);
        this.say(newClient.getIO(), "Welcome " + newClient.getName() + " !");

    }

}