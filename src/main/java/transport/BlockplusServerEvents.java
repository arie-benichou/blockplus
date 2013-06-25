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

import transport.events.Client;
import transport.events.interfaces.ClientInterface;
import transport.events.interfaces.GameConnectionInterface;
import transport.events.interfaces.GameReconnectionInterface;
import transport.events.interfaces.MoveSubmitInterface;
import blockplus.context.Context;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
        if (game.isFull()) {
            gameConnection.getIO().emit("info", "\"" + "Game " + game.getOrdinal() + " is full" + "\""); // TODO revoir emit
            gameConnection.getIO().emit("fullGame", "\"" + game.getOrdinal() + "\"");
        }
        else {
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
                client.getIO().emit("info", "\"" + newClient.getName() + " has joined game " + newGame.getOrdinal() + "\""); // TODO revoir emit
                client.getIO().emit("player", playerInfo.toString());
            }

            try {
                Thread.sleep(750);
            }
            catch (final InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (newGame.isFull()) {
                int k = 0;
                for (final ClientInterface client : newGame.getClients()) {
                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.add("game", new JsonPrimitive(newGame.getOrdinal()));
                    jsonObject.add("code", new JsonPrimitive(newGame.getCode()));
                    jsonObject.add("time", new JsonPrimitive(newGame.getTimeStamp()));
                    jsonObject.add("name", new JsonPrimitive(client.getName()));
                    jsonObject.add("color", new JsonPrimitive(++k));
                    jsonObject.add("client", new JsonPrimitive(client.hashCode()));
                    //TODO ajouter le timeStamp de connexion et l'ip du client
                    //client.getIO().emit("link", jsonObject.toString());
                }
                newGame.update();
            }
            else {
                // TODO replace quick & dirty patch by a virtual client factory
                try {
                    BlockplusServer.main(new String[] { newGame.getOrdinal().toString() });
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onMoveSubmit(final MoveSubmitInterface moveSubmit) {
        /*
            try {
                Thread.sleep(3000);
            }
            catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            */
        final ClientInterface client = this.getServer().getClient(moveSubmit.getIO());
        final Integer game = client.getGame();
        final BlockplusGame blockplusGame = (BlockplusGame) this.getServer().getGame(game);
        final BlockplusGame newGame = (BlockplusGame) blockplusGame.play(moveSubmit);
        this.getServer().updateGames(newGame.getOrdinal(), newGame);
        newGame.update();
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
                            final transport.events.Client newClient = new transport.events.Client(gameReconnection.getIO(), name, ordinal);
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