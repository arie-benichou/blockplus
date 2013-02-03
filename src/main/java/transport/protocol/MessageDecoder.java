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

package transport.protocol;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class MessageDecoder implements MessageDecoderInterface {

    private static class MessageDeserializer implements JsonDeserializer<MessageInterface> {

        @Override
        public MessageInterface deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject jsonObject = json.getAsJsonObject();
            final JsonElement typeElement = jsonObject.get("type"); // TODO extract constant
            final String type = typeElement.getAsString();
            final JsonElement dataElement = jsonObject.get("data"); // TODO extract constant
            final JsonObject data = dataElement.getAsJsonObject();
            return new Message(type, data);
        }

    }

    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<MessageInterface>() {}.getType(), new MessageDeserializer())
            .create();

    @Override
    public MessageInterface decode(final String data) {
        return GSON_INSTANCE.fromJson(data, new TypeToken<MessageInterface>() {}.getType());
    }

}