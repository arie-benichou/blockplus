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

import interfaces.move.MoveInterface;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.Color;
import blockplus.board.Board;
import blockplus.board.BoardLayer;
import blockplus.context.Context;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceType;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import components.position.PositionInterface;

// TODO extract interface
public final class ContextRepresentation {

    private final Context gameContext;

    public Context getGameContext() {
        return this.gameContext;
    }

    public ContextRepresentation(final Context game) {
        this.gameContext = game;
    }

    public String encodeColor(final Color color) {
        return JSONSerializer.getInstance().toJson(color);
    }

    public JsonElement encodeBoard() {
        final JsonObject boardState = new JsonObject();
        final JsonObject meta = new JsonObject();
        final JsonObject data = new JsonObject();
        final Board board = this.getGameContext().getBoard();
        final int rows = board.rows();
        final int columns = board.columns();
        meta.addProperty("rows", rows);
        meta.addProperty("columns", columns);
        final Set<Color> colors = board.getColors();
        for (final Color color : colors) {
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

    // TODO no more map filtering
    // TODO remove null piece    
    public JsonElement encodePieces() {
        final JsonObject data = new JsonObject();
        final Context context = this.getGameContext();
        for (final Player player : context.getPlayers()) {
            final Color color = player.getColor();
            final JsonArray jsonArray = new JsonArray();
            final PiecesBag pieces = player.getPieces();
            for (final Entry<PieceType, Integer> entry : pieces) {
                if (entry.getValue() > 0) {
                    final PieceType piece = entry.getKey();
                    jsonArray.add(new JsonPrimitive(piece.ordinal()));
                }
            }
            data.add(color.toString(), jsonArray);
        }
        return data;
    }

    public JsonElement encodeOptions() {
        final List<MoveInterface> options = this.getGameContext().options();
        final Map<PieceType, List<Set<PositionInterface>>> legalPositionsByPiece = Maps.newTreeMap();
        for (final MoveInterface moveInterface : options) {
            if (!moveInterface.isNull()) { // TODO à revoir
                final Move move = (Move) moveInterface;
                final PieceInterface piece = move.getPiece();
                final PieceType key = PieceType.get(piece.getId());
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
        data.addProperty("color", this.getGameContext().getSide().toString());
        data.addProperty("isTerminal", this.getGameContext().isTerminal());
        data.add("board", this.encodeBoard());
        data.add("pieces", this.encodePieces());
        data.add("options", this.encodeOptions());
        return data.toString();
    }

}