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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import blockplus.color.ColorInterface;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class Players implements PlayersInterface {

    public final static class Builder {

        private final Set<PlayerInterface> players = Sets.newHashSet();

        public Builder add(final PlayerInterface player) {
            this.players.add(player);
            return this;
        }

        public Players build() {
            return new Players(this.players);
        }

    }

    private final Set<PlayerInterface> players;
    private final Map<ColorInterface, PlayerInterface> playerByColor = Maps.newHashMap();

    private Players(final Set<PlayerInterface> players) {
        Preconditions.checkState(players.size() == 4);
        this.players = ImmutableSet.copyOf(players);
        for (final PlayerInterface player : this.players) {
            this.playerByColor.put(player.getColor(), player);
        }
    }

    @Override
    public Iterator<PlayerInterface> iterator() {
        return this.players.iterator();
    }

    @Override
    public PlayerInterface get(final ColorInterface color) {
        return this.playerByColor.get(color);
    }

    @Override
    public Players update(final PlayerInterface newPlayer) {
        Preconditions.checkArgument(newPlayer != null);
        if (this.players.contains(newPlayer)) return this;
        final ColorInterface color = newPlayer.getColor();
        final Builder builder = new Players.Builder();
        for (final PlayerInterface player : this) {
            if (!player.getColor().equals(color)) builder.add(player);
        }
        builder.add(newPlayer);
        return builder.build();
    }

    // TODO compute boolean in builder
    public boolean hasAlivePlayer() {
        for (final PlayerInterface player : this) {
            if (player.isAlive()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.players)
                .toString();
    }

}