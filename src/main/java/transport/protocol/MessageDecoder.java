
package transport.protocol;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class MessageDecoder implements MessageDecoderInterface {

    private static class MessageDeserializer implements JsonDeserializer<MessageInterface> {

        @Override
        public MessageInterface deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement typeElement = jsonObject.get("type"); // TODO extract constant
            final String type = typeElement.getAsString();
            final JsonElement dataElement = jsonObject.get("data"); // TODO extract constant
            final JsonObject data = dataElement.getAsJsonObject();
            return new Message(type, data);
        }

    }

    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<MessageInterface>() {}.getType(), new MessageDeserializer())
            .create();

    @Override
    public MessageInterface decode(final String data) {
        return GSON_INSTANCE.fromJson(data, new TypeToken<MessageInterface>() {}.getType());
    }

}