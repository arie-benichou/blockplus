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

package blockplus.model;


import java.util.Map;

import blockplus.model.entity.Polyomino;
import blockplus.model.interfaces.IPlayers;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class Sides implements IPlayers<Colors> {

    private final static Map<Colors, Side> EMPTY = Maps.newHashMap();

    public final static class Builder {

        private final Map<Colors, Side> alivePlayers = Maps.newTreeMap();

        public Builder add(final Colors color, final Side player) {
            this.alivePlayers.put(color, player);
            return this;
        }

        public Sides build() {
            Preconditions.checkState(this.alivePlayers.size() == 4); // TODO use adversity
            return new Sides(this.alivePlayers, EMPTY);
        }
    }

    private final Map<Colors, Side> alivePlayers;

    private final Map<Colors, Side> deadPlayers;

    private Sides(final Map<Colors, Side> alivePlayers, final Map<Colors, Side> deadPlayers) {
        this.alivePlayers = alivePlayers;
        this.deadPlayers = deadPlayers;
    }

    public Side getAlivePlayer(final Colors color) {
        return this.alivePlayers.get(color);
    }

    public Side getDeadOrAlivePlayer(final Colors color) {
        final Side alivePlayer = this.getAlivePlayer(color);
        return alivePlayer == null ? this.deadPlayers.get(color) : alivePlayer;
    }

    @Override
    public boolean hasAlivePlayer() {
        return !this.alivePlayers.isEmpty();
    }

    public Sides apply(final Colors color, final Polyomino polyomino) {
        final Side player = this.alivePlayers.get(color);
        final Map<Colors, Side> alivePlayers = Maps.newHashMap(this.alivePlayers);
        if (polyomino == null) {
            final Map<Colors, Side> deadPlayers = Maps.newHashMap(this.deadPlayers);
            alivePlayers.remove(color);
            deadPlayers.put(color, player);
            return new Sides(alivePlayers, deadPlayers);
        }
        final Side newPlayer = player.apply(polyomino);
        alivePlayers.put(color, newPlayer);
        return new Sides(alivePlayers, this.deadPlayers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.alivePlayers, this.deadPlayers);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof Sides);
        final Sides that = (Sides) object;
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