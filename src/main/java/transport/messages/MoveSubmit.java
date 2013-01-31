
package transport.messages;

import transport.protocol.MessageInterface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class MoveSubmit implements MessageInterface {

    private final int id;

    public int getId() {
        return this.id;
    }

    private final JsonArray positions;

    public JsonArray getPositions() {
        return this.positions;
    }

    public MoveSubmit(final int id, final JsonArray positions) {
        this.id = id;
        this.positions = positions;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public JsonObject getData() {
        final JsonObject data = new JsonObject();
        data.addProperty("id", this.getId());
        data.add("positions", this.getPositions());
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