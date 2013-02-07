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

package transport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.eclipse.jetty.websocket.WebSocketServlet;

import transport.events.interfaces.ClientInterface;
import transport.events.interfaces.EventInterface;
import transport.events.interfaces.FeedbackInterface;
import transport.events.interfaces.ShowGameInterface;
import transport.messages.Messages;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;
import blockplus.context.Context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class BlockplusServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter
    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, ClientInterface> clientByIO = Maps.newConcurrentMap();

    public void updateClients(final IOinterface io, final ClientInterface user) {
        this.clientByIO.put(io, user);
    }

    public void removeFromClients(final IOinterface io) {
        this.clientByIO.remove(io);
    }

    // TODO utiliser un Futur<GameInterface<BlockplusGameContext>>
    private final Map<Integer, GameInterface<Context>> gameByOrdinal = Maps.newConcurrentMap();

    public GameInterface<Context> getGame(final Integer ordinal) {
        return this.gameByOrdinal.get(ordinal);
    }

    public void updateGames(final Integer ordinal, final GameInterface<Context> newGame) {
        this.gameByOrdinal.put(ordinal, newGame);

        // TODO asynch
        // TODO notifier uniquement les clients dans le patio
        // TODO prévoir une pagination
        if (newGame.isFull()) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("game", ordinal);
            jsonObject.addProperty("board", newGame.toJson());
            for (final IOinterface io : this.clientByIO.keySet()) {
                io.emit("game", jsonObject.toString());
            }
        }

    }

    private final Map<Integer, List<ClientInterface>> clientsByGame = Maps.newConcurrentMap(); // TODO à virer

    //TODO add patio

    public List<ClientInterface> getClientsByGame(final Integer game) {
        return this.clientsByGame.get(game);
    }

    public void updateGame(final Integer gameOrdinal, final ImmutableList<ClientInterface> gameUsers) {
        this.clientsByGame.put(gameOrdinal, gameUsers);
    }

    public Collection<ClientInterface> getClients() {
        return this.clientByIO.values();
    }

    private final EventBus eventBus = new EventBus();

    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public void init() throws ServletException {

        super.init();

        this.getEventBus().register(this);

        final BlockplusServerEvents blockplusServerEvents = new BlockplusServerEvents(this); // TODO à injecter
        this.getEventBus().register(blockplusServerEvents);

        for (int i = 1; i <= 12; ++i) {
            final ImmutableList<ClientInterface> empty = ImmutableList.of();
            this.clientsByGame.put(i, empty);
            this.gameByOrdinal.put(i, new BlockplusGame(i, "", empty, null, 0));
        }

    }

    @Override
    public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
        /*
        try {
            Thread.sleep(2 * 1000);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        */
        System.out.println(request);
        return new IO(this);
    }

    public void connect(final ClientInterface user) {
        this.clientByIO.put(user.getIO(), user);
    }

    public void disconnect(final IOinterface io) {
        this.clientByIO.remove(io);
    }

    public MessageInterface decode(final String data) {
        return this.messageDecoder.decode(data);
    }

    public Object handle(final IO io, final MessageInterface message) {
        return this.messageHandler.handle(io, message);
    }

    public ClientInterface getClient(final IOinterface io) {
        return this.clientByIO.get(io);
    }

    private List<Integer> getGames() {
        final Set<Integer> keySet = this.gameByOrdinal.keySet();
        final ArrayList<Integer> games = Lists.newArrayList(keySet);
        Collections.sort(games);
        return games;
    }

    @Subscribe
    @AllowConcurrentEvents
    // TODO à revoir
    public void onNewClient(final ClientInterface newClient) {
        this.connect(newClient);
        newClient.getIO().emit("info", "\"" + "Welcome " + newClient.getName() + " !" + "\"");
        newClient.getIO().emit("welcome", "\"" + newClient.getName() + "\"");
        final String game = new Gson().toJson(this.getGames());
        newClient.getIO().emit("games", "\"" + game + "\"");
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onShowGame(final ShowGameInterface showGame) {
        final IOinterface io = showGame.getIO();
        final Integer ordinal = showGame.getOrdinal();
        final GameInterface<Context> game = this.gameByOrdinal.get(ordinal);
        if (game.isFull()) { // TODO à revoir
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("game", ordinal);
            jsonObject.addProperty("board", game.toJson());
            io.emit("game", jsonObject.toString());
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onFeedback(final FeedbackInterface feedback) {
        final Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("arie.benichou", "")); // TODO
        email.setSSLOnConnect(true);
        try {
            email.setFrom("arie.benichou@gmail.com");
            email.setSubject("[Block+] New feedback from " + feedback.getName() + " !");
            email.setMsg(feedback.getContent());
            email.addTo("arie.benichou@gmail.com");
            email.send();
            feedback.getIO().emit("info", "\"" + "Thank you for your feedback !" + "\"");
        }
        catch (final EmailException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onDeadEvent(final DeadEvent deadEvent) {
        final Object event = deadEvent.getEvent();
        if (event instanceof EventInterface) {
            final EventInterface eventInterface = (EventInterface) event;
            eventInterface.getIO().emit("error", "\"" + eventInterface + "\"");
        }
        else System.out.println(deadEvent.getEvent());
    }

    // TODO use other virtual clients for testing
    public static void main(final String[] args) throws Exception {

        final int game = args.length > 0 ? Integer.parseInt(args[0]) : 6;

        final String host = "localhost";
        final int port = 8282;

        final WebSocketClientFactory factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();

        final Messages messages = new Messages();

        final WebSocketClient client = factory.newWebSocketClient();
        client.setMaxIdleTime(60000 * 5);
        client.setMaxTextMessageSize(1024 * 64);

        final VirtualClient virtualClient = new VirtualClient("virtual-client", client, host, port, "network/io");
        virtualClient.start();

        // connection
        final MessageInterface message1 = messages.newClient(virtualClient.getName());
        virtualClient.send(message1);

        // join game
        final MessageInterface message2 = messages.newGameConnection(game);
        virtualClient.send(message2);

        //virtualClient.stop();
        //factory.stop();
    }

}