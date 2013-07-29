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

package blockplus.transport.events;

import blockplus.transport.IEndPoint;
import blockplus.transport.events.interfaces.IClient;

import com.google.gson.JsonObject;

// TODO event = {source:IO, message:JSON}
public final class Client implements IClient {

    public static class Builder {

        public static Client build(final IEndPoint io, final JsonObject data) {
            // TODO à revoir
            return new Client(io, data.get("name").getAsString(), data.has("game") ? data.get("game").getAsInt() : 0);
        }

    }

    private final IEndPoint io;

    @Override
    public IEndPoint getEndpoint() {
        return this.io;
    }

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getGame() { // TODO à revoir
        return this.game;
    }

    // TODO à revoir
    private final Integer game;

    public Client(final IEndPoint io, final String name, final Integer game) {
        this.io = io;
        this.name = name;
        this.game = game;
    }

    @Override
    public String toString() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getClass().getSimpleName());
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getName());
        data.addProperty("game", this.getGame().toString());
        data.addProperty("io", this.getEndpoint().toString());
        jsonObject.add("data", data);
        return jsonObject.toString();
    }
}