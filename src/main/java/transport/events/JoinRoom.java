
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class JoinRoom implements JoinRoomInterface {

    public static class Builder {

        public static JoinRoom build(final IOinterface io, final JsonObject data) {
            return new JoinRoom(io, data.get("ordinal").getAsInt());
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

    private JoinRoom(final IOinterface io, final Integer ordinal) {
        this.io = io;
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("ordinal", this.getOrdinal()).toString();
    }

}