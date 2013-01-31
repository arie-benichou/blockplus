
package transport.messages;

import transport.protocol.MessageInterface;

import com.google.gson.JsonObject;

public final class RoomConnection implements MessageInterface {

    private final int ordinal;

    public int getOrdinal() {
        return this.ordinal;
    }

    public RoomConnection(final int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public JsonObject getData() {
        final JsonObject data = new JsonObject();
        data.addProperty("ordinal", this.getOrdinal());
        return data;
    }

    @Override
    public String toString() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getType());
        jsonObject.add("data", this.getData());
        return jsonObject.toString();
    }

}