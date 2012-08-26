
package serialization;

import static blockplus.model.board.State.*;
import static components.position.Position.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import blockplus.model.board.State;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import components.board.Board;
import components.board.BoardInterface;
import components.board.rendering.StringRendering;
import components.position.PositionInterface;

// TODO utiliser une collection de tuples(Position, State) plutôt qu'un tableau
public final class CellEncodingDemo {

    private final static Type NORMALIZED_FORM_TYPE = new TypeToken<State[][]>() {}.getType();

    private final static Gson JSON_FORMATTER = new Gson();

    private static State[][] getNormalizedForm(final BoardInterface<State> board) {
        final int rows = board.rows();
        final int columns = board.columns();
        final State[][] data = new State[rows][columns];
        for (final State[] row : data) {
            Arrays.fill(row, None);
        }
        for (final Entry<PositionInterface, State> entry : board.filter(null).entrySet()) {
            final PositionInterface position = entry.getKey();
            data[position.row()][position.column()] = entry.getValue();
        }
        return data;
    }

    private static String encodeNormalizedForm(final State[][] data) {
        return JSON_FORMATTER.toJson(data);
    }

    public static String encode(final BoardInterface<State> board) {
        return encodeNormalizedForm(getNormalizedForm(board));
    }

    private static State[][] decodeNormalizedForm(final String data) {
        return (State[][]) JSON_FORMATTER.fromJson(data, NORMALIZED_FORM_TYPE);
    }

    public static BoardInterface<State> decode(final String data) {
        final State[][] decodedToNormalizedForm = decodeNormalizedForm(data);
        final int rows = decodedToNormalizedForm.length;
        final int columns = decodedToNormalizedForm[0].length;
        final Map<PositionInterface, State> mutations = Maps.newHashMap();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final State state = decodedToNormalizedForm[i][j];
                if (state != None) mutations.put(Position(i, j), state);
            }
        }
        return Board.from(rows, columns, None, Other, mutations);
    }

    private static BoardInterface<State> createBoard() {
        final BoardInterface<State> board = Board.from(3, 3, None, Other);
        final Map<PositionInterface, State> mutation = Maps.newHashMap();
        mutation.put(Position(0, 0), Shadow);
        mutation.put(Position(1, 1), Self);
        mutation.put(Position(2, 2), Other);
        return board.apply(mutation);
    }

    private static void debug(final BoardInterface<State> board) {
        final Map<?, Character> symbols = ImmutableMap.of(Self, 'O', Other, 'X', None, ' ', Light, '.', Shadow, '#');
        final StringRendering view = new StringRendering(symbols);
        System.out.println(view.apply(board));
    }

    public static void main(final String[] args) throws IOException {

        final BoardInterface<State> board = createBoard();
        debug(board);

        final String encodedToJson = encode(board);
        System.out.println(encodedToJson);

        final BoardInterface<State> decodedFromJson = decode(encodedToJson);
        debug(decodedFromJson);

        Preconditions.checkState(board.equals(decodedFromJson), "WTF");
    }

    private CellEncodingDemo() {}

}