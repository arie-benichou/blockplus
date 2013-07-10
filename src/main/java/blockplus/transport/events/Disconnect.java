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
import blockplus.transport.events.interfaces.DisconnectInterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class Disconnect implements DisconnectInterface {

    public static class Builder {

        public static Disconnect build(final IOinterface io, final JsonObject data) {
            return new Disconnect(io);
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private Disconnect(final IOinterface io) {
        this.io = io;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }

}