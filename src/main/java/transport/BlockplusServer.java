
package transport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.jetty.websocket.WebSocketServlet;

import transport.events.interfaces.ClientInterface;
import transport.events.interfaces.EventInterface;
import transport.events.interfaces.FeedbackInterface;
import transport.events.interfaces.ShowRoomInterface;
import transport.messages.Messages;
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
        /*
        try {
            Thread.sleep(2 * 1000);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        */
        System.out.println(request);
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
        newClient.getIO().emit("welcome", "\"" + newClient.getName() + "\"");
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
    public void onFeedback(final FeedbackInterface feedback) {
        final Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("arie.benichou", "*****")); // TODO
        email.setSSLOnConnect(true);
        try {
            email.setFrom("arie.benichou@gmail.com");
            email.setSubject("[Block+] New feedback from " + feedback.getName() + " !");
            email.setMsg(feedback.getContent());
            email.addTo("arie.benichou@gmail.com");
            email.send();
            feedback.getIO().emit("info", "\"" + "Thank you for your feedback !" + "\"");
        }
        catch (final EmailException e) {
            e.printStackTrace();
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

    // TODO unit tests !
    public static void main(final String[] args) throws Exception {

        final int room = args.length > 0 ? Integer.parseInt(args[0]) : 1;

        final String host = "localhost";
        final int port = 8080;

        final WebSocketClientFactory factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();

        final Messages messages = new Messages();

        final VirtualClient[] virtualClients = new VirtualClient[4];

        //final WebSocketClient client = factory.newWebSocketClient();

        Thread.sleep(1500);

        for (int i = 1; i <= 4; ++i) {

            final WebSocketClient client = factory.newWebSocketClient();
            client.setMaxIdleTime(60000 * 5);
            client.setMaxTextMessageSize(1024 * 64);

            final VirtualClient virtualClient = new VirtualClient("virtual-client-" + i, client, host, port);
            virtualClients[i - 1] = virtualClient;
            virtualClient.start();

            // connection
            final MessageInterface message1 = messages.newClient(virtualClient.getName());
            virtualClient.send(message1);
            ///Thread.sleep(100);

            // join room 1
            final MessageInterface message2 = messages.newRoomConnection(room);
            Thread.sleep(750);
            virtualClient.send(message2);

            //System.out.println("----------------------------------------------------");

        }

        /*
        Thread.sleep(1000 * 60 * 60);

        for (final VirtualClient virtualClient : virtualClients) {
            virtualClient.stop();
        }

        factory.stop();
        */
    }
}