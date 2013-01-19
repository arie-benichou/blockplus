
package serialization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import blockplus.model.color.ColorInterface;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

public final class GameJSONRepresentation {

    private final Game game;

    public Game getGame() {
        return this.game;
    }

    public GameJSONRepresentation(final Game game) {
        this.game = game;
    }

    public String encodeColor() {
        final ColorInterface color = this.getGame().getInitialContext().getColor();
        return JSONSerializer.getInstance().toJson(color);
    }

    public String encodeBoard() {
        return CellEncoding.encode(this.getGame().getInitialContext().getBoard().colorize());
    }

    public String encodeBagOfPiece() {
        final GameContext context = this.getGame().getInitialContext();
        final PiecesBag bag = context.getPlayers().get(context.getColor()).getPieces();
        final PiecesBag effectiveBag = bag.remove(Pieces.PIECE0);
        return PiecesBagEncoding.encode(effectiveBag);
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

    /*
    @Override
    public String toString() {
        final String json = this.encodeBoard();
        return json;
    }
    */

    public static void main(final String[] args) {
        final Game game = new Game();
        final String string = new GameJSONRepresentation(game).encodeColor();
        System.out.println(string);
    }

}