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

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import components.position.Position;
import components.position.PositionInterface;

@Deprecated
public final class PositionSerializer implements JsonSerializer<Position> {

    @Override
    public JsonElement serialize(final Position position, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(position.row()));
        jsonArray.add(new JsonPrimitive(position.column()));
        return jsonArray;
    }

    public static void main(final String[] args) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(new TypeToken<Position>() {}.getType(), new PositionSerializer());
        final Gson gson = gsonBuilder.create();

        final PositionInterface position = Position.from(4, 7);
        final String json = gson.toJson(position);
        System.out.println(json);

    }

}