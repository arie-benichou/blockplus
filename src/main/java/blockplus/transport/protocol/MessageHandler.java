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

import blockplus.transport.IOinterface;

import com.google.common.base.Throwables;
import com.google.gson.JsonObject;

public class MessageHandler implements MessageHandlerInterface {

    private final String configuration;

    public String getConfiguration() {
        return this.configuration;
    }

    public MessageHandler() {
        this.configuration = null;
    }

    //TODO
    public MessageHandler(final String configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object handle(final IOinterface io, final MessageInterface message) {
        Object object = null;
        final String type = message.getType();
        final JsonObject data = message.getData();
        try {
            final String inflection = "blockplus.transport.events." + type + "$Builder";
            object = Class.forName(inflection).getMethod("build", IOinterface.class, JsonObject.class).invoke(null, io, data);
        }
        catch (final Exception e) {
            Throwables.propagate(e); // TODO
        }
        // TODO check for null and create null events
        return object;
    }

}