
package serialization;

import static blockplus.model.color.Colors.*;
import static components.position.Position.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import blockplus.model.color.ColorInterface;
import blockplus.model.color.PrimeColors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import components.board.Board;
import components.board.BoardInterface;
import components.board.rendering.StringRendering;
import components.position.PositionInterface;

// TODO utiliser une collection de tuples(Position, ColorInterface) plutôt qu'un
// tableau
public final class CellEncoding {

    /*
    private final static class ColorInstanceCreator implements InstanceCreator<ColorInterface> {

        @Override
        public ColorInterface createInstance(final Type type) {
            return null;
        }
    }
    */

    // TODO ColorEncoding
    private final static class ColorInterfaceSerializer implements JsonSerializer<ColorInterface> {

        @Override
        public JsonElement serialize(final ColorInterface color, final Type typeOfSrc, final JsonSerializationContext context) {
            if (color.is(White)) return new JsonPrimitive("White"); // TODO ! pouvoir définir le nom d'une couleur
            if (color.is(Black)) return new JsonPrimitive("Black"); // TODO ! pouvoir définir le nom d'une couleur
            return new JsonPrimitive(((PrimeColors) color.list().get(0)).name());
        }
    }

    private final static Type COLOR_INTERFACE_TYPE = new TypeToken<ColorInterface>() {}.getType();

    private final static Type NORMALIZED_FORM_TYPE = new TypeToken<ColorInterface[][]>() {}.getType();

    private final static Gson JSON_FORMATTER = new GsonBuilder().registerTypeAdapter(COLOR_INTERFACE_TYPE, new ColorInterfaceSerializer()).create();

    private static ColorInterface[][] getNormalizedForm(final BoardInterface<ColorInterface> board) {
        final int rows = board.rows();
        final int columns = board.columns();
        final ColorInterface[][] data = new ColorInterface[rows][columns];
        for (final ColorInterface[] row : data) {
            Arrays.fill(row, board.initialSymbol());
        }
        for (final Entry<PositionInterface, ColorInterface> entry : board.filter(null).entrySet()) {
            final PositionInterface position = entry.getKey();
            data[position.row()][position.column()] = entry.getValue();
        }
        return data;
    }

    private static String encodeNormalizedForm(final ColorInterface[][] data) {
        return JSON_FORMATTER.toJson(data);
    }

    public static String encode(final BoardInterface<ColorInterface> board) {
        return encodeNormalizedForm(getNormalizedForm(board));
    }

    private static ColorInterface[][] decodeNormalizedForm(final String data) {
        return (ColorInterface[][]) JSON_FORMATTER.fromJson(data, NORMALIZED_FORM_TYPE);
    }

    public static BoardInterface<ColorInterface> decode(final String data) {
        final ColorInterface[][] decodedToNormalizedForm = decodeNormalizedForm(data);
        final int rows = decodedToNormalizedForm.length;
        final int columns = decodedToNormalizedForm[0].length;
        final Map<PositionInterface, ColorInterface> mutations = Maps.newHashMap();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final ColorInterface state = decodedToNormalizedForm[i][j];
                if (state != White) mutations.put(Position(i, j), state);
            }
        }
        return Board.from(rows, columns, White, Black, mutations);
    }

    private static BoardInterface<ColorInterface> createBoard() {
        final BoardInterface<ColorInterface> board = Board.from(3, 3, White, Black);
        final Map<PositionInterface, ColorInterface> mutation = Maps.newHashMap();
        mutation.put(Position(0, 0), Blue);
        mutation.put(Position(1, 1), Red);
        mutation.put(Position(2, 2), Green);
        return board.apply(mutation);
    }

    private static void debug(final BoardInterface<ColorInterface> board) {
        final Map<?, Character> symbols = ImmutableMap.of(Blue, 'B', Red, 'R', Green, 'G', White, 'W');
        final StringRendering view = new StringRendering(symbols);
        System.out.println(view.apply(board));
    }

    public static void main(final String[] args) throws IOException {

        final BoardInterface<ColorInterface> board = createBoard();
        debug(board);

        final String encodedToJson = encode(board);
        System.out.println(encodedToJson);

        /*
        final BoardInterface<ColorInterface> decodedFromJson = decode(encodedToJson);
        debug(decodedFromJson);

        Preconditions.checkState(board.equals(decodedFromJson), "WTF");
        */
    }

    private CellEncoding() {}

}