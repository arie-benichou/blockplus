
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class AutoJoin implements AutoJoinInterface {

    public static class Builder {

        public static AutoJoin build(final IOinterface io, final JsonObject data) {
            System.out.println(data.get("hashCode").getAsString());
            return new AutoJoin(io, data.get("hashCode").getAsString());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final String hashCode;

    @Override
    public String getHashCode() {
        return this.hashCode;
    }

    private AutoJoin(final IOinterface io, final String hashCode) {
        this.io = io;
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("hashCode", this.getHashCode()).toString();
    }

}