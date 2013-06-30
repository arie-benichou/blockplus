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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

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
import transport.events.interfaces.DisconnectInterface;
import transport.events.interfaces.EventInterface;
import transport.events.interfaces.FeedbackInterface;
import transport.events.interfaces.InPatioInterface;
import transport.events.interfaces.ShowGameInterface;
import transport.messages.Messages;
import transport.protocol.MessageDecoder;
import transport.protocol.MessageHandler;
import transport.protocol.MessageHandlerInterface;
import transport.protocol.MessageInterface;
import blockplus.context.Context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;

// TODO : non réellement serializable (les champs d'instance ne le sont pas).
// Est ce un probleme pour plus tard ?
@SuppressWarnings("serial")
public class BlockplusServer extends WebSocketServlet {

    private final MessageHandlerInterface messageHandler = new MessageHandler(); // TODO à injecter
    private final MessageDecoder messageDecoder = new MessageDecoder(); // TODO à injecter

    private final Map<IOinterface, ClientInterface> clientByIO = Maps.newConcurrentMap();

    public final ConcurrentLinkedDeque<IOinterface> clientsInPatio = new ConcurrentLinkedDeque<IOinterface>();

    public void updateClients(final IOinterface io, final ClientInterface user) {
        this.clientByIO.put(io, user);
    }

    public void removeFromClients(final IOinterface io) {
        this.clientByIO.remove(io);
    }

    // TODO utiliser un Futur<GameInterface<BlockplusGameContext>>
    public final Map<Integer, GameInterface<Context>> gameByOrdinal = Maps.newConcurrentMap();

    public GameInterface<Context> getGame(final Integer ordinal) {
        return this.gameByOrdinal.get(ordinal);
    }

    public void updateGames(final Integer ordinal, final GameInterface<Context> newGame) {
        this.gameByOrdinal.put(ordinal, newGame);
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

        for (int i = 1; i <= 25; ++i) {
            final ImmutableList<ClientInterface> empty = ImmutableList.of();
            this.clientsByGame.put(i, empty);
            this.gameByOrdinal.put(i, new BlockplusGame(i, "", empty, null, 0));
        }

    }

    @Override
    public WebSocket doWebSocketConnect(final HttpServletRequest request, final String protocol) {
        return new IO(this);
    }

    public void connect(final ClientInterface user) {
        this.clientByIO.put(user.getIO(), user);
    }

    public void disconnect(final IOinterface io) {
        this.clientByIO.remove(io);
        this.clientsInPatio.remove(io);
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

    /*
    private List<Integer> getGames() {
        final Set<Integer> keySet = this.gameByOrdinal.keySet();
        final ArrayList<Integer> games = Lists.newArrayList(keySet);
        Collections.sort(games);
        return games;
    }
    */

    @Subscribe
    @AllowConcurrentEvents
    // TODO à revoir
    public void onNewClient(final ClientInterface newClient) {
        this.connect(newClient);
        /*
        final String game = new Gson().toJson(this.getGames());
        newClient.getIO().emit("games", "\"" + game + "\"");
        */
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onDisconnect(final DisconnectInterface client) {
        System.out.println("disconnecting " + client);
        this.disconnect(client.getIO());
        /*
        final JsonObject tables = this.tables();
        System.out.println(this.clientsInPatio.size() + " clients dans le patio");
        System.out.println(this.clientsInPatio.iterator().hasNext());
        System.out.println(this.tables().toString());
        for (final IOinterface other : this.clientsInPatio) {
            System.out.println(other.getGame());
            other.emit("tables", tables.toString());
        }
        */
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onInPatio(final InPatioInterface inPatio) { // TODO trier les tables
        final IOinterface io = inPatio.getIO();
        final JsonObject tables = this.tables();
        io.emit("tables", tables.toString());
        for (final IOinterface other : this.clientsInPatio) {
            other.emit("tables", tables.toString());
        }
        this.clientsInPatio.add(io);
        System.out.println(this.clientsInPatio);
        final Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("beep -f 200 -l 180");
            Thread.sleep(190);
            runtime.exec("beep -f 400 -l 200");
            Thread.sleep(210);
            runtime.exec("beep -f 600 -l 250");
            Thread.sleep(260);
            runtime.exec("beep -f 800 -l 300");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private BlockplusGame reset(final GameInterface<Context> game) {
        final ImmutableList<ClientInterface> empty = ImmutableList.of();
        this.updateGame(game.getOrdinal(), empty);
        final BlockplusGame newGame = new BlockplusGame(game.getOrdinal(), "", empty, null, 0);
        this.updateGames(game.getOrdinal(), newGame);
        return newGame;
    }

    public JsonObject tables() {
        final JsonObject tables = new JsonObject();
        for (final GameInterface<Context> game : this.gameByOrdinal.values()) {
            if (game.isFull()) {
                boolean isAlive = false;
                for (final ClientInterface client : game.getClients()) {
                    final boolean isOpen = client.getIO().getConnection().isOpen();
                    if (!isOpen) this.disconnect(client.getIO());
                    isAlive = isAlive || isOpen;
                }
                if (!isAlive) {
                    tables.addProperty("" + this.reset(game).getOrdinal(), this.reset(game).getClients().size());
                }
            }
            else {
                boolean isFullyAlive = true;
                boolean isFullyDead = true;
                for (final ClientInterface client : game.getClients()) {
                    final boolean isOpen = client.getIO().getConnection().isOpen();
                    //if (!isOpen) this.disconnect(client.getIO());
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

        /*
        final Runtime runtime = Runtime.getRuntime();
        runtime.exec("beep -f 200 -l 180");
        Thread.sleep(190);
        runtime.exec("beep -f 400 -l 200");
        Thread.sleep(210);
        runtime.exec("beep -f 600 -l 250");
        Thread.sleep(260);
        runtime.exec("beep -f 800 -l 300");
        */

        final int game = args.length > 0 ? Integer.parseInt(args[0]) : 6;

        final String host = "localhost";
        final int port = 8282;

        final WebSocketClientFactory factory = new WebSocketClientFactory();
        factory.setBufferSize(4096);
        factory.start();

        final Messages messages = new Messages();

        final WebSocketClient client = factory.newWebSocketClient();
        client.setMaxIdleTime(60 * 1000 * 5);
        //client.setMaxIdleTime(4 * (60 + 1) * 1000);
        client.setMaxTextMessageSize(1024 * 64);

        final VirtualClient virtualClient = new VirtualClient("virtual-client", client, host, port, "io");
        virtualClient.start();

        //Thread.sleep(1000);

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