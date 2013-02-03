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

import blockplus.piece.Pieces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

@Deprecated
public final class PiecesSerializer implements JsonSerializer<Pieces> {

    @Override
    public JsonElement serialize(final Pieces piece, final Type typeOfSrc, final JsonSerializationContext context) {
        return new JsonPrimitive(piece.ordinal());
    }

    public static void main(final String[] args) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.registerTypeAdapter(new TypeToken<Pieces>() {}.getType(), new PiecesSerializer());
        final Gson gson = gsonBuilder.create();

        final Pieces position = Pieces.get(7);
        final String json = gson.toJson(position);
        System.out.println(json);

    }

}