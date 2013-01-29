
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

@Deprecated
public final class PiecesSerializer implements JsonSerializer<Pieces> {

    @Override
    public JsonElement serialize(final Pieces piece, final Type typeOfSrc, final JsonSerializationContext context) {
        return new JsonPrimitive(piece.ordinal());
    }

    public static void main(final String[] args) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(new TypeToken<Pieces>() {}.getType(), new PiecesSerializer());
        final Gson gson = gsonBuilder.create();

        final Pieces position = Pieces.get(7);
        final String json = gson.toJson(position);
        System.out.println(json);

    }

}