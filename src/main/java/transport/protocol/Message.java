
package transport.protocol;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public class Message implements MessageInterface {

    private final String type;
    private final JsonObject data;

    public Message(final String type, final JsonObject data) {
        this.type = type;
        this.data = data;

    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public JsonObject getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", this.getType())
                .add("data", this.getData())
                .toString();
    }

}