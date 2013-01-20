
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class RoomConnection implements RoomConnectionInterface {

    public static class Builder {

        public static RoomConnection build(final IOinterface io, final JsonObject data) {
            return new RoomConnection(io, data.get("ordinal").getAsInt());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final Integer ordinal;

    @Override
    public Integer getOrdinal() {
        return this.ordinal;
    }

    private RoomConnection(final IOinterface io, final Integer ordinal) {
        this.io = io;
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("ordinal", this.getOrdinal()).toString();
    }

}