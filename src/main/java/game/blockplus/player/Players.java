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

package game.blockplus.player;

import game.blockplus.context.Color;
import game.blockplus.polyomino.Polyomino;
import game.interfaces.IPlayers;

import java.util.Map;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class Players implements IPlayers<Color> {

    private final static Map<Color, Player> EMPTY = Maps.newHashMap();

    public final static class Builder {

        private final Map<Color, Player> alivePlayers = Maps.newTreeMap();

        public Builder add(final Color color, final Player player) {
            this.alivePlayers.put(color, player);
            return this;
        }

        public Players build() {
            Preconditions.checkState(this.alivePlayers.size() == 4); // TODO use adversity
            return new Players(this.alivePlayers, EMPTY);
        }
    }

    private final Map<Color, Player> alivePlayers;

    private final Map<Color, Player> deadPlayers;

    private Players(final Map<Color, Player> alivePlayers, final Map<Color, Player> deadPlayers) {
        this.alivePlayers = alivePlayers;
        this.deadPlayers = deadPlayers;
    }

    @Override
    public Player getAlivePlayer(final Color color) {
        return this.alivePlayers.get(color);
    }

    @Override
    public Player getDeadOrAlivePlayer(final Color color) {
        final Player alivePlayer = this.getAlivePlayer(color);
        return alivePlayer == null ? this.deadPlayers.get(color) : alivePlayer;
    }

    @Override
    public boolean hasAlivePlayer() {
        return !this.alivePlayers.isEmpty();
    }

    @Override
    public Players apply(final Color color, final Polyomino polyomino) {
        final Player player = this.alivePlayers.get(color);
        final Map<Color, Player> alivePlayers = Maps.newHashMap(this.alivePlayers);
        if (polyomino == null) {
            final Map<Color, Player> deadPlayers = Maps.newHashMap(this.deadPlayers);
            alivePlayers.remove(color);
            deadPlayers.put(color, player);
            return new Players(alivePlayers, deadPlayers);
        }
        final Player newPlayer = player.apply(polyomino);
        alivePlayers.put(color, newPlayer);
        return new Players(alivePlayers, this.deadPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.alivePlayers, this.deadPlayers);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof Players);
        final Players that = (Players) object;
        return Equivalences.equals().equivalent(this.alivePlayers, that.alivePlayers)
                && Equivalences.equals().equivalent(this.deadPlayers, that.deadPlayers);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("alive", this.alivePlayers)
                .add("dead", this.deadPlayers)
                .toString();
    }

}