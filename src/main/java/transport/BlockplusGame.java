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

import java.util.List;
import java.util.Set;

import serialization.GameContextRepresentation;
import transport.events.interfaces.ClientInterface;
import transport.events.interfaces.MoveSubmitInterface;
import blockplus.color.ColorInterface;
import blockplus.color.PrimeColors;
import blockplus.game.BlockplusGameContext;
import blockplus.game.BlockplusGameContextBuilder;
import blockplus.move.Move;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.player.PlayerInterface;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import components.position.Position;
import components.position.PositionInterface;

// TODO RoomContext et RoomContextBuilder
public class BlockplusGame implements GameInterface<BlockplusGameContext> {

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
    private final BlockplusGameContext gameContext;
    private final long timeStamp;

    private ImmutableMap<PlayerInterface, ClientInterface> clientByPlayer;
    private ImmutableMap<ClientInterface, PlayerInterface> playerByClient;

    public BlockplusGame(final int ordinal, final String code, final ImmutableList<ClientInterface> clients, final BlockplusGameContext gameContext,
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
            final Builder<PlayerInterface, ClientInterface> builder1 = new ImmutableMap.Builder<PlayerInterface, ClientInterface>();
            final Builder<ClientInterface, PlayerInterface> builder2 = new ImmutableMap.Builder<ClientInterface, PlayerInterface>();
            int n = 0;
            for (final ClientInterface client : clients) {
                final ColorInterface color = PrimeColors.get(this.SEQUENCE.get(n));
                final PlayerInterface player = gameContext.getPlayers().get(color);
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
    public BlockplusGameContext getApplication() {
        return this.gameContext;
    }

    @Override
    public boolean isEmpty() {
        return this.getClients().isEmpty();
    }

    @Override
    public boolean isFull() {
        return (this.getCapacity() - this.getClients().size()) == 0;
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
        final ColorInterface colorToPlay = this.getApplication().getColor();
        final PlayerInterface player = this.getApplication().getPlayers().get(colorToPlay);
        return this.clientByPlayer.get(player);
    }

    public PlayerInterface getPlayer(final ClientInterface client) {
        return this.playerByClient.get(client);
    }

    @Override
    public GameInterface<BlockplusGameContext> connect(final ClientInterface newClient) {
        final ImmutableList<ClientInterface> clients = new ImmutableList.Builder<ClientInterface>().addAll(this.getClients()).add(newClient).build();
        BlockplusGame newRoom = null;
        if (clients.size() == this.getCapacity()) {
            newRoom = new BlockplusGame(this.ordinal, computeCode(clients), clients, new BlockplusGameContextBuilder().build(), System.currentTimeMillis());
        }
        else {
            newRoom = new BlockplusGame(this.ordinal, this.code, clients, this.gameContext, this.timeStamp);
        }
        return newRoom;
    }

    public GameInterface<BlockplusGameContext> play(final MoveSubmitInterface moveSubmit) {

        // TODO check that move is from current player        
        // TODO check that game is not over
        // TODO check that move is legal                

        final BlockplusGameContext context = this.getApplication();

        // TODO à revoir
        final JsonArray array = moveSubmit.getPositions();
        final Set<PositionInterface> positions = Sets.newLinkedHashSet();
        for (final JsonElement jsonElement : array) {
            final JsonArray asJsonArray = jsonElement.getAsJsonArray();
            final int row = asJsonArray.get(0).getAsInt();
            final int column = asJsonArray.get(1).getAsInt();
            positions.add(Position.from(row, column));
        }

        final ColorInterface color = context.getColor();
        final PieceInterface piece = PieceComposite.from(moveSubmit.getId(), positions.iterator().next(), positions); // TODO à revoir
        final Move move = new Move(color, piece);

        BlockplusGameContext nextContext = context.apply(move);
        nextContext = nextContext.next();

        // TODO revoir la gestion du next player et du game-over !!
        List<Move> nextOptions = nextContext.options();
        final ImmutableSet<PositionInterface> emptySet = ImmutableSet.of();
        while (nextOptions.size() == 1 && nextOptions.iterator().next().isNull() && !nextContext.getColor().equals(color)) {
            final Move nullMove = new Move(nextContext.getColor(), PieceComposite.from(0, Position.from(), emptySet));
            nextContext = nextContext.apply(nullMove).next();
            nextOptions = nextContext.options();
        }
        if (nextContext.getColor().equals(color) && nextOptions.size() == 1 && nextOptions.iterator().next().isNull()) {
            final Move nullMove = new Move(nextContext.getColor(), PieceComposite.from(0, Position.from(), emptySet));
            nextContext = nextContext.apply(nullMove).next();
            nextOptions = nextContext.options();
        }

        return new BlockplusGame(this.getOrdinal(), this.getCode(), this.getClients(), nextContext, this.getTimeStamp());
    }

    @Override
    public GameInterface<BlockplusGameContext> disconnect(final ClientInterface client) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void update(final ClientInterface client) {
        final BlockplusGameContext context = this.getApplication();
        final GameContextRepresentation gameRepresentation = new GameContextRepresentation(context);
        client.getIO().emit("color", gameRepresentation.encodeColor(this.getPlayer(client).getColor()));
        client.getIO().emit("update", gameRepresentation.toString());
    }

    public void update() {
        final BlockplusGameContext context = this.getApplication();
        final GameContextRepresentation gameRepresentation = new GameContextRepresentation(context);
        for (final ClientInterface client : this.getClients()) {
            client.getIO().emit("color", gameRepresentation.encodeColor(this.getPlayer(client).getColor()));
            client.getIO().emit("update", gameRepresentation.toString());
        }
    }

    @Override
    public String toJson() {
        final BlockplusGameContext context = this.getApplication();
        final GameContextRepresentation gameRepresentation = new GameContextRepresentation(context);
        return gameRepresentation.encodeBoard().toString();
    }

}