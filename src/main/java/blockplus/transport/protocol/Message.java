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

package blockplus.transport.protocol;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

public class Message implements IMessage {

    private final String type;
    private final JsonObject data;

    public Message(final String type, final JsonObject data) {
        this.type = type;
        this.data = data;

    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public JsonObject getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", this.getType())
                .add("data", this.getData())
                .toString();
    }

}