
package transport.messages;

import transport.protocol.MessageInterface;

import com.google.gson.JsonArray;

public class Messages {

    public MessageInterface newClient(final String name) {
        return new Client(name);
    }

    public MessageInterface newRoomConnection(final int ordinal) {
        return new RoomConnection(ordinal);
    }

    public MessageInterface newMoveSubmit(final int id, final JsonArray positions) {
        return new MoveSubmit(id, positions);
    }

}