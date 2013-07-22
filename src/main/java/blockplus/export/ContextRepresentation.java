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

import blockplus.model.Board;
import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.model.Options;
import blockplus.model.Pieces;
import blockplus.model.Sides.Side;
import blockplus.model.polyomino.Polyomino;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import components.cells.IPosition;

public final class ContextRepresentation {

    private final static Gson GSON = new Gson();

    private final Context gameContext;

    public Context getGameContext() {
        return this.gameContext;
    }

    public ContextRepresentation(final Context game) {
        this.gameContext = game;
    }

    public JsonElement encodeBoard() {
        final JsonObject boardState = new JsonObject();
        final JsonObject meta = new JsonObject();
        final JsonObject data = new JsonObject();
        final Board board = this.getGameContext().board();
        final int rows = board.rows();
        final int columns = board.columns();
        meta.addProperty("rows", rows);
        meta.addProperty("columns", columns);
        final Set<Colors> colors = board.getColors();
        for (final Colors color : colors) {
            final JsonArray jsonArray = new JsonArray();
            for (final IPosition position : board.getSelves(color))
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
        for (final Entry<Colors, Side> sideEntry : context.sides()) {
            final Colors color = sideEntry.getKey();
            final Side side = sideEntry.getValue();
            int bits = 1;
            final Pieces remainingPieces = side.remainingPieces();
            for (final Entry<Polyomino, Integer> entry : remainingPieces) {
                if (entry.getKey().ordinal() == 0) continue;
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
        final Board board = this.getGameContext().board();
        final int rows = board.rows();
        final int columns = board.columns();
        final Options options = this.getGameContext().options();
        final Map<Integer, List<Set<Integer>>> legalPositionsByPiece = Maps.newTreeMap(); // TODO à revoir
        for (final Entry<Polyomino, Map<IPosition, List<Set<IPosition>>>> entry : options.byPolyomino()) {
            final Polyomino polyomino = entry.getKey();
            final Map<IPosition, List<Set<IPosition>>> map = entry.getValue();
            final List<Set<Integer>> playablePositions = Lists.newArrayList();
            for (final Entry<IPosition, List<Set<IPosition>>> instancesByLight : map.entrySet()) {
                final List<Set<IPosition>> instances = instancesByLight.getValue();
                for (final Set<IPosition> set : instances) {
                    final Set<Integer> positions = Sets.newHashSet();
                    for (final IPosition position : set) {
                        positions.add(columns * position.row() + position.column() % rows); // TODO !!!
                    }
                    playablePositions.add(positions);
                }
            }
            legalPositionsByPiece.put(polyomino.ordinal() + 1, playablePositions);
        }
        return GSON.toJsonTree(legalPositionsByPiece);
    }

    @Override
    public String toString() {
        final JsonObject data = new JsonObject();
        data.addProperty("color", this.getGameContext().side().toString());
        data.addProperty("isTerminal", this.getGameContext().isTerminal());
        data.add("board", this.encodeBoard());
        data.add("pieces", this.encodePieces());
        data.add("options", this.encodeOptions());
        return data.toString();
    }

}