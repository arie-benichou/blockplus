/*
 * Copyright 2012-2013 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package serialization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import blockplus.board.Board;
import blockplus.board.BoardLayer;
import blockplus.color.ColorInterface;
import blockplus.game.BlockplusGame;
import blockplus.game.BlockplusGameContext;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.PlayerInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
                jsonArray.add(new JsonPrimitive(columns * position.row() + (position.column() % rows))); // TODO extract method
            data.add(color.toString(), jsonArray);
        }
        boardState.add("dimension", meta);
        boardState.add("cells", data);
        return boardState;
    }

    // TODO ordering in piecesBag
    // TODO array of 0/1
    // TODO enlever la pièce nulle ?
    public JsonElement encodePieces() {
        final JsonObject data = new JsonObject();
        final BlockplusGameContext context = this.getGame().getInitialContext();
        final List<PlayerInterface> players = context.getPlayers().getAllPlayers();
        for (final PlayerInterface player : players) {
            final ColorInterface color = player.getColor();
            final JsonArray jsonArray = new JsonArray();
            final PiecesBag pieces = player.getPieces();
            for (final Pieces piece : pieces) {
                jsonArray.add(new JsonPrimitive(piece.ordinal()));
            }
            data.add(color.toString(), jsonArray);
        }
        return data;
    }

    public JsonElement encodeOptions() {
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
        return JSONSerializer.getInstance().toJsonTree(legalPositionsByPiece);
    }

    @Override
    public String toString() {
        final JsonObject data = new JsonObject();
        data.addProperty("color", this.getGame().getInitialContext().getColor().toString());
        data.addProperty("isTerminal", !this.getGame().getInitialContext().hasNext());
        data.add("board", this.encodeBoard());
        data.add("pieces", this.encodePieces());
        data.add("options", this.encodeOptions());
        return data.toString();
    }

    public static void main(final String[] args) {
        final BlockplusGame game = new BlockplusGame();
        final GameJSONRepresentation gameJSONRepresentation = new GameJSONRepresentation(game);
        System.out.println(gameJSONRepresentation);
        System.out.println(gameJSONRepresentation.encodePieces());
    }

}