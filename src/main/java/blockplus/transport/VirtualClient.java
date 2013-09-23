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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;

import blockplus.ai.AI3;
import blockplus.imports.BoardEncoding;
import blockplus.imports.SidesEncoding;
import blockplus.model.Board;
import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.model.Options;
import blockplus.model.Sides;
import blockplus.model.polyomino.Polyomino;
import blockplus.transport.messages.MoveSubmit;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import components.cells.IPosition;

public class VirtualClient implements WebSocket.OnTextMessage {

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

            //            System.out.println(data);

            final String color = data.get("color").getAsString();
            if (color.equals(this.color)) {
                if (data.get("isTerminal").getAsBoolean()) {
                    System.out.println("Game Over");
                }
                else {

                    final Colors side = Colors.valueOf(this.color);

                    final BoardEncoding boardEncoding = new BoardEncoding();
                    final Board board = boardEncoding.decode(data.get("board").getAsJsonObject());

                    //                    final OptionsEncoding optionsEncoding = new OptionsEncoding();
                    //                    final Options options = optionsEncoding.decode(data.get("options").getAsJsonObject());

                    final SidesEncoding sidesEncoding = new SidesEncoding();
                    final Sides sides = sidesEncoding.decode(data.get("pieces").getAsJsonObject());

                    final Context context = new Context(side, sides, board);
                    final AI3 ai = new AI3();

                    //                    final IPosition position = this.testAI(side, board, options);
                    //                    final Set<IPosition> positions = this.moveSupplier(options, position);

                    System.out.println();
                    System.out.println(side);
                    final Set<IPosition> positions = ai.get(context);

                    final JsonArray jsonArray = new JsonArray();
                    for (final IPosition iPosition : positions)
                        jsonArray.add(new JsonPrimitive(20 * iPosition.row() + iPosition.column() % 20)); // TODO !!!

                    final MoveSubmit moveSubmit = new MoveSubmit(jsonArray);

                    try {
                        this.send(moveSubmit);
                    }
                    catch (final Exception e) {}
                }
            }
        }
    }

    private IPosition testAI(final Colors color, final Board board, final Options options) {
        System.out.println();
        System.out.println(color);
        final Iterable<IPosition> ownLights = board.getLights(color);
        System.out.println(ownLights);
        final Set<IPosition> setOfOwnLights = Sets.newHashSet(ownLights);
        final Set<Colors> colors = board.getColors();
        colors.remove(color);
        Set<IPosition> intersections = Sets.newHashSet();
        for (final Colors Color : colors) {
            final Iterable<IPosition> otherLights = board.getLights(Color);
            final SetView<IPosition> intersection = Sets.intersection(setOfOwnLights, Sets.newHashSet(otherLights));
            intersections = Sets.union(intersections, intersection);
        }
        IPosition position = null;
        if (!intersections.isEmpty()) {
            System.out.println(intersections);
            int max = 0;
            for (final IPosition iPosition : intersections) {
                int n = 0;
                for (final Colors Color : colors) {
                    if (Sets.newHashSet(board.getLights(Color)).contains(iPosition)) ++n;
                }
                if (n > max) {
                    max = n;
                    position = iPosition;
                }
            }
            System.out.println(max);
            System.out.println(position);
        }
        return position;
    }

    private Set<IPosition> moveSupplier(final Options options, final IPosition position) {
        IPosition l = position;
        Polyomino p = Polyomino._0;
        final Map<IPosition, Map<Polyomino, List<Set<IPosition>>>> byLight = options.byLight();
        if (l == null) {
            for (final Entry<IPosition, Map<Polyomino, List<Set<IPosition>>>> entry : byLight.entrySet()) {
                final IPosition light = entry.getKey();
                final Map<Polyomino, List<Set<IPosition>>> polyonimos = entry.getValue();
                for (final Entry<Polyomino, List<Set<IPosition>>> byPolyonimo : polyonimos.entrySet()) {
                    final Polyomino polyomino = byPolyonimo.getKey();
                    if (polyomino.ordinal() > p.ordinal()) {
                        p = polyomino;
                        l = light;
                    }
                }
            }
            final Map<Polyomino, List<Set<IPosition>>> map = options.byLight().get(l);
            final List<Set<IPosition>> instances = map.get(p);
            final int n = new Random().nextInt(instances.size());
            final Set<IPosition> positions = instances.get(n);
            return positions;
        }
        final Map<Polyomino, List<Set<IPosition>>> map = options.byLight().get(l);
        for (final Entry<Polyomino, List<Set<IPosition>>> byPolyonimo : map.entrySet()) {
            final Polyomino polyomino = byPolyonimo.getKey();
            if (polyomino.ordinal() > p.ordinal()) p = polyomino;
        }
        final List<Set<IPosition>> instances = map.get(p);
        final int n = new Random().nextInt(instances.size());
        final Set<IPosition> positions = instances.get(n);
        return positions;
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

    public static void main(final String[] args) {
        final Context context = new Context.Builder().build();
    }

}