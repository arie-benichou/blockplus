
package transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import serialization.CellEncoding;
import transport.events.AutoJoinInterface;
import transport.events.JoinRoomInterface;
import transport.events.UserInterface;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;
import blockplus.model.board.Board;
import blockplus.model.color.ColorInterface;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import components.board.BoardInterface;

@SuppressWarnings("serial")
public class WebSocketServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter

    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, UserInterface> connectedUserByIO = Maps.newConcurrentMap();

    private final Map<Integer, List<UserInterface>> connectedUsersByRoom = Maps.newConcurrentMap();

    //private final Map<Integer, Game> gameByRoom = Maps.newConcurrentMap();

    private final Map<Integer, RoomInterface> roomByOrdinal = Maps.newConcurrentMap();

    public Collection<UserInterface> getConnectedUsers() {
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
            final ImmutableList<UserInterface> empty = ImmutableList.of();
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

    public void connect(final UserInterface user) {
        this.connectedUserByIO.put(user.getIO(), user);
    }

    public UserInterface getUser(final IOinterface io) {
        return this.connectedUserByIO.get(io);
    }

    public void disconnect(final IOinterface io) {
        //io.say("Bye !");
        this.connectedUserByIO.remove(io);
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
        final Integer room = joinRoomEvent.getOrdinal();
        final List<UserInterface> connectedRoomUsers = this.connectedUsersByRoom.get(room);
        final int n = connectedRoomUsers.size();
        if (n < 4) {
            final IOinterface io = joinRoomEvent.getIO();
            final UserInterface user = this.getUser(io);
            final ImmutableList<UserInterface> roomUsers = new ImmutableList.Builder<UserInterface>().addAll(connectedRoomUsers).add(user).build();
            System.out.println(roomUsers);
            this.connectedUsersByRoom.put(room, roomUsers);
            io.setRoom(room);
            for (final UserInterface connectedRoomUser : roomUsers) {
                //connectedRoomUser.getIO().say(user.getName() + " has joined room " + room);
                connectedRoomUser.getIO().say("{\"type\":\"info\",\"data\":" + "\"" + user.getName() + " has joined room " + room + "\"" + "}");
            }
            if (n == 3) {
                for (final UserInterface connectedRoomUser : roomUsers) {
                    connectedRoomUser.getIO().say("Let's kick some ass ! :)");
                }
                final Game game = new Game();
                //this.gameByRoom.put(room, game);
                System.out.println(roomUsers);
                final List<Integer> parts = Lists.newArrayList();
                for (final UserInterface userInterface : roomUsers) {
                    final IOinterface io2 = userInterface.getIO();
                    parts.add(io2.hashCode());
                }
                final String hash1 = Joiner.on(':').join(parts);
                //System.out.println(hash);
                // TODO générer un hascode et le placer dans le local storage
                // reconnecter immédiatement à la room une socket déconnectée dont le hashcode correspond

                final Room newRoom = new Room(room, hash1, roomUsers, game);
                this.roomByOrdinal.put(room, newRoom);

                final GameContext context = game.getInitialContext();
                final Board board = context.getBoard();
                final BoardInterface<ColorInterface> coloredBoard = board.colorize();
                final String json = CellEncoding.encode(coloredBoard);

                int k = 0;
                for (final UserInterface connectedRoomUser : roomUsers) {
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

                    connectedRoomUser.getIO().say(newRoom.getCode());

                    connectedRoomUser.getIO().say("{\"type\":\"room\",\"data\":" + "\"" + hashcode + "\"" + "}");
                    connectedRoomUser.getIO().say("{\"type\":\"board\",\"data\":" + json + "}");

                }

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

        System.out.println("--------------------------------");
        System.out.println(parts);
        System.out.println();
        System.out.println(ordinal);
        System.out.println(colorIndex);
        System.out.println(name);
        System.out.println(code);
        System.out.println();
        System.out.println(room);
        System.out.println();
        System.out.println(room != null);
        System.out.println();
        System.out.println(room.getCode());
        System.out.println(code);
        System.out.println();
        System.out.println(room.getCode().equals(code));
        final UserInterface roomUser2 = room.getUsers().get(colorIndex - 1);
        System.out.println(roomUser2);
        System.out.println(roomUser2.getName().equals(name));
        System.out.println("--------------------------------");

        if (room != null) {
            if (room.getCode().equals(code)) {
                final UserInterface roomUser = room.getUsers().get(colorIndex - 1);
                if (roomUser.getName().equals(name)) {
                    // TODO vérifier le timestamp de la première connexion
                    //if (!list.contains(autoJoinEvent.getIO().hashCode())) {
                    final ArrayList<UserInterface> newUsers = Lists.newArrayList(room.getUsers());
                    final transport.events.User user = new transport.events.User(autoJoinEvent.getIO(), name);
                    newUsers.set(colorIndex - 1, user);
                    this.connectedUsersByRoom.put(ordinal, ImmutableList.copyOf(newUsers));
                    final IOinterface oldIo = room.getUsers().get(colorIndex - 1).getIO();
                    //oldIo.say("auto join...");
                    oldIo.getConnection().close();

                    this.connectedUserByIO.remove(oldIo);
                    this.connectedUserByIO.put(autoJoinEvent.getIO(), user);
                    final Room newRoom = new Room(ordinal, code, newUsers, room.getGame());
                    this.roomByOrdinal.put(ordinal, newRoom);
                    System.out.println(newRoom);
                    user.getIO().say("Welcome back, " + name + " ;)");

                    final GameContext context = room.getGame().getInitialContext();
                    final Board board = context.getBoard();
                    final BoardInterface<ColorInterface> coloredBoard = board.colorize();
                    final String json = CellEncoding.encode(coloredBoard);
                    user.getIO().say("{\"type\":\"board\",\"data\":" + json + "}");

                    //}
                }
            }
        }
    }
}