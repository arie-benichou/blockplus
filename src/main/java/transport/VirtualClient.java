
package transport;

import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

import transport.messages.MoveSubmit;
import blockplus.model.piece.Pieces;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
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

    private transient Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    private transient String color;

    public VirtualClient(final String username, final WebSocketClient client, final String host, final int port) {
        this.name = username;
        this.client = client;
        this.uri = "ws://" + host + ":" + port + "/io/";
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
    public void onOpen(final Connection connection) {
        //System.out.println(this);
    }

    @Override
    public void onClose(final int closeCode, final String message) {

    }

    @Override
    public void onMessage(final String message) {
        //System.out.println(message);
        final JsonParser jsonParser = new JsonParser();
        final JsonObject jsonObject = jsonParser.parse(message).getAsJsonObject();
        final String type = jsonObject.get("type").getAsString();
        if (type.equals("color")) {
            this.color = jsonObject.get("data").getAsString();
        }
        else if (type.equals("update")) {
            final JsonObject data = jsonObject.get("data").getAsJsonObject();
            final String color = data.get("color").getAsString();
            if (color.equals(this.color)) {
                final JsonObject options = data.get("options").getAsJsonObject();
                final Set<Entry<String, JsonElement>> entrySet = options.entrySet();
                System.out.println();
                System.out.println(this);
                System.out.println("It's my turn !");
                //System.out.println(entrySet);
                System.out.println(entrySet.size());
                // TODO color on game over should be none...
                //if (entrySet.size() == 0) System.out.println(data.get("isTerminal").getAsBoolean());
                if (data.get("isTerminal").getAsBoolean()) {
                    System.out.println("Oh no ! I'm such a fool, game is over ! :(");
                }
                else {
                    final List<Entry<String, JsonElement>> list = Lists.newArrayList(entrySet);
                    final Entry<String, JsonElement> entry = list.get(entrySet.size() - 1);
                    final String piece = entry.getKey();
                    final JsonArray instances = entry.getValue().getAsJsonArray();
                    //System.out.println(piece);
                    //System.out.println(instances);
                    //System.out.println(instances.size());
                    final int n = new Random().nextInt(instances.size());
                    //System.out.println(n);
                    final JsonArray positions = instances.get(n).getAsJsonArray();
                    //System.out.println(positions);
                    final Pieces pieceObject = Pieces.valueOf(piece);
                    //System.out.println(pieceObject);
                    //System.out.println("----------------------------------------------------");
                    final MoveSubmit moveSubmit = new MoveSubmit(pieceObject.ordinal(), positions);
                    System.out.println(moveSubmit);
                    try {
                        Thread.sleep(750);
                        this.send(moveSubmit);
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
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