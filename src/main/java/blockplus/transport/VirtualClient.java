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

package blockplus.transport;

import java.net.URI;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

import blockplus.model.Colors;
import blockplus.transport.messages.MoveSubmit;

import com.google.common.base.Objects;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VirtualClient implements WebSocket.OnTextMessage
{

    private final String name;

    public String getName() {
        return this.name;
    }

    private final WebSocketClient client;

    public WebSocketClient getClient() {
        return this.client;
    }

    private final String uri;

    public String getUri() {
        return this.uri;
    }

    private Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    private String color;

    public VirtualClient(final String username, final WebSocketClient client, final String host, final int port, final String base) {
        this.name = username;
        this.client = client;
        this.uri = "ws://" + host + ":" + port + "/" + base;
    }

    public void start() throws Exception {
        this.connection = this.getClient().open(new URI(this.getUri()), this).get();
    }

    public void send(final String message) throws Exception {
        this.connection.sendMessage(message);
    }

    public void send(final Object message) throws Exception {
        this.send(message.toString());
    }

    @Override
    public void onOpen(final Connection connection) {}

    @Override
    public void onClose(final int closeCode, final String message) {

    }

    @Override
    public void onMessage(final String message) {
        final JsonParser jsonParser = new JsonParser();
        final JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        final String type = jsonObject.get("type").getAsString();
        if (type.equals("game")) {
            final JsonObject data = jsonObject.get("data").getAsJsonObject();
            final int k = data.get("players").getAsInt();
            this.color = Colors.values()[k - 1].toString();
        }
        if (type.equals("update")) {
            final JsonObject data = jsonObject.get("data").getAsJsonObject();
            final String color = data.get("color").getAsString();
            if (color.equals(this.color)) {
                final JsonObject options = data.get("options").getAsJsonObject();
                if (data.get("isTerminal").getAsBoolean()) {
                    System.out.println("Game Over");
                }
                else {
                    final Set<Entry<String, JsonElement>> entrySet = options.entrySet();
                    int max = 0;
                    String maxlight = null;
                    for (final Entry<String, JsonElement> entry : entrySet) {
                        final String light = entry.getKey();
                        final JsonObject polyonimos = entry.getValue().getAsJsonObject();
                        for (final Entry<String, JsonElement> polyonimo : polyonimos.entrySet()) {
                            final int ordinal = Integer.parseInt(polyonimo.getKey());
                            if (ordinal > max) {
                                max = ordinal;
                                maxlight = light;
                            }
                        }
                    }
                    final JsonObject jsonObject2 = options.get(maxlight).getAsJsonObject();
                    final JsonArray instancesOfPolynimoWithBiggestId = jsonObject2.get(String.valueOf(max)).getAsJsonArray();

                    final int n = new Random().nextInt(instancesOfPolynimoWithBiggestId.size());
                    final JsonArray positions = instancesOfPolynimoWithBiggestId.get(n).getAsJsonArray();
                    final MoveSubmit moveSubmit = new MoveSubmit(positions);
                    try {
                        this.send(moveSubmit);
                    }
                    catch (final Exception e) {}
                }
            }
        }
    }

    public void stop() {
        this.connection.close();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", this.getName())
                .add("uri", this.getUri())
                .toString();
    }

}