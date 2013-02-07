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

import blockplus.piece.Pieces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import components.position.Position;
import components.position.PositionInterface;

@Deprecated
public final class JSONSerializer {

    private static final Gson INSTANCE = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<PositionInterface>() {}.getType(), new PositionSerializer())
            .registerTypeAdapter(new TypeToken<Position>() {}.getType(), new PositionSerializer())
            .registerTypeAdapter(new TypeToken<Pieces>() {}.getType(), new PiecesSerializer())
            .create();

    private JSONSerializer() {}

    public static Gson getInstance() {
        return INSTANCE;
    }

}