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

package blockplus.board.layer;

import components.board.Symbol;

/**
 * Possible states for a cell in a board's layer of a Blokus game.Each Blokus
 * color is mapped on its own layer, so a board manages 4 layers of colors: one
 * for Blue, one for Yellow, one for Red and one for Green.
 */
public enum State implements Symbol {

    /**
     * State for a cell having an undefined state
     */
    None,

    /**
     * State for a cell having a different color
     */
    Other,

    /**
     * State for a cell having this color
     */
    Self,

    /**
     * State for a cell that could never contain this color
     */
    Shadow,

    /**
     * State for a cell that could contain this color
     */
    Light;

}