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

package blockplus.export;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.model.board.Board;
import blockplus.model.board.Layer;
import blockplus.model.context.Color;
import blockplus.model.context.Context;
import blockplus.model.entity.Polyomino;
import blockplus.model.player.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import components.cells.Positions.Position;

public final class ContextRepresentation {

    private final static Gson GSON = new Gson();

    private final Context gameContext;

    public Context getGameContext() {
        return this.gameContext;
    }

    public ContextRepresentation(final Context game) {
        this.gameContext = game;
    }

    public String encodeColor(final Color color) {
        return "\"" + color.toString() + "\"";
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
            final Layer layer = board.get(color);
            final Set<Position> positions = layer.getSelves().keySet();
            for (final Position position : positions)
                jsonArray.add(new JsonPrimitive(columns * position.row() + position.column() % rows)); // TODO extract method
            data.add(color.toString(), jsonArray);
        }
        boardState.add("dimension", meta);
        boardState.add("cells", data);
        return boardState;
    }

    public JsonElement encodePieces() {
        final JsonObject data = new JsonObject();
        final Context context = this.getGameContext();
        for (final Color color : context.sides()) {
            final Player player = context.getPlayer(color);
            int bits = 0b1;
            for (final Entry<Polyomino, Integer> entry : player.remainingPieces()) {
                bits = bits << 1 | entry.getValue();
            }
            data.add(color.toString(), new JsonPrimitive(bits));
        }
        return data;
    }

    /* TODO !!! cf roadmap
    final ObjectMapper mapper = new ObjectMapper();
    final String json = mapper.writeValueAsString(options.rowMap());
    System.out.println(json);
    */
    public JsonElement encodeOptions() {
        final Table<Position, Polyomino, List<Set<Position>>> options = (Table<Position, Polyomino, List<Set<Position>>>) this.getGameContext().options();
        final Map<Integer, List<Set<Integer>>> legalPositionsByPiece = Maps.newTreeMap(); // TODO à revoir
        for (final Entry<Polyomino, Map<Position, List<Set<Position>>>> entry : options.columnMap().entrySet()) {
            final Polyomino polyomino = entry.getKey();
            final Map<Position, List<Set<Position>>> map = entry.getValue();
            final List<Set<Integer>> playablePositions = Lists.newArrayList();
            for (final Entry<Position, List<Set<Position>>> instancesByLight : map.entrySet()) {
                final List<Set<Position>> instances = instancesByLight.getValue();
                for (final Set<Position> set : instances) {
                    final Set<Integer> positions = Sets.newHashSet();
                    for (final Position position : set) {
                        positions.add(position.id());
                    }
                    playablePositions.add(positions);
                }
            }
            legalPositionsByPiece.put(polyomino.ordinal() + 1, playablePositions);
        }
        return GSON.toJsonTree(legalPositionsByPiece);
    }

    /*
    public JsonElement encodeComputedOptions(final List<IOption> options) {
        //final List<MoveInterface> options = this.getGameContext().options();
        final Map<Integer, List<Set<Integer>>> legalPositionsByPiece = Maps.newTreeMap();
        for (final IOption moveInterface : options) {
            if (!moveInterface.isNull()) { // TODO à revoir
                final Option move = (Option) moveInterface;
                final PieceInstance piece = move.getPiece();
                final int key = piece.entity.type(); // TODO à revoir
                List<Set<Integer>> playablePositions = legalPositionsByPiece.get(key);
                if (playablePositions == null) {
                    playablePositions = Lists.newArrayList();
                    legalPositionsByPiece.put(key, playablePositions);
                }
                final Set<Integer> positions = Sets.newHashSet();
                for (final Position position : piece.positions()) {
                    // TODO position factory: id d'une position
                    final int id = 20 * position.row() + position.column() % 20;
                    positions.add(id);
                }
                playablePositions.add(positions);
            }
        }
        return GSON.toJsonTree(legalPositionsByPiece);
    }
    */

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