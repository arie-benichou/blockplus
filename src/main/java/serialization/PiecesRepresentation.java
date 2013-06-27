
package serialization;

import blockplus.piece.PieceType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import components.position.PositionInterface;

public final class PiecesRepresentation {

    private final static PiecesRepresentation INSTANCE = new PiecesRepresentation();

    private static String _toJson() {
        final JsonObject pieces = new JsonObject();
        for (final PieceType piece : PieceType.asSet()) {
            final JsonArray positions = new JsonArray();
            for (final PositionInterface position : piece.positions()) {
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("x", position.column());
                jsonObject.addProperty("y", position.row());
                positions.add(jsonObject);
            }
            pieces.add(String.valueOf(piece.id()), positions);
        }
        pieces.remove("0");
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