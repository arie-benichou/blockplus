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

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class MessageDecoder implements IMessageDecoder {

    private static final Type TYPE_TOKEN = new TypeToken<IMessage>() {}.getType();

    private static class MessageDeserializer implements JsonDeserializer<IMessage> {

        private final static String TYPE = "type";
        private final static String DATA = "data";

        @Override
        public IMessage deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement typeElement = jsonObject.get(TYPE);
            final JsonElement dataElement = jsonObject.get(DATA);
            return new Message(typeElement.getAsString(), dataElement.getAsJsonObject());
        }

    }

    private final static Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(TYPE_TOKEN, new MessageDeserializer())
            .create();

    @Override
    public IMessage decode(final String data) {
        return GSON_INSTANCE.fromJson(data, TYPE_TOKEN);
    }

}