
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class Submit implements SubmitInterface {

    public static class Builder {

        public static Submit build(final IOinterface io, final JsonObject data) {
            return new Submit(io, data.get("id").getAsInt(), data.get("positions").getAsJsonArray());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final int id;

    @Override
    public Integer getId() {
        return this.id;
    }

    private final JsonArray positions;

    @Override
    public JsonArray getPositions() {
        return this.positions;
    }

    public Submit(final IOinterface io, final int id, final JsonArray positions) {
        this.io = io;
        this.id = id;
        this.positions = positions;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("io", this.getIO())
                .add("id", this.getId())
                .add("positions", this.getPositions())
                .toString();
    }

}