
package transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import serialization.GameJSONRepresentation;
import transport.events.AutoJoinInterface;
import transport.events.Client;
import transport.events.ClientInterface;
import transport.events.JoinRoomInterface;
import transport.events.SubmitInterface;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;
import blockplus.model.color.ColorInterface;
import blockplus.model.color.PrimeColors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInterface;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import components.position.Position;
import components.position.PositionInterface;

@SuppressWarnings("serial")
public class WebSocketServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter

    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, ClientInterface> connectedUserByIO = Maps.newConcurrentMap();

    private final Map<Integer, List<ClientInterface>> connectedUsersByRoom = Maps.newConcurrentMap(); // TODO à virer

    private final Map<Integer, RoomInterface> roomByOrdinal = Maps.newConcurrentMap();

    public Collection<ClientInterface> getConnectedUsers() {
        return this.connectedUserByIO.values();
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
            final ImmutableList<ClientInterface> empty = ImmutableList.of();
            this.connectedUsersByRoom.put(i, empty);
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

        private Integer room = 0;

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
                System.out.println(message);
                final Object object = WebSocketServer.this.messageHandler.handle(this, message);
                System.out.println(object);
                System.out.println("---------------------------------------------------------");
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

        @Override
        public Integer getRoom() {
            return this.room;
        }

        @Override
        public void setRoom(final Integer ordinal) {
            this.room = ordinal;
        }
    }

    public void connect(final ClientInterface user) {
        this.connectedUserByIO.put(user.getIO(), user);
    }

    public ClientInterface getUser(final IOinterface io) {
        return this.connectedUserByIO.get(io);
    }

    public void disconnect(final IOinterface io) {
        //io.say("Bye !");
        this.connectedUserByIO.remove(io);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleDeadEvent(final DeadEvent deadEvent) {
        System.out.println(deadEvent.getEvent());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleNewUserEvent(final ClientInterface newUserEvent) {
        this.connect(newUserEvent);
        newUserEvent.getIO().say("Welcome " + newUserEvent.getName() + " !");
        //newUserEvent.getIO().say(this.getConnectedUsers());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleJoinRoomEvent(final JoinRoomInterface joinRoomEvent) {
        final Integer room = joinRoomEvent.getOrdinal();
        final List<ClientInterface> connectedRoomUsers = this.connectedUsersByRoom.get(room);
        final int n = connectedRoomUsers.size();
        if (n < 4) {
            final IOinterface io = joinRoomEvent.getIO();
            final ClientInterface oldUser = this.getUser(io);
            final ClientInterface user = new Client(oldUser.getIO(), oldUser.getName(), room);
            this.connectedUserByIO.put(user.getIO(), user);
            final ImmutableList<ClientInterface> roomUsers = new ImmutableList.Builder<ClientInterface>().addAll(connectedRoomUsers).add(user).build();
            System.out.println(roomUsers);
            this.connectedUsersByRoom.put(room, roomUsers);
            io.setRoom(room);
            for (final ClientInterface connectedRoomUser : roomUsers) {
                //connectedRoomUser.getIO().say(user.getName() + " has joined room " + room);
                connectedRoomUser.getIO().say("{\"type\":\"info\",\"data\":" + "\"" + user.getName() + " has joined room " + room + "\"" + "}");
            }
            if (n == 3) {
                for (final ClientInterface connectedRoomUser : roomUsers) {
                    connectedRoomUser.getIO().say("Let's kick some ass ! :)");
                }
                final Game game = new Game();
                //this.gameByRoom.put(room, game);
                System.out.println(roomUsers);
                final List<Integer> parts = Lists.newArrayList();
                for (final ClientInterface userInterface : roomUsers) {
                    final IOinterface io2 = userInterface.getIO();
                    parts.add(io2.hashCode());
                }
                final String hash1 = Joiner.on(':').join(parts);
                //System.out.println(hash);
                // TODO générer un hascode et le placer dans le local storage
                // reconnecter immédiatement à la room une socket déconnectée dont le hashcode correspond

                final Room newRoom = new Room(room, hash1, roomUsers, game);
                this.roomByOrdinal.put(room, newRoom);

                final GameJSONRepresentation gameRepresentation = new GameJSONRepresentation(game);

                int k = 0;
                for (final ClientInterface connectedRoomUser : roomUsers) {
                    //connectedRoomUser.getIO().say(json);
                    //connectedRoomUser.getIO().say(list);
                    //connectedRoomUser.getIO().say(this.connectedUsersByIO);
                    final List<String> hashParts = Lists.newArrayList();
                    hashParts.add(connectedRoomUser.getName());
                    hashParts.add("" + room);
                    hashParts.add("" + (++k));
                    //hashParts.add("" + connectedRoomUser.getIO().hashCode());
                    hashParts.add(newRoom.getCode());
                    final String hashcode = Joiner.on('.').join(hashParts);

                    //connectedRoomUser.getIO().say(newRoom.getCode());

                    connectedRoomUser.getIO().say("{\"type\":\"room\",\"data\":" + "\"" + hashcode + "\"" + "}");
                    //connectedRoomUser.getIO().say("{\"type\":\"color\",\"data\":" + "\"" + gameRepresentation.encodeColor() + "\"" + "}");
                    connectedRoomUser.getIO().say("{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}");
                }

                final ClientInterface userToPlay = newRoom.getUserToPlay();
                //System.out.println(userToPlay);

                userToPlay.getIO().say("{\"type\":\"color\",\"data\":" + gameRepresentation.encodeColor() + "}");
                userToPlay.getIO().say("{\"type\":\"pieces\",\"data\":" + gameRepresentation.encodeBagOfPiece() + "}");
                userToPlay.getIO().say("{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}");
                userToPlay.getIO().say("{\"type\":\"options\",\"data\":" + gameRepresentation.encodeOptions() + "}");
                userToPlay.getIO().say("{\"type\":\"potential\",\"data\":" + gameRepresentation.encodePotentialPositions() + "}");

            }
        }
        else {
            joinRoomEvent.getIO().say("Room " + room + " is full :(");
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleAutoJoinEvent(final AutoJoinInterface autoJoinEvent) {
        final String hashCode = autoJoinEvent.getHashCode();
        final List<String> parts = Lists.newArrayList(Splitter.on('.').split(hashCode));
        final String name = parts.get(0);
        final Integer ordinal = Integer.parseInt(parts.get(1));
        final Integer colorIndex = Integer.parseInt(parts.get(2));
        final String code = parts.get(3);
        final RoomInterface room = this.roomByOrdinal.get(ordinal);
        if (room != null) {
            if (room.getCode().equals(code)) {
                final ClientInterface roomUser = room.getUsers().get(colorIndex - 1);
                if (roomUser.getName().equals(name)) {
                    //if (!list.contains(autoJoinEvent.getIO().hashCode())) {
                    final ArrayList<ClientInterface> newUsers = Lists.newArrayList(room.getUsers());
                    final transport.events.Client user = new transport.events.Client(autoJoinEvent.getIO(), name, ordinal);
                    newUsers.set(colorIndex - 1, user);
                    final IOinterface oldIo = room.getUsers().get(colorIndex - 1).getIO();
                    oldIo.getConnection().close();
                    final Room newRoom = new Room(ordinal, code, newUsers, room.getGame());
                    this.roomByOrdinal.put(ordinal, newRoom);
                    this.connectedUserByIO.put(autoJoinEvent.getIO(), user);
                    this.connectedUserByIO.remove(oldIo);
                    user.getIO().say("Welcome back, " + name + " ;)");
                    // TODO utiliser un ID pour les IO
                    //
                    final Game game = room.getGame();
                    final GameJSONRepresentation gameRepresentation = new GameJSONRepresentation(game);
                    user.getIO().say("{\"type\":\"color\",\"data\":" + gameRepresentation.encodeColor() + "}");
                    user.getIO().say("{\"type\":\"pieces\",\"data\":" + gameRepresentation.encodeBagOfPiece() + "}");
                    user.getIO().say("{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}");

                    final RoomInterface roomInterface = this.roomByOrdinal.get(ordinal);
                    if (roomInterface.getUserToPlay().equals(user)) {
                        user.getIO().say("{\"type\":\"options\",\"data\":" + gameRepresentation.encodeOptions() + "}");
                        user.getIO().say("{\"type\":\"potential\",\"data\":" + gameRepresentation.encodePotentialPositions() + "}");
                    }
                    //}
                }
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleSubmit(final SubmitInterface newSubmit) {
        final ClientInterface user = this.connectedUserByIO.get(newSubmit.getIO());
        final Integer room = user.getRoom();
        System.out.println(room);
        final RoomInterface roomInterface = this.roomByOrdinal.get(room);
        final Game game = roomInterface.getGame();
        final GameContext context = game.getInitialContext();
        // TODO checks...
        final int id = newSubmit.getId();
        final JsonArray array = newSubmit.getPositions();
        final Set<PositionInterface> positions = Sets.newLinkedHashSet();
        for (final JsonElement jsonElement : array) {
            final JsonArray asJsonArray = jsonElement.getAsJsonArray();
            final int row = asJsonArray.get(0).getAsInt();
            final int column = asJsonArray.get(1).getAsInt();
            positions.add(Position.from(row, column));
        }
        PieceInterface piece;
        if (positions.isEmpty()) { // TODO !!! à revoir
            piece = PieceComposite.from(id, Position.from(), positions);
        }
        else {
            piece = PieceComposite.from(id, positions.iterator().next(), positions);
        }
        final Move move = new Move(context.getColor(), piece);
        // TODO ! check if move is legal

        GameContext nextContext = context.apply(move);
        final Game nextGame = new Game(nextContext);

        // speed-up
        //nextContext = nextGame.start(4 * 4);
        //nextGame = new Game(nextContext);

        // check next player
        final ColorInterface color0 = nextContext.getColor();
        List<Move> nextOptions;
        do {
            nextContext = nextContext.next();
            nextOptions = nextContext.options();
        } while (nextOptions.size() == 1 && nextOptions.iterator().next().isNull() && !nextContext.getColor().equals(color0));

        //
        final Room room2 = new Room(roomInterface.getOrdinal(), roomInterface.getCode(), roomInterface.getUsers(), nextGame); // TODO update(Game);
        this.roomByOrdinal.put(roomInterface.getOrdinal(), room2);
        final RoomInterface roomInterface2 = this.roomByOrdinal.get(roomInterface.getOrdinal());
        final List<ClientInterface> users = roomInterface2.getUsers();
        final GameJSONRepresentation gameRepresentation = new GameJSONRepresentation(roomInterface2.getGame());
        final String messageColor = "{\"type\":\"color\",\"data\":" + gameRepresentation.encodeColor() + "}";
        final String messagePieces = "{\"type\":\"pieces\",\"data\":" + gameRepresentation.encodeBagOfPiece() + "}";
        final String messageBoard = "{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}";

        // check game over
        if (nextContext.getColor().equals(color0) && nextOptions.size() == 1 && nextOptions.iterator().next().isNull()) {
            final String messageEnd = "{\"type\":\"end\",\"data\":" + "game-over" + "}";
            for (final ClientInterface client : users) {
                client.getIO().say(messageColor);
                client.getIO().say(messagePieces);
                client.getIO().say(messageBoard);
                client.getIO().say(messageBoard);
                client.getIO().say(messageEnd);
            }
        }
        else {
            for (final ClientInterface client : users) {
                client.getIO().say(messageColor);
                client.getIO().say(messagePieces);
                client.getIO().say(messageBoard);
            }
            roomInterface2.getUserToPlay().getIO().say("{\"type\":\"options\",\"data\":" + gameRepresentation.encodeOptions() + "}");
            roomInterface2.getUserToPlay().getIO().say("{\"type\":\"potential\",\"data\":" + gameRepresentation.encodePotentialPositions() + "}");
        }
    }

    public static void main(final String[] args) {
        final Game game = new Game();
        final ColorInterface color = game.getInitialContext().getColor();
        System.out.println(PrimeColors.get(color.toString()));
    }

}