
package transport.events;

import transport.IOinterface;
import transport.events.interfaces.FeedbackInterface;

import com.google.gson.JsonObject;

// TODO event = {source:IO, message:JSON}
public final class Feedback implements FeedbackInterface {

    public static class Builder {

        public static Feedback build(final IOinterface io, final JsonObject data) {
            return new Feedback(io, data.get("name").getAsString(), data.get("content").getAsString());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    private final String content;

    @Override
    public String getContent() {
        return this.content;
    }

    public Feedback(final IOinterface io, final String name, final String content) {
        this.io = io;
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getClass().getSimpleName());
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getName());
        data.addProperty("content", this.getContent());
        jsonObject.add("data", data);
        return jsonObject.toString();
    }
}