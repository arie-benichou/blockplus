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

package blockplus.transport.messages;


import blockplus.transport.protocol.IMessage;

import com.google.gson.JsonObject;

public final class Client implements IMessage {

    private final String name;

    public String getName() {
        return this.name;
    }

    public Client(final String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public JsonObject getData() {
        final JsonObject data = new JsonObject();
        data.addProperty("name", this.getName());
        return data;
    }

    @Override
    public String toString() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.getType());
        jsonObject.add("data", this.getData());
        return jsonObject.toString();
    }
}