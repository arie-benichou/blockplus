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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.transport.events.Client;
import blockplus.transport.events.interfaces.ClientInterface;
import blockplus.transport.events.interfaces.GameConnectionInterface;
import blockplus.transport.events.interfaces.GameReconnectionInterface;
import blockplus.transport.events.interfaces.MoveSubmitInterface;
import blockplus.transport.events.interfaces.NotificationInterface;
import blockplus.transport.events.interfaces.VirtualPlayerConnectionInterface;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;

public class BlockplusServerEvents {

    private final BlockplusServer server;

    public BlockplusServer getServer() {
        return this.server;
    }

    public BlockplusServerEvents(final BlockplusServer server) {
        this.server = server;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onGameConnection(final GameConnectionInterface gameConnection) {
        final GameInterface<Context> game = this.getServer().getGame(gameConnection.getOrdinal());
        if (!game.isFull()) {
            final ClientInterface oldClient = this.getServer().getClient(gameConnection.getIO());
            final ClientInterface newClient = new Client(gameConnection.getIO(), oldClient.getName(), game.getOrdinal());
            System.out.println();
            System.out.println(newClient.getName());
            System.out.println(game.getOrdinal());
            System.out.println();
            this.getServer().updateClients(newClient.getIO(), newClient);

            final BlockplusGame newGame = (BlockplusGame) game.connect(newClient);

            final ImmutableList<ClientInterface> clients = newGame.getClients();
            this.getServer().updateGame(newGame.getOrdinal(), clients);
            this.getServer().updateGames(newGame.getOrdinal(), newGame);

            final JsonObject gameInfo = new JsonObject();
            gameInfo.addProperty("id", newGame.getOrdinal());
            gameInfo.addProperty("players", clients.size());

            // TODO revoir emit
            newClient.getIO().emit("game", gameInfo.toString());

            final JsonObject playerInfo = new JsonObject();
            playerInfo.addProperty("name", newClient.getName());

            for (final ClientInterface client : clients) {
                client.getIO().emit("player", playerInfo.toString());
            }
            this.getServer().clientsInPatio.remove(newClient.getIO());
            final ConcurrentLinkedDeque<IOinterface> patio = this.getServer().clientsInPatio;
            for (final IOinterface io : patio) {
                io.emit("tables", this.getServer().tables().toString());
            }
            if (newGame.isFull()) {
                newGame.update();
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onVirtualPlayerConnection(final VirtualPlayerConnectionInterface virtualPlayerConnection) {
        try {
            BlockplusServer.runVC(new String[] { virtualPlayerConnection.getOrdinal().toString() }); // TODO
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onMoveSubmit(final MoveSubmitInterface moveSubmit) {
        final ClientInterface client = this.getServer().getClient(moveSubmit.getIO());
        final Integer game = client.getGame();
        final BlockplusGame blockplusGame = (BlockplusGame) this.getServer().getGame(game);
        final BlockplusGame newGame = (BlockplusGame) blockplusGame.play(moveSubmit);
        this.getServer().updateGames(newGame.getOrdinal(), newGame);
        newGame.update();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onNotification(final NotificationInterface notificationInterface) {
        final ClientInterface client = this.getServer().getClient(notificationInterface.getIO());
        final Integer game = client.getGame();
        final BlockplusGame blockplusGame = (BlockplusGame) this.getServer().getGame(game);
        final Colors from = Colors.valueOf(notificationInterface.getFrom());
        final Colors to = Colors.valueOf(notificationInterface.getTo());
        final String message = notificationInterface.getMessage();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("from", from.toString());
        jsonObject.addProperty("to", to.toString());
        System.out.println("##############################");
        //System.out.println(from);
        //System.out.println(to);
        //System.out.println(message);
        System.out.println(jsonObject.toString());
        final ClientInterface toClient = blockplusGame.getPlayer(to);
        toClient.getIO().emit("notification", jsonObject.toString());
        System.out.println("##############################");
        //final BlockplusGame newGame = (BlockplusGame) blockplusGame.play(moveSubmit);
        //this.getServer().updateGames(newGame.getOrdinal(), newGame);
        //newGame.update();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onGameReconnection(final GameReconnectionInterface gameReconnection) {

        final JsonObject link = gameReconnection.getLink();

        final String name = link.get("name").getAsString();
        final Integer ordinal = link.get("game").getAsInt();
        final Integer colorIndex = link.get("color").getAsInt();
        final String code = link.get("code").getAsString();
        final int client = link.get("client").getAsInt();
        final long timeStamp = link.get("time").getAsLong();

        final GameInterface<Context> game = this.getServer().getGame(ordinal);

        if (game != null) {
            if (game.getCode().equals(code)) {
                final ClientInterface gameUser = game.getClients().get(colorIndex - 1);
                if (gameUser.getName().equals(name)) {
                    if (gameUser.hashCode() == client) {
                        if (game.getTimeStamp() == timeStamp) {
                            final ArrayList<ClientInterface> newUsers = Lists.newArrayList(game.getClients());
                            final blockplus.transport.events.Client newClient = new blockplus.transport.events.Client(gameReconnection.getIO(), name, ordinal);
                            newUsers.set(colorIndex - 1, newClient);
                            final IOinterface oldIo = game.getClients().get(colorIndex - 1).getIO();
                            oldIo.getConnection().close();
                            final BlockplusGame newGame =
                                                          new BlockplusGame(ordinal, code, ImmutableList.copyOf(newUsers), game.getContext(),
                                                                  game.getTimeStamp());
                            this.getServer().updateGames(ordinal, newGame);
                            this.getServer().updateClients(gameReconnection.getIO(), newClient);
                            this.getServer().removeFromClients(oldIo);

                            link.addProperty("client", newClient.hashCode());
                            newClient.getIO().emit("link", link.toString());

                            newGame.update(newClient);
                        }
                    }
                }
            }
        }
    }
}