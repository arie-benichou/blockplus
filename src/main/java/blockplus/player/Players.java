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

package blockplus.player;

import interfaces.player.PlayerInterface;
import interfaces.player.PlayersInterface;

import java.util.Iterator;

import blockplus.Color;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

public final class Players implements PlayersInterface<Color>, Iterable<Player> {

    public final static class Builder {

        private final ImmutableSortedMap.Builder<Color, Player> playerByColorBuilder = new ImmutableSortedMap.Builder<Color, Player>(Ordering.natural());

        public Builder add(final Player player) {
            this.playerByColorBuilder.put(player.getColor(), player);
            return this;
        }

        public Players build() {
            final ImmutableSortedMap<Color, Player> players = this.playerByColorBuilder.build();
            Preconditions.checkState(players.size() == 4);
            return new Players(players);
        }

    }

    @Override
    public Iterator<Player> iterator() {
        return this.playerByColor.values().iterator();
    }

    private final ImmutableSortedMap<Color, Player> playerByColor;

    @Override
    public Player get(final Color color) {
        return this.playerByColor.get(color);
    }

    private Players(final ImmutableSortedMap<Color, Player> playerByColor) {
        this.playerByColor = playerByColor;
    }

    @Override
    public Players apply(final PlayerInterface playerInterface) {
        Preconditions.checkArgument(playerInterface instanceof Player);
        final Player newPlayer = (Player) playerInterface;
        final Builder builder = new Players.Builder();
        for (final Player player : this)
            if (!player.getColor().equals(newPlayer.getColor())) builder.add(player);
        return builder.add(newPlayer).build();
    }

    @Override
    public boolean hasAlivePlayer() { // TODO Scala lazy
        for (final PlayerInterface player : this) {
            if (player.isAlive()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.playerByColor).toString();
    }

}