
package transport;

import java.util.ArrayList;

import transport.events.Client;
import transport.events.interfaces.ClientInterface;
import transport.events.interfaces.MoveSubmitInterface;
import transport.events.interfaces.RoomConnectionInterface;
import transport.events.interfaces.RoomReconnectionInterface;
import blockplus.model.game.BlockplusGame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class BlockplusServerEvents {

    private final BlockplusServer server;

    public BlockplusServer getServer() {
        return this.server;
    }

    public BlockplusServerEvents(final BlockplusServer server) {
        this.server = server;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onRoomConnection(final RoomConnectionInterface roomConnection) {
        final RoomInterface<BlockplusGame> room = this.getServer().getRoom(roomConnection.getOrdinal());
        if (room.isFull()) {
            roomConnection.getIO().emit("info", "\"" + "Room " + room.getOrdinal() + " is full" + "\""); // TODO revoir emit
        }
        else {
            final ClientInterface oldClient = this.getServer().getClient(roomConnection.getIO());
            final ClientInterface newClient = new Client(roomConnection.getIO(), oldClient.getName(), room.getOrdinal());
            System.out.println();
            System.out.println(newClient.getName());
            System.out.println(room.getOrdinal());
            System.out.println();
            this.getServer().updateClients(newClient.getIO(), newClient);

            final BlockplusRoom newRoom = (BlockplusRoom) room.connect(newClient);

            final ImmutableList<ClientInterface> clients = newRoom.getClients();
            this.getServer().updateRoom(newRoom.getOrdinal(), clients);
            this.getServer().updateRooms(newRoom.getOrdinal(), newRoom);

            for (final ClientInterface client : clients) {
                client.getIO().emit("info", "\"" + newClient.getName() + " has joined room " + newRoom.getOrdinal() + "\""); // TODO revoir emit
            }
            if (newRoom.isFull()) {
                int k = 0;
                for (final ClientInterface client : newRoom.getClients()) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.add("room", new JsonPrimitive(newRoom.getOrdinal()));
                    jsonObject.add("code", new JsonPrimitive(newRoom.getCode()));
                    jsonObject.add("time", new JsonPrimitive(newRoom.getTimeStamp()));
                    jsonObject.add("name", new JsonPrimitive(client.getName()));
                    jsonObject.add("color", new JsonPrimitive(++k));
                    jsonObject.add("client", new JsonPrimitive(client.hashCode()));
                    //TODO ajouter le timeStamp de connexion et l'ip du client
                    client.getIO().emit("link", jsonObject.toString());
                }
                newRoom.update();
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onMoveSubmit(final MoveSubmitInterface moveSubmit) {
        final ClientInterface client = this.getServer().getClient(moveSubmit.getIO());
        final Integer room = client.getRoom();
        final BlockplusRoom blockplusRoom = (BlockplusRoom) this.getServer().getRoom(room);
        final BlockplusRoom newRoom = (BlockplusRoom) blockplusRoom.play(moveSubmit);
        this.getServer().updateRooms(newRoom.getOrdinal(), newRoom);
        newRoom.update();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onRoomReconnection(final RoomReconnectionInterface RoomReconnection) {

        final JsonObject link = RoomReconnection.getLink();

        final String name = link.get("name").getAsString();
        final Integer ordinal = link.get("room").getAsInt();
        final Integer colorIndex = link.get("color").getAsInt();
        final String code = link.get("code").getAsString();
        final int client = link.get("client").getAsInt();
        final long timeStamp = link.get("time").getAsLong();

        final RoomInterface<BlockplusGame> room = this.getServer().getRoom(ordinal);

        if (room != null) {
            if (room.getCode().equals(code)) {
                final ClientInterface roomUser = room.getClients().get(colorIndex - 1);
                if (roomUser.getName().equals(name)) {
                    if (roomUser.hashCode() == client) {
                        if (room.getTimeStamp() == timeStamp) {
                            final ArrayList<ClientInterface> newUsers = Lists.newArrayList(room.getClients());
                            final transport.events.Client newClient = new transport.events.Client(RoomReconnection.getIO(), name, ordinal);
                            newUsers.set(colorIndex - 1, newClient);
                            final IOinterface oldIo = room.getClients().get(colorIndex - 1).getIO();
                            oldIo.getConnection().close();
                            final BlockplusRoom newRoom =
                                                          new BlockplusRoom(ordinal, code, ImmutableList.copyOf(newUsers), room.getApplication(),
                                                                  room.getTimeStamp());
                            this.getServer().updateRooms(ordinal, newRoom);
                            this.getServer().updateClients(RoomReconnection.getIO(), newClient);
                            this.getServer().removeFromClients(oldIo);

                            link.addProperty("client", newClient.hashCode());
                            newClient.getIO().emit("link", link.toString());

                            newRoom.update(newClient);
                        }
                    }
                }
            }
        }
    }
}