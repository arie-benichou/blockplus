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

import java.util.List;
import java.util.SortedSet;

import blockplus.export.ContextRepresentation;
import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.model.Context.Builder;
import blockplus.model.Move;
import blockplus.model.Side;
import blockplus.model.polyomino.PolyominoProperties;
import blockplus.model.polyomino.PolyominoProperties.Location;
import blockplus.transport.events.interfaces.ClientInterface;
import blockplus.transport.events.interfaces.MoveSubmitInterface;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import components.cells.IPosition;

public class BlockplusGame implements GameInterface<Context> {

    private static String computeCode(final ImmutableList<ClientInterface> clients) {
        final List<Integer> parts = Lists.newArrayList();
        for (final ClientInterface client : clients) {
            parts.add(client.getIO().hashCode());
        }
        return Joiner.on(':').join(parts);
    }

    // TODO à revoir
    private final ImmutableList<String> SEQUENCE = ImmutableList.of("Blue", "Yellow", "Red", "Green");

    private final int ordinal;
    private final String code;
    private final ImmutableList<ClientInterface> clients;
    private final Context gameContext;
    private final long timeStamp;

    private ImmutableMap<Side, ClientInterface> clientByPlayer;
    private ImmutableMap<ClientInterface, Side> playerByClient;

    public BlockplusGame(final int ordinal, final String code, final ImmutableList<ClientInterface> clients, final Context gameContext,
            final long timeStamp) {

        this.ordinal = ordinal;
        this.code = code;
        this.timeStamp = timeStamp;
        this.clients = clients;
        this.gameContext = gameContext;

        // TODO ! Builder de game
        if (this.gameContext == null) {
            this.clientByPlayer = null;
        }
        else {
            final ImmutableMap.Builder<Side, ClientInterface> builder1 = new ImmutableMap.Builder<Side, ClientInterface>();
            final ImmutableMap.Builder<ClientInterface, Side> builder2 = new ImmutableMap.Builder<ClientInterface, Side>();
            int n = 0;
            for (final ClientInterface client : clients) {
                final Colors color = Colors.valueOf(this.SEQUENCE.get(n));
                final Side player = gameContext.players().getDeadOrAlivePlayer(color);
                builder1.put(player, client);
                builder2.put(client, player);
                ++n;
            }
            this.clientByPlayer = builder1.build();
            this.playerByClient = builder2.build();
        }
    }

    @Override
    public Integer getOrdinal() {
        return this.ordinal;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public ImmutableList<ClientInterface> getClients() {
        return this.clients;
    }

    @Override
    public Context getContext() {
        return this.gameContext;
    }

    @Override
    public boolean isEmpty() {
        return this.getClients().isEmpty();
    }

    @Override
    public boolean isFull() {
        return this.getCapacity() - this.getClients().size() == 0;
    }

    @Override
    public int getCapacity() {
        return 4; // TODO extract constant
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ordinal", this.ordinal)
                .add("code", this.code)
                .toString();
    }

    public ClientInterface getUserToPlay() {
        final Colors colorToPlay = this.getContext().side();
        final Side player = this.getContext().players().getAlivePlayer(colorToPlay);
        return this.clientByPlayer.get(player);
    }

    public Side getPlayer(final ClientInterface client) {
        return this.playerByClient.get(client);
    }

    @Override
    public GameInterface<Context> connect(final ClientInterface newClient) {
        final ImmutableList<ClientInterface> clients = new ImmutableList.Builder<ClientInterface>().addAll(this.getClients()).add(newClient).build();
        BlockplusGame newRoom = null;
        if (clients.size() == this.getCapacity()) {
            newRoom = new BlockplusGame(this.ordinal, computeCode(clients), clients, new Builder().build(), System.currentTimeMillis());
        }
        else {
            newRoom = new BlockplusGame(this.ordinal, this.code, clients, this.gameContext, this.timeStamp);
        }
        return newRoom;
    }

    public GameInterface<Context> play(final MoveSubmitInterface moveSubmitInterface) {
        final Context context = this.getContext();
        final SortedSet<IPosition> positions = Sets.newTreeSet();
        for (final JsonElement element : moveSubmitInterface.getPositions()) {
            final int id = element.getAsInt();
            final Location position = new PolyominoProperties.Location(id / 20, id % 20); // TODO !!!
            positions.add(position);
        }
        final Move move = new Move(context.side(), positions);
        Context nextContext = context.apply(move);
        nextContext = nextContext.forward();
        return new BlockplusGame(this.getOrdinal(), this.getCode(), this.getClients(), nextContext, this.getTimeStamp());
    }

    @Override
    public GameInterface<Context> disconnect(final ClientInterface client) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void update(final ClientInterface client) {
        final Context context = this.getContext();
        final ContextRepresentation gameRepresentation = new ContextRepresentation(context);
        client.getIO().emit("update", gameRepresentation.toString());
    }

    public void update() {
        final Context context = this.getContext();
        final ContextRepresentation gameRepresentation = new ContextRepresentation(context);
        for (final ClientInterface client : this.getClients()) {
            client.getIO().emit("update", gameRepresentation.toString());
        }
    }

    @Override
    public String toJson() {
        final Context context = this.getContext();
        final ContextRepresentation gameRepresentation = new ContextRepresentation(context);
        return gameRepresentation.encodeBoard().toString();
    }

}