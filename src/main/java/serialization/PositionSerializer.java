
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

public final class PositionSerializer implements JsonSerializer<Position> {

    @Override
    public JsonElement serialize(Position position, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(position.row()));
        jsonArray.add(new JsonPrimitive(position.column()));
        return jsonArray;
    }

    public static void main(String[] args) {
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(new TypeToken<Position>() {}.getType(), new PositionSerializer());
        Gson gson = gsonBuilder.create();

        PositionInterface position = Position.from(4, 7);
        String json = gson.toJson(position);
        System.out.println(json);

    }

}