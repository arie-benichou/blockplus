
package serialization;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import components.position.Position;
import components.position.PositionInterface;

@Deprecated
public final class PositionSerializer implements JsonSerializer<Position> {

    @Override
    public JsonElement serialize(final Position position, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(position.row()));
        jsonArray.add(new JsonPrimitive(position.column()));
        return jsonArray;
    }

    public static void main(final String[] args) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(new TypeToken<Position>() {}.getType(), new PositionSerializer());
        final Gson gson = gsonBuilder.create();

        final PositionInterface position = Position.from(4, 7);
        final String json = gson.toJson(position);
        System.out.println(json);

    }

}