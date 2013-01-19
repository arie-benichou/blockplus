
package transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import serialization.GameJSONRepresentation;
import transport.events.AutoJoinInterface;
import transport.events.Client;
import transport.events.ClientInterface;
import transport.events.JoinRoomInterface;
import transport.events.SubmitInterface;
import blockplus.model.color.ColorInterface;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInterface;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import components.position.Position;
import components.position.PositionInterface;

public class BlockplusServerEvents {

    private final WebSocketServer server;

    public WebSocketServer getServer() {
        return this.server;
    }

    public BlockplusServerEvents(final WebSocketServer server) {
        this.server = server;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleJoinRoomEvent(final JoinRoomInterface joinRoomEvent) {
        final Integer room = joinRoomEvent.getOrdinal();
        final List<ClientInterface> connectedRoomUsers = this.getServer().getClientsByRoom(room);
        final int n = connectedRoomUsers.size();
        if (n < 4) {
            final IOinterface io = joinRoomEvent.getIO();
            final ClientInterface oldUser = this.getServer().getClient(io);
            final ClientInterface user = new Client(oldUser.getIO(), oldUser.getName(), room);
            this.getServer().updateClients(user.getIO(), user);
            final ImmutableList<ClientInterface> roomUsers = new ImmutableList.Builder<ClientInterface>().addAll(connectedRoomUsers).add(user).build();
            this.getServer().updateRoom(room, roomUsers);
            io.setRoom(room);
            for (final ClientInterface connectedRoomUser : roomUsers) {
                connectedRoomUser.getIO().say("{\"type\":\"info\",\"data\":" + "\"" + user.getName() + " has joined room " + room + "\"" + "}");
            }
            if (n == 3) {
                for (final ClientInterface connectedRoomUser : roomUsers) {
                    connectedRoomUser.getIO().say("Let's kick some ass ! :)");
                }
                final Game game = new Game();
                final List<Integer> parts = Lists.newArrayList();
                for (final ClientInterface userInterface : roomUsers) {
                    final IOinterface io2 = userInterface.getIO();
                    parts.add(io2.hashCode());
                }
                final String hash1 = Joiner.on(':').join(parts);
                final Room newRoom = new Room(room, hash1, roomUsers, game);
                this.getServer().updateRooms(room, newRoom);
                final GameJSONRepresentation gameRepresentation = new GameJSONRepresentation(game);
                int k = 0;
                for (final ClientInterface connectedRoomUser : roomUsers) {
                    final List<String> hashParts = Lists.newArrayList();
                    hashParts.add(connectedRoomUser.getName());
                    hashParts.add("" + room);
                    hashParts.add("" + (++k));
                    hashParts.add(newRoom.getCode());
                    final String hashcode = Joiner.on('.').join(hashParts);
                    connectedRoomUser.getIO().say("{\"type\":\"room\",\"data\":" + "\"" + hashcode + "\"" + "}");
                    connectedRoomUser.getIO().say("{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}");
                }
                final ClientInterface userToPlay = newRoom.getUserToPlay();
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
        final RoomInterface room = this.getServer().getRoom(ordinal);
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
                    this.getServer().updateRooms(ordinal, newRoom);
                    this.getServer().updateClients(autoJoinEvent.getIO(), user);
                    this.getServer().removeFromClients(oldIo);
                    user.getIO().say("Welcome back, " + name + " ;)"); // TODO utiliser un ID pour les IO
                    final Game game = room.getGame();
                    final GameJSONRepresentation gameRepresentation = new GameJSONRepresentation(game);
                    user.getIO().say("{\"type\":\"color\",\"data\":" + gameRepresentation.encodeColor() + "}");
                    user.getIO().say("{\"type\":\"pieces\",\"data\":" + gameRepresentation.encodeBagOfPiece() + "}");
                    user.getIO().say("{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}");
                    final RoomInterface roomInterface = this.getServer().getRoom(ordinal);
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
        final ClientInterface user = this.getServer().getClient(newSubmit.getIO());
        final Integer room = user.getRoom();
        System.out.println(room);
        final RoomInterface roomInterface = this.getServer().getRoom(room);
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
        final ColorInterface color = context.getColor();
        final Move move = new Move(color, piece);
        // TODO ! check if move is legal
        GameContext nextContext = context.apply(move);
        // check next player
        List<Move> nextOptions;
        do {
            nextContext = nextContext.next();
            nextOptions = nextContext.options();
        } while (nextOptions.size() == 1 && nextOptions.iterator().next().isNull() && !nextContext.getColor().equals(color));
        //
        final Game nextGame = new Game(nextContext);
        final Room room2 = new Room(roomInterface.getOrdinal(), roomInterface.getCode(), roomInterface.getUsers(), nextGame); // TODO update(Game);
        this.getServer().updateRooms(roomInterface.getOrdinal(), room2);
        final RoomInterface roomInterface2 = this.getServer().getRoom(roomInterface.getOrdinal());
        final List<ClientInterface> users = roomInterface2.getUsers();
        final GameJSONRepresentation gameRepresentation = new GameJSONRepresentation(roomInterface2.getGame());
        final String messageColor = "{\"type\":\"color\",\"data\":" + gameRepresentation.encodeColor() + "}";
        final String messagePieces = "{\"type\":\"pieces\",\"data\":" + gameRepresentation.encodeBagOfPiece() + "}";
        final String messageBoard = "{\"type\":\"board\",\"data\":" + gameRepresentation.encodeBoard() + "}";
        // check game over
        if (nextContext.getColor().equals(color) && nextOptions.size() == 1 && nextOptions.iterator().next().isNull()) {
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

    @Subscribe
    @AllowConcurrentEvents
    public void handleDeadEvent(final DeadEvent deadEvent) {
        System.out.println(deadEvent.getEvent());
    }

}