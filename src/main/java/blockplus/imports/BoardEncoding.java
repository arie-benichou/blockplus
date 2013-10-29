
package blockplus.imports;

import static components.cells.Positions.Position;

import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import blockplus.model.Board;
import blockplus.model.Colors;
import blockplus.model.polyomino.Polyomino;
import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;
import blockplus.model.polyomino.Polyominos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import components.cells.IPosition;

public class BoardEncoding {

    public BoardEncoding() {}

    public Board decode(final JsonObject data) {
        final JsonObject dimension = data.get("dimension").getAsJsonObject();
        final int rows = dimension.get("rows").getAsInt();
        final int columns = dimension.get("columns").getAsInt();
        Board board = Board.of(rows, columns);
        final JsonObject cells = data.get("cells").getAsJsonObject();
        board = this.buildBoard(board, cells);
        return board;
    }

    private Board buildBoard(Board board, final JsonObject cells) {
        final Polyominos polyominos = Polyominos.getInstance();
        final PolyominoInstance unit = Polyomino._1.get().iterator().next();
        final Set<Entry<String, JsonElement>> entrySet = cells.entrySet();
        for (final Entry<String, JsonElement> entry : entrySet) {
            final String color = entry.getKey();
            for (final JsonElement jsonElement : entry.getValue().getAsJsonArray()) {
                final int id = jsonElement.getAsInt();
                final IPosition position = Position(id / 20, id % 20); // TODO !!!
                final SortedSet<IPosition> positions = unit.apply(position);
                final PolyominoTranslatedInstance instance = polyominos.get(positions);
                board = board.apply(Colors.valueOf(color), instance);
            }
        }
        return board;
    }

}