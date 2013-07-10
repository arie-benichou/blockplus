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


import blockplus.transport.IOinterface;
import blockplus.transport.events.interfaces.GameReconnectionInterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class GameReconnection implements GameReconnectionInterface {

    public static class Builder {

        public static GameReconnection build(final IOinterface io, final JsonObject data) {
            return new GameReconnection(io, data.get("link").getAsJsonObject());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final JsonObject link;

    @Override
    public JsonObject getLink() {
        return this.link;
    }

    private GameReconnection(final IOinterface io, final JsonObject link) {
        this.io = io;
        this.link = link;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("link", this.getLink())
                .toString();
    }

}