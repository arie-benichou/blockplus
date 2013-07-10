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

package blockplus.model.context;

import blockplus.model.adversity.IAdversity;
import blockplus.model.move.IMove;
import blockplus.model.option.IOptionsSupplier;
import blockplus.model.player.IPlayers;

import com.google.common.collect.Table;

/**
 * Context interface for a game.
 * 
 * @param <T>
 *            type used by the sides of this context
 */
public interface IContext<T> {

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
    IAdversity<T> getAdversity();

    /**
     * Returns players in this context.
     * 
     * @return players in this context
     */
    IPlayers<T> getPlayers();

    // TODO ? delegate to Referee interface
    /**
     * Returns the effective successor of this context.
     * 
     * @return the effective successor of this context
     */
    IContext<T> forward();

    /**
     * Returns options in this context for the current side to play.
     * 
     * @return options in this context for the current side to play
     */
    Table<?, ?, ?> options(); // TODO Options class

    /**
     * Applies a given move in this context.
     * 
     * @param move
     *            a given move.
     * 
     * @return a new instance of context
     */
    IContext<T> apply(IMove moveInterface);

    IOptionsSupplier getOptionsSupplier();

    Iterable<T> sides();

}