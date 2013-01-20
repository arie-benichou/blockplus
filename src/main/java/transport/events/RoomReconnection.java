
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class RoomReconnection implements RoomReconnectionInterface {

    public static class Builder {

        public static RoomReconnection build(final IOinterface io, final JsonObject data) {
            return new RoomReconnection(io, data.get("link").getAsJsonObject());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final JsonObject link;

    @Override
    public JsonObject getLink() {
        return this.link;
    }

    private RoomReconnection(final IOinterface io, final JsonObject link) {
        this.io = io;
        this.link = link;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("link", this.getLink())
                .toString();
    }

}