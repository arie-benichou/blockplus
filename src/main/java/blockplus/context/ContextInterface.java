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

package blockplus.context;

import java.util.List;

import blockplus.adversity.AdversityInterface;
import blockplus.adversity.SideInterface;
import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.move.Move;
import blockplus.player.PlayersInterface;

public interface ContextInterface {

    /////////////////////////////

    SideInterface getSide();

    AdversityInterface<ColorInterface> getAdversity();

    PlayersInterface getPlayers();

    Board getBoard();

    /////////////////////////////

    boolean isTerminal();

    ColorInterface get();

    ColorInterface getNext();

    List<Move> options();

    /////////////////////////////

    ContextInterface apply(Move move);

    Context forward(boolean skipOnNullOption); // TODO skip predicate

    ContextInterface forward();

    /////////////////////////////

}