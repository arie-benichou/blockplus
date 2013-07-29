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

import static components.cells.Positions.Position;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import blockplus.export.ContextRepresentation;
import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.model.Context.Builder;
import blockplus.model.Move;
import blockplus.transport.events.interfaces.IClient;
import blockplus.transport.events.interfaces.IMoveSubmit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import components.cells.IPosition;

public class BlockplusGame implements IGame<Context> {

    private final static List<Colors> COLORS = Lists.newArrayList(Colors.set());

    private final int ordinal;
    private final ImmutableList<IClient> clients;
    private final Context context;

    private final ImmutableMap<Colors, IClient> clientByColor;

    public BlockplusGame(final int ordinal, final ImmutableList<IClient> clients, final Context context) {
        this.ordinal = ordinal;
        this.clients = clients;
        this.context = context;
        final Iterator<Colors> iterator = COLORS.iterator();
        final ImmutableMap.Builder<Colors, IClient> builder = new ImmutableMap.Builder<Colors, IClient>();
        for (final IClient client : clients)
            builder.put(iterator.next(), client);
        this.clientByColor = builder.build();
    }

    @Override
    public Integer getOrdinal() {
        return this.ordinal;
    }

    @Override
    public ImmutableList<IClient> getClients() {
        return this.clients;
    }

    @Override
    public Context getContext() {
        return this.context;
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
    public IGame<Context> connect(final IClient newClient) {
        final ImmutableList<IClient> clients = new ImmutableList.Builder<IClient>().addAll(this.getClients()).add(newClient).build();
        BlockplusGame newGame = null;
        if (clients.size() == this.getCapacity()) {
            newGame = new BlockplusGame(this.ordinal, clients, new Builder().build());
        }
        else {
            newGame = new BlockplusGame(this.ordinal, clients, this.context);
        }
        return newGame;
    }

    @Override
    public void update() {
        final Context context = this.getContext();
        final ContextRepresentation gameRepresentation = new ContextRepresentation(context);
        for (final IClient client : this.getClients()) {
            client.getEndpoint().emit("update", gameRepresentation.toString());
        }
    }

    public IGame<Context> play(final IMoveSubmit moveSubmitInterface) {
        final Context context = this.getContext();
        final SortedSet<IPosition> positions = Sets.newTreeSet();
        for (final JsonElement element : moveSubmitInterface.getPositions()) {
            final int id = element.getAsInt();
            final IPosition position = Position(id / 20, id % 20); // TODO !!!
            positions.add(position);
        }
        final Move move = new Move(context.side(), positions);
        return new BlockplusGame(this.getOrdinal(), this.getClients(), context.apply(move).forward());
    }

    public IClient getPlayer(final Colors color) {
        return this.clientByColor.get(color);
    }

}