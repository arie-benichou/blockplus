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
import transport.events.interfaces.FeedbackInterface;

import com.google.gson.JsonObject;

// TODO event = {source:IO, message:JSON}
public final class Feedback implements FeedbackInterface {

    public static class Builder {

        public static Feedback build(final IOinterface io, final JsonObject data) {
            return new Feedback(io, data.get("name").getAsString(), data.get("content").getAsString());
        }

    }

    private final IOinterface io;

    @Override
    public IOinterface getIO() {
        return this.io;
    }

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    private final String content;

    @Override
    public String getContent() {
        return this.content;
    }

    public Feedback(final IOinterface io, final String name, final String content) {
        this.io = io;
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getClass().getSimpleName());
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getName());
        data.addProperty("content", this.getContent());
        jsonObject.add("data", data);
        return jsonObject.toString();
    }
}