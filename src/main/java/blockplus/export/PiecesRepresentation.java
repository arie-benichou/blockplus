
package blockplus.export;

import blockplus.model.polyomino.Polyomino;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import components.cells.IPosition;

public final class PiecesRepresentation {

    private final static PiecesRepresentation INSTANCE = new PiecesRepresentation();

    private static String _toJson() {
        final JsonArray pieces = new JsonArray();
        for (final Polyomino polyomino : Polyomino.set()) {
            final Iterable<IPosition> positions = polyomino.positions();
            if (positions.iterator().hasNext()) {
                final JsonArray data = new JsonArray();
                for (final IPosition position : positions) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("y", position.row());
                    jsonObject.addProperty("x", position.column());
                    data.add(jsonObject);
                }
                pieces.add(data);
            }
        }
        return pieces.toString();
    }

    private final static String jsonRepresentation = _toJson();

    private PiecesRepresentation() {}

    public static PiecesRepresentation getInstance() {
        return INSTANCE;
    }

    public String toJson() {
        return jsonRepresentation;
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public static void main(final String[] args) {
        System.out.println(PiecesRepresentation.getInstance().toJson());
    }

}