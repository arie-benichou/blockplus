
package serialization;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import blockplus.model.color.ColorInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * TODO retourner une paire (piece, statut:(un)available)
 */
public final class PiecesBagEncoding {

    private final static class PiecesBagSerializer implements JsonSerializer<PiecesBag> {

        @Override
        public JsonElement serialize(final PiecesBag bag, final Type typeOfSrc, final JsonSerializationContext context) {
            //PiecesBag effectiveBag = bag.remove(Pieces.PIECE0);
            List<Pieces> effectiveBagAsList = bag.asList(); // TODO pouvoir passer un Ordering
            Collections.sort(effectiveBagAsList);
            List<Integer> remaining = Lists.transform(effectiveBagAsList, new Function<Pieces, Integer>() {
                @Override
                public Integer apply(@Nullable Pieces input) {
                    return input.ordinal();
                }
            });
            return context.serialize(remaining);
        }
    }

    private final static Type INTERFACE_TYPE = new TypeToken<PiecesBag>() {}.getType();

    private final static Gson JSON_FORMATTER = new GsonBuilder().registerTypeAdapter(INTERFACE_TYPE, new PiecesBagSerializer()).create();

    public static String encode(final PiecesBag bag) {
        return JSON_FORMATTER.toJson(bag);
    }

    public static void main(final String[] args) throws IOException {
        PiecesBag bag = PiecesBag.from(Pieces.values());
        final String encodedToJson = encode(bag);
        System.out.println(encodedToJson);
    }

    private PiecesBagEncoding() {}

}