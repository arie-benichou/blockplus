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

import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.transport.events.Client;
import blockplus.transport.events.interfaces.IClient;
import blockplus.transport.events.interfaces.IGameConnection;
import blockplus.transport.events.interfaces.IMoveSubmit;
import blockplus.transport.events.interfaces.INotification;
import blockplus.transport.events.interfaces.IVirtualPlayerConnection;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;

public class BlockplusServerEvents {

    private final BlockplusServer server;

    private BlockplusServer getServer() {
        return this.server;
    }

    public BlockplusServerEvents(final BlockplusServer server) {
        this.server = server;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onGameConnection(final IGameConnection gameConnection) {
        final GameInterface<Context> game = this.getServer().getGame(gameConnection.getOrdinal());
        if (!game.isFull()) {
            final IClient oldClient = this.getServer().getClientByEndpoint(gameConnection.getEndpoint());
            final IClient newClient = new Client(gameConnection.getEndpoint(), oldClient.getName(), game.getOrdinal());
            this.getServer().updateClients(newClient.getEndpoint(), newClient);

            final BlockplusGame newGame = (BlockplusGame) game.connect(newClient);

            final ImmutableList<IClient> clients = newGame.getClients();
            this.getServer().updateGame(newGame.getOrdinal(), newGame);

            final JsonObject gameInfo = new JsonObject();
            gameInfo.addProperty("id", newGame.getOrdinal());
            gameInfo.addProperty("players", clients.size());

            newClient.getEndpoint().emit("game", gameInfo.toString());

            final JsonObject playerInfo = new JsonObject();
            playerInfo.addProperty("name", newClient.getName());

            for (final IClient client : clients) {
                client.getEndpoint().emit("player", playerInfo.toString());
            }
            this.getServer().removeFromPatio(newClient.getEndpoint());
            for (final IEndPoint endPoint : this.getServer().getEndpointsInPatio()) {
                endPoint.emit("tables", this.getServer().games().toString());
            }
            if (newGame.isFull()) newGame.update();
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onVirtualPlayerConnection(final IVirtualPlayerConnection virtualPlayerConnection) {
        try {
            BlockplusServer.runVC(new String[] { virtualPlayerConnection.getOrdinal().toString() }); // TODO
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onMoveSubmit(final IMoveSubmit moveSubmit) {
        final IClient client = this.getServer().getClientByEndpoint(moveSubmit.getEndpoint());
        final Integer game = client.getGame();
        final BlockplusGame blockplusGame = (BlockplusGame) this.getServer().getGame(game);
        final BlockplusGame newGame = (BlockplusGame) blockplusGame.play(moveSubmit);
        this.getServer().updateGame(newGame.getOrdinal(), newGame);
        newGame.update();
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onNotification(final INotification notificationInterface) {
        final IClient client = this.getServer().getClientByEndpoint(notificationInterface.getEndpoint());
        final Integer game = client.getGame();
        final BlockplusGame blockplusGame = (BlockplusGame) this.getServer().getGame(game);
        final Colors from = Colors.valueOf(notificationInterface.getFrom());
        final Colors to = Colors.valueOf(notificationInterface.getTo());
        final String message = notificationInterface.getMessage();
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("from", from.toString());
        jsonObject.addProperty("to", to.toString());
        final IClient toClient = blockplusGame.getPlayer(to);
        toClient.getEndpoint().emit("notification", jsonObject.toString());
    }

}