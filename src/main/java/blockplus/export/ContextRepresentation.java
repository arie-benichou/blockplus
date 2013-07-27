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

import java.lang.reflect.Type;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import components.cells.IPosition;

public final class ContextRepresentation {

    private final static Type POSITIONS_BY_POLYOMINO = new TypeToken<Map<Polyomino, List<Set<IPosition>>>>() {}.getType();

    private final static class PolyominoSerializer implements JsonSerializer<Polyomino> {
        @Override
        public JsonElement serialize(final Polyomino polyomino, final Type typeOfSrc, final JsonSerializationContext context) {
            return new JsonPrimitive(polyomino.ordinal());
        }
    }

    private final class PositionSerializer implements JsonSerializer<IPosition> {
        @Override
        public JsonElement serialize(final IPosition position, final Type typeOfSrc, final JsonSerializationContext context) {
            final int columns = ContextRepresentation.this.context.board().columns();
            final int rows = ContextRepresentation.this.context.board().rows();
            return new JsonPrimitive(columns * position.row() + position.column() % rows);
        }
    }

    private final Gson gson;

    private final Context context;

    public ContextRepresentation(final Context game) {
        this.gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(Polyomino.class, new PolyominoSerializer())
                .registerTypeAdapter(IPosition.class, new PositionSerializer())
                .create();
        this.context = game;
    }

    public JsonElement encodeBoard() {
        final JsonObject boardState = new JsonObject();
        final JsonObject meta = new JsonObject();
        final JsonObject data = new JsonObject();
        final Board board = this.context.board();
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
        final Context context = this.context;
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

    public JsonElement _encodeOptions() {
        final Board board = this.context.board();
        final int rows = board.rows();
        final int columns = board.columns();
        final Options options = this.context.options();
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
        return this.gson.toJsonTree(legalPositionsByPiece);
    }

    public JsonElement encodeOptions() {
        final Options options = this.context.options();
        final Set<Entry<IPosition, Map<Polyomino, List<Set<IPosition>>>>> byLight = options.byLight();
        final JsonObject jsonObject = new JsonObject();
        for (final Entry<IPosition, Map<Polyomino, List<Set<IPosition>>>> entry : byLight) {
            final String light = this.gson.toJson(entry.getKey(), IPosition.class);
            final JsonElement positionsByPolyominos = this.gson.toJsonTree(entry.getValue(), POSITIONS_BY_POLYOMINO);
            jsonObject.add(light, positionsByPolyominos);
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        final JsonObject data = new JsonObject();
        data.addProperty("color", this.context.side().toString());
        data.addProperty("isTerminal", this.context.isTerminal());
        data.add("board", this.encodeBoard());
        data.add("pieces", this.encodePieces());
        data.add("options", this.encodeOptions());
        return data.toString();
    }

}