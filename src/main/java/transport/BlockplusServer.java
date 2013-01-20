
package transport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import transport.events.interfaces.ClientInterface;
import transport.events.interfaces.EventInterface;
import transport.events.interfaces.ShowRoomInterface;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;
import blockplus.model.game.BlockplusGame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class BlockplusServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter
    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, ClientInterface> clientByIO = Maps.newConcurrentMap();

    public void updateClients(final IOinterface io, final ClientInterface user) {
        this.clientByIO.put(io, user);
    }

    public void removeFromClients(final IOinterface io) {
        this.clientByIO.remove(io);
    }

    private final Map<Integer, RoomInterface<BlockplusGame>> roomByOrdinal = Maps.newConcurrentMap();

    public RoomInterface<BlockplusGame> getRoom(final Integer ordinal) {
        return this.roomByOrdinal.get(ordinal);
    }

    public void updateRooms(final Integer ordinal, final RoomInterface<BlockplusGame> newRoom) {
        this.roomByOrdinal.put(ordinal, newRoom);

        // TODO asynch
        // TODO notifier uniquement les clients dans le patio
        // TODO prendre en compte la pagination de rooms
        if (newRoom.isFull()) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room", ordinal);
            jsonObject.addProperty("board", newRoom.toJson());
            for (final IOinterface io : this.clientByIO.keySet()) {
                io.emit("room", jsonObject.toString());
            }
        }

    }

    private final Map<Integer, List<ClientInterface>> clientsByRoom = Maps.newConcurrentMap(); // TODO à virer

    //TODO add patio

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

        for (int i = 1; i <= 12; ++i) {
            final ImmutableList<ClientInterface> empty = ImmutableList.of();
            this.clientsByRoom.put(i, empty);
            this.roomByOrdinal.put(i, new BlockplusRoom(i, "", empty, null, 0));
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

    public MessageInterface decode(final String data) {
        return this.messageDecoder.decode(data);
    }

    public Object handle(final IO io, final MessageInterface message) {
        return this.messageHandler.handle(io, message);
    }

    public ClientInterface getClient(final IOinterface io) {
        return this.clientByIO.get(io);
    }

    private List<Integer> getRooms() {
        final Set<Integer> keySet = this.roomByOrdinal.keySet();
        final ArrayList<Integer> rooms = Lists.newArrayList(keySet);
        Collections.sort(rooms);
        return rooms;
    }

    @Subscribe
    @AllowConcurrentEvents
    // TODO à revoir
    public void onNewClient(final ClientInterface newClient) {
        this.connect(newClient);
        newClient.getIO().emit("info", "\"" + "Welcome " + newClient.getName() + " !" + "\"");
        final String rooms = new Gson().toJson(this.getRooms());
        newClient.getIO().emit("rooms", "\"" + rooms + "\"");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onShowRoom(final ShowRoomInterface showRoom) {

        final IOinterface io = showRoom.getIO();
        final Integer ordinal = showRoom.getOrdinal();
        final RoomInterface<BlockplusGame> room = this.roomByOrdinal.get(ordinal);

        // TODO refactoring with updateRooms
        if (room.isFull()) { // TODO à revoir
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room", ordinal);
            jsonObject.addProperty("board", room.toJson());
            io.emit("room", jsonObject.toString());
        }

    }

    @Subscribe
    @AllowConcurrentEvents
    public void onDeadEvent(final DeadEvent deadEvent) {
        final Object event = deadEvent.getEvent();
        if (event instanceof EventInterface) {
            final EventInterface eventInterface = (EventInterface) event;
            eventInterface.getIO().emit("error", "\"" + eventInterface + "\"");
        }
        else System.out.println(deadEvent.getEvent());
    }

}