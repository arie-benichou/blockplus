
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class Client implements ClientInterface {

    public static class Builder {

        public static Client build(final IOinterface io, final JsonObject data) {
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
    public Integer getRoom() {
        return this.room;
    }

    private final Integer room;

    public Client(final IOinterface io, final String name, final Integer room) {
        this.io = io;
        this.name = name;
        this.room = room;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("io", this.getIO())
                .add("name", this.getName())
                .add("room", this.getRoom())
                .toString();
    }
}