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
import blockplus.transport.events.interfaces.INotification;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public final class Notification implements INotification {

    public static class Builder {

        public static Notification build(final IEndPoint io, final JsonObject data) {
            return new Notification(io, data.get("from").getAsString(), data.get("to").getAsString(), data.get("message").getAsString());
        }

    }

    private final IEndPoint io;

    @Override
    public IEndPoint getEndpoint() {
        return this.io;
    }

    private final String from;
    private final String to;
    private final String message;

    public Notification(final IEndPoint io, final String from, final String to, final String message) {
        this.io = io;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("io", this.getEndpoint())
                .add("from", this.getFrom())
                .add("to", this.getTo())
                .add("message", this.getMessage())
                .toString();
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    @Override
    public String getTo() {
        return this.to;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}