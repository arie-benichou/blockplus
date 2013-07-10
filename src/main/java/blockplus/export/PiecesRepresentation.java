
package blockplus.export;


import blockplus.model.entity.Entity;
import blockplus.model.entity.Polyomino;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import components.cells.Positions.Position;

public final class PiecesRepresentation {

    private final static PiecesRepresentation INSTANCE = new PiecesRepresentation();

    private static String _toJson() {
        final JsonArray pieces = new JsonArray();
        for (final Polyomino polyomino : Polyomino.set()) {
            final JsonArray positions = new JsonArray();
            final Entity piece = polyomino.get();
            for (final Position position : piece.positions()) {
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("y", position.row());
                jsonObject.addProperty("x", position.column());
                positions.add(jsonObject);
            }
            pieces.add(positions);
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