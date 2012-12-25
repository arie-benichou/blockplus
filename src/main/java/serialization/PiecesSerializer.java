
package serialization;

import java.lang.reflect.Type;

import blockplus.model.piece.Pieces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import components.position.Position;
import components.position.PositionInterface;

public final class PiecesSerializer implements JsonSerializer<Pieces> {

    @Override
    public JsonElement serialize(Pieces piece, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(piece.ordinal());
    }

    public static void main(String[] args) {
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(new TypeToken<Pieces>() {}.getType(), new PiecesSerializer());
        Gson gson = gsonBuilder.create();

        Pieces position = Pieces.get(7);
        String json = gson.toJson(position);
        System.out.println(json);

    }

}