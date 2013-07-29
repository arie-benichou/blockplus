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

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.jetty.websocket.WebSocketServlet;

import blockplus.model.Context;
import blockplus.transport.events.interfaces.IClient;
import blockplus.transport.events.interfaces.IDisconnect;
import blockplus.transport.events.interfaces.IEvent;
import blockplus.transport.events.interfaces.IInPatio;
import blockplus.transport.messages.Messages;
import blockplus.transport.protocol.IMessage;
import blockplus.transport.protocol.IMessageHandler;
import blockplus.transport.protocol.MessageDecoder;
import blockplus.transport.protocol.MessageHandler;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class BlockplusServer extends WebSocketServlet {

    private final static int GAMES = 25;

    private final EventBus eventBus = new EventBus();
    private final IMessageHandler messageHandler = new MessageHandler(); // TODO à injecter
    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IEndPoint, IClient> clientByEndpoint = Maps.newConcurrentMap();
    private final ConcurrentLinkedDeque<IEndPoint> endpointsInPatio = new ConcurrentLinkedDeque<IEndPoint>();
    private final Map<Integer, GameInterface<Context>> gameByOrdinal = Maps.newConcurrentMap(); // TODO utiliser un Futur

    @Override
    public void init() throws ServletException {
        super.init();
        this.eventBus.register(this);
        final BlockplusServerEvents blockplusServerEvents = new BlockplusServerEvents(this); // TODO à injecter
        this.eventBus.register(blockplusServerEvents);
        final ImmutableList<IClient> empty = ImmutableList.of();
        for (int i = 1; i <= GAMES; ++i) {
            this.updateGame(i, new BlockplusGame(i, empty, null));
        }
    }

    public void updateClients(final IEndPoint endPoint, final IClient user) {
        this.clientByEndpoint.put(endPoint, user);
    }

    public GameInterface<Context> getGame(final Integer ordinal) {
        return this.gameByOrdinal.get(ordinal);
    }

    public void updateGame(final Integer ordinal, final GameInterface<Context> newGame) {
        this.gameByOrdinal.put(ordinal, newGame);
    }

    public void removeFromPatio(final IEndPoint endpoint) {
        this.endpointsInPatio.remove(endpoint);
    }

    public ConcurrentLinkedDeque<IEndPoint> getEndpointsInPatio() {
        return this.endpointsInPatio;
    }

    @Override
    public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
        return new EndPoint(this);
    }

    public IMessage decode(final String data) {
        return this.messageDecoder.decode(data);
    }

    public Object handle(final IEndPoint endPoint, final IMessage message) {
        return this.messageHandler.handle(endPoint, message);
    }

    public IClient getClientByEndpoint(final IEndPoint endPoint) {
        return this.clientByEndpoint.get(endPoint);
    }

    private void connect(final IEndPoint endPoint, final IClient client) {
        this.clientByEndpoint.put(endPoint, client);
    }

    public void disconnect(final IEndPoint endPoint) {
        this.clientByEndpoint.remove(endPoint);
        this.endpointsInPatio.remove(endPoint);
    }

    public void onMessage(final IEndPoint endPoint, final String data) {
        IMessage message = null;
        try {
            message = this.decode(data);
            System.out.println(message);
        }
        catch (final Exception e) { // TODO MessageConstructionException
            endPoint.say("Message could not be created from " + data + " : " + Throwables.getRootCause(e));
        }
        if (message != null) {
            Object object = null;
            try {
                object = this.handle(endPoint, message);
            }
            catch (final Exception e) { // TODO EventConstructionException
                endPoint.say("Event could not be created from " + message + " : " + Throwables.getRootCause(e));
            }
            if (object != null) {
                try {
                    this.eventBus.post(object);
                }
                catch (final Exception e) { // TODO EventDispatchingException
                    endPoint.say("Event could not be dispatched from " + object + " : " + Throwables.getRootCause(e));
                }
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onConnection(final IClient newClient) {
        this.connect(newClient.getEndpoint(), newClient);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onDisconnection(final IDisconnect client) {
        this.disconnect(client.getEndpoint());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onInPatio(final IInPatio inPatio) {
        final IEndPoint endPoint = inPatio.getEndpoint();
        final JsonObject tables = this.games();
        endPoint.emit("tables", tables.toString());
        for (final IEndPoint other : this.endpointsInPatio) {
            other.emit("tables", tables.toString());
        }
        this.endpointsInPatio.add(endPoint);
    }

    private BlockplusGame reset(final GameInterface<Context> game) {
        final ImmutableList<IClient> empty = ImmutableList.of();
        final BlockplusGame newGame = new BlockplusGame(game.getOrdinal(), empty, null);
        this.updateGame(game.getOrdinal(), newGame);
        return newGame;
    }

    public JsonObject games() {
        final JsonObject tables = new JsonObject();
        for (final GameInterface<Context> game : this.gameByOrdinal.values()) {
            if (game.isFull()) {
                boolean isAlive = false;
                for (final IClient client : game.getClients()) {
                    final boolean isOpen = client.getEndpoint().isOpen();
                    if (!isOpen) this.disconnect(client.getEndpoint());
                    isAlive = isAlive || isOpen;
                }
                if (!isAlive) {
                    tables.addProperty("" + this.reset(game).getOrdinal(), this.reset(game).getClients().size());
                }
            }
            else {
                boolean isFullyAlive = true;
                boolean isFullyDead = true;
                for (final IClient client : game.getClients()) {
                    final boolean isOpen = client.getEndpoint().isOpen();
                    isFullyAlive = isFullyAlive && isOpen;
                    isFullyDead = isFullyDead && !isOpen;
                }
                if (isFullyAlive) {
                    tables.addProperty("" + game.getOrdinal(), game.getClients().size());
                }
                else if (isFullyDead) {
                    tables.addProperty("" + this.reset(game).getOrdinal(), this.reset(game).getClients().size());
                }
            }
        }
        return tables;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onDeadEvent(final DeadEvent deadEvent) {
        final Object event = deadEvent.getEvent();
        if (event instanceof IEvent) {
            final IEvent eventInterface = (IEvent) event;
            eventInterface.getEndpoint().emit("dead event", "\"" + eventInterface + "\"");
        }
        else System.out.println(deadEvent.getEvent());
    }

    // TODO use other virtual clients for testing
    public static void runVC(final String[] args) throws Exception {
        final int game = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        final String host = "localhost";
        final int port = 8282;
        final WebSocketClientFactory factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();
        final Messages messages = new Messages();
        final WebSocketClient client = factory.newWebSocketClient();
        client.setMaxIdleTime(60 * 1000 * 5);
        client.setMaxTextMessageSize(1024 * 64);
        final VirtualClient virtualClient = new VirtualClient("virtual-client", client, host, port, "io");
        virtualClient.start();
        final IMessage message1 = messages.newClient(virtualClient.getName());
        virtualClient.send(message1);
        final IMessage message2 = messages.newGameConnection(game);
        virtualClient.send(message2);
        //virtualClient.stop();
        //factory.stop();
    }

    public static void main(final String[] args) throws Exception {
        for (int n = 0; n < 4; ++n) {
            runVC(new String[] { "" + 4 });
        }
    }

}