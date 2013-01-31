
package transport.messages;

import transport.protocol.MessageInterface;

import com.google.gson.JsonObject;

public final class Client implements MessageInterface {

    private final String name;

    public String getName() {
        return this.name;
    }

    public Client(final String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public JsonObject getData() {
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getName());
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