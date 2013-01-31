
package transport.events;

import transport.IOinterface;
import transport.events.interfaces.ClientInterface;

import com.google.gson.JsonObject;

// TODO event = {source:IO, message:JSON}
public final class Client implements ClientInterface {

    public static class Builder {

        public static Client build(final IOinterface io, final JsonObject data) {
            // TODO à revoir
            return new Client(io, data.get("name").getAsString(), data.has("room") ? data.get("room").getAsInt() : 0);
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

    @Override
    public Integer getRoom() { // TODO à revoir
        return this.room;
    }

    // TODO à revoir
    private final Integer room;

    public Client(final IOinterface io, final String name, final Integer room) {
        this.io = io;
        this.name = name;
        this.room = room;
    }

    @Override
    public String toString() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getClass().getSimpleName());
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getName());
        jsonObject.add("data", data);
        return jsonObject.toString();
    }
}