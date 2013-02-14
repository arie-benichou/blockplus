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

package interfaces.context;

import interfaces.adversity.AdversityInterface;
import interfaces.board.BoardInterface;
import interfaces.move.MoveInterface;
import interfaces.player.PlayersInterface;

import java.util.List;

/**
 * Context interface for a game.
 * 
 * @param <T>
 *            type used by the sides of this context
 */
public interface ContextInterface<T> {

    /**
     * Returns true if this context is a leaf, false otherwise.
     * 
     * @return true if this context is a leaf, false otherwise
     */
    // TODO ? delegate to Referee interface
    boolean isTerminal();

    /**
     * Returns the current side to play.
     * 
     * @return the current side to play
     */
    T getSide();

    /**
     * Returns the defined (but not necessary effective) successor of the
     * current side to play.
     * 
     * @return the defined (but not necessary effective) successor of the
     *         current side to play.
     */
    T getNextSide();

    /**
     * Returns the adversity defined in this context.
     * 
     * @return the adversity defined in this context
     */
    AdversityInterface<T> getAdversity();

    /**
     * Returns the board in this context.
     * 
     * @return the board in this context
     */
    BoardInterface getBoard();

    /**
     * Returns players in this context.
     * 
     * @return players in this context
     */
    PlayersInterface<T> getPlayers();

    // TODO ? delegate to Referee interface
    /**
     * Returns the effective successor of this context.
     * 
     * @return the effective successor of this context
     */
    ContextInterface<T> forward();

    /**
     * Returns options in this context for the current side to play.
     * 
     * @return options in this context for the current side to play
     */
    List<MoveInterface> options();

    /**
     * Applies a given move in this context.
     * 
     * @param move
     *            a given move.
     * 
     * @return a new instance of context
     */
    ContextInterface<T> apply(MoveInterface move);

}