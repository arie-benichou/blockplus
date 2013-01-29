
package serialization;

import blockplus.model.piece.Pieces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import components.position.Position;
import components.position.PositionInterface;

@Deprecated
public class JSONSerializer {

    private static final Gson INSTANCE = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<PositionInterface>() {}.getType(), new PositionSerializer())
            .registerTypeAdapter(new TypeToken<Position>() {}.getType(), new PositionSerializer())
            .registerTypeAdapter(new TypeToken<Pieces>() {}.getType(), new PiecesSerializer())
            .create();

    private JSONSerializer() {}

    public static Gson getInstance() {
        return INSTANCE;
    }

}