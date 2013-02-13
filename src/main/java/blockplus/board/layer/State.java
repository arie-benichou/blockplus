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
 * Possible states for a cell in a layer of a board. Since each color is mapped
 * on its own layer, the board is made of 4 layers : one for Blue, one for
 * Yellow, one for Red and one for Green.
 */
public enum State implements Symbol {

    /**
     * State for a cell that could contain this layer's color
     */
    Metta,

    /**
     * State for a cell that can not contain this layer's color
     */
    Karuna,

    /**
     * State for a cell that contains a different layer's color
     */
    Mudita,

    /**
     * State for a cell that contains this layer's color
     */
    Upekkha,

    /**
     * State for a stateless cell
     */
    Nirvana

}