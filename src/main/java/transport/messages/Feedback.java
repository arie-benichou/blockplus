
package transport.messages;

import transport.protocol.MessageInterface;

import com.google.gson.JsonObject;

public final class Feedback implements MessageInterface {

    private final String content;

    public String getContent() {
        return this.content;
    }

    public Feedback(final String content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public JsonObject getData() {
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getContent());
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