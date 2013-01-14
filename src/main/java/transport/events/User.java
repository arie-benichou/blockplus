
package transport.events;

import transport.IOinterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class User implements UserInterface {

    public static class Builder {

        public static User build(final IOinterface io, final JsonObject data) {
            return new User(io, data.get("name").getAsString());
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

    public User(final IOinterface io, final String name) {
        this.io = io;
        this.name = name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name", this.getName()).toString();
    }

}