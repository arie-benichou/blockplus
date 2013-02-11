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

package interfaces.player;

/**
 * Players interface for a game.
 * 
 * @param <T>
 *            type used by players id
 */
public interface PlayersInterface<T> {

    /**
     * Returns true if at least one player is still alive, false if all players
     * are dead.
     * 
     * @return true if at least one player is still alive, false if all players
     *         are dead
     */
    boolean hasAlivePlayer();

    /**
     * Returns a player for a given id.
     * 
     * @param id
     *            player id
     * 
     * @return a player for a given id
     */
    PlayerInterface get(T id);

    /**
     * Update old player instance in thoses players with a given new player
     * instance.
     * 
     * @param playerInterface
     *            a given new player instance
     * 
     * @return a new instance of players
     */
    PlayersInterface<T> apply(PlayerInterface playerInterface);

}