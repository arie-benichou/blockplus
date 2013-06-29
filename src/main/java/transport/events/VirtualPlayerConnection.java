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

package transport.events;

import transport.IOinterface;
import transport.events.interfaces.VirtualPlayerConnectionInterface;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class VirtualPlayerConnection implements VirtualPlayerConnectionInterface {

    public static class Builder {

        public static VirtualPlayerConnection build(final IOinterface io, final JsonObject data) {
            return new VirtualPlayerConnection(io, data.get("ordinal").getAsInt());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final Integer ordinal;

    @Override
    public Integer getOrdinal() {
        return this.ordinal;
    }

    private VirtualPlayerConnection(final IOinterface io, final Integer ordinal) {
        this.io = io;
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("ordinal", this.getOrdinal()).toString();
    }

}