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
import blockplus.board.Layer;
import blockplus.context.Context;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceType;
import blockplus.piece.Pieces;
import blockplus.player.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
            final Layer layer = board.getLayer(color);
            final Set<PositionInterface> positions = layer.getSelves().keySet();
            for (final PositionInterface position : positions)
                jsonArray.add(new JsonPrimitive(columns * position.row() + position.column() % rows)); // TODO extract method
            data.add(color.toString(), jsonArray);
        }
        boardState.add("dimension", meta);
        boardState.add("cells", data);
        return boardState;
    }

    public JsonElement _encodePieces() {
        final JsonObject data = new JsonObject();
        final Context context = this.getGameContext();
        for (final Player player : context.getPlayers()) {
            final Color color = player.getColor();
            final JsonObject jsonObject = new JsonObject();
            final Pieces pieces = player.getPieces();
            for (final Entry<PieceType, Integer> entry : pieces) {
                final int ordinal = entry.getKey().ordinal();
                if (ordinal != 0) jsonObject.addProperty("" + ordinal, entry.getValue());
            }
            data.add(color.toString(), jsonObject);
        }
        return data;
    }

    public JsonElement encodePieces() {
        final JsonObject data = new JsonObject();
        final Context context = this.getGameContext();
        for (final Player player : context.getPlayers()) {
            final Color color = player.getColor();
            int bits = 0b1;
            for (final Entry<PieceType, Integer> entry : player.getPieces()) {
                bits = bits << 1 | entry.getValue();
            }
            data.add(color.toString(), new JsonPrimitive(bits));
        }
        return data;
    }

    public JsonElement encodeOptions() {
        final List<MoveInterface> options = this.getGameContext().options();
        final Map<Integer, List<Set<Integer>>> legalPositionsByPiece = Maps.newTreeMap();
        for (final MoveInterface moveInterface : options) {
            if (!moveInterface.isNull()) { // TODO à revoir
                final Move move = (Move) moveInterface;
                final PieceInterface piece = move.getPiece();
                final int key = piece.getId();
                List<Set<Integer>> playablePositions = legalPositionsByPiece.get(key);
                if (playablePositions == null) {
                    playablePositions = Lists.newArrayList();
                    legalPositionsByPiece.put(key, playablePositions);
                }
                final Set<Integer> positions = Sets.newHashSet();
                for (final PositionInterface position : piece.getSelfPositions()) {
                    // TODO position factory: id d'une position
                    final int id = 20 * position.row() + position.column() % 20;
                    positions.add(id);
                }
                playablePositions.add(positions);
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

    public static void main(final String[] args) {
        int data = 0b1;
        for (int i = 0; i < 22; ++i) {
            data = data << 1 | i % 2;
        }
        System.out.println();
        System.out.println(data);
        System.out.println(Integer.toBinaryString(data));
    }

}