
package serialization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.color.ColorInterface;
import blockplus.model.game.BlockplusGame;
import blockplus.model.game.BlockplusGameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import components.position.PositionInterface;

public final class GameJSONRepresentation {

    private final BlockplusGame game;

    public BlockplusGame getGame() {
        return this.game;
    }

    public GameJSONRepresentation(final BlockplusGame game) {
        this.game = game;
    }

    public String encodeColor(final ColorInterface color) {
        return JSONSerializer.getInstance().toJson(color);
    }

    public String encodeColor() {
        return this.encodeColor(this.getGame().getInitialContext().getColor());
    }

    public String _encodeBoard() {
        return CellEncoding.encode(this.getGame().getInitialContext().getBoard().colorize());
    }

    public JsonElement encodeBoard() {
        final JsonObject boardState = new JsonObject();
        final JsonObject meta = new JsonObject();
        final JsonObject data = new JsonObject();
        final Board board = this.getGame().getInitialContext().getBoard();
        final int rows = board.rows();
        final int columns = board.columns();
        meta.addProperty("rows", rows);
        meta.addProperty("columns", columns);
        final Set<ColorInterface> colors = board.getColors();
        for (final ColorInterface color : colors) {
            final JsonArray jsonArray = new JsonArray();
            final BoardLayer layer = board.getLayer(color);
            final Set<PositionInterface> positions = layer.getSelves().keySet();
            for (final PositionInterface position : positions)
                jsonArray.add(new JsonPrimitive(columns * position.row() + (position.column() % rows)));
            data.add(color.toString(), jsonArray);
        }
        boardState.add("dimension", meta);
        boardState.add("cells", data);
        return boardState;
    }

    public String encodeBagOfPiece(final PiecesBag pieces) {
        final PiecesBag effectiveBag = pieces.remove(Pieces.PIECE0); // TODO à revoir
        return PiecesBagEncoding.encode(effectiveBag);
    }

    public String encodeBagOfPiece() {
        final BlockplusGameContext context = this.getGame().getInitialContext();
        final PiecesBag pieces = context.getPlayers().get(context.getColor()).getPieces();
        return PiecesBagEncoding.encode(pieces);
    }

    public String encodeOptions() {
        final List<Move> options = this.getGame().getInitialContext().options();
        final Map<Pieces, List<Set<PositionInterface>>> legalPositionsByPiece = Maps.newTreeMap();
        for (final Move move : options) {
            if (!move.isNull()) { // TODO à revoir
                final PieceInterface piece = move.getPiece();
                final Pieces key = Pieces.get(piece.getId());
                List<Set<PositionInterface>> playablePositions = legalPositionsByPiece.get(key);
                if (playablePositions == null) {
                    playablePositions = Lists.newArrayList();
                    legalPositionsByPiece.put(key, playablePositions);
                }
                playablePositions.add(piece.getSelfPositions());
            }
        }
        return JSONSerializer.getInstance().toJson(legalPositionsByPiece);
    }

    // TODO à faire cote js
    public String encodePotentialPositions() {
        final List<Move> options = this.getGame().getInitialContext().options();
        final Set<PositionInterface> potentialPositions = Sets.newHashSet();
        for (final Move move : options) {
            potentialPositions.addAll(move.getPiece().getSelfPositions());
        }
        return JSONSerializer.getInstance().toJson(potentialPositions);
    }

    public static void main(final String[] args) {
        final BlockplusGame game = new BlockplusGame();
        final JsonElement jsonElement = new GameJSONRepresentation(game).encodeBoard();
        System.out.println(jsonElement);
        System.out.println(new GameJSONRepresentation(game)._encodeBoard());
    }
}