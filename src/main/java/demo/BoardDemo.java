/*
 * Copyright 2012 Arie Benichou
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

package demo;

import java.util.Map;
import java.util.Map.Entry;

import blockplus.board.Board;
import blockplus.board.BoardRenderingManager;
import blockplus.direction.DirectionInterface;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

public final class BoardDemo {

    public static void main(final String[] args) {

        final String data = "" +
                "3333333" +
                "3222223" +
                "3211123" +
                "3210123" +
                "3211123" +
                "3222223" +
                "3333333";

        final Board board = Board.from(data);

        final BoardRenderingManager boardRenderingManager = new BoardRenderingManager();
        System.out.println(boardRenderingManager.render(board));

        final PositionInterface position = Position.from(3, 3);

        {
            final Map<DirectionInterface, Integer> neighbours = board.getNeighbours(position, 0);
            for (final Entry<DirectionInterface, Integer> entry : neighbours.entrySet()) {
                System.out.println(entry);
            }
        }

        System.out.println();

        {
            final Map<DirectionInterface, Integer> neighbours = board.getNeighbours(position, 1);
            for (final Entry<DirectionInterface, Integer> entry : neighbours.entrySet()) {
                System.out.println(entry);
            }
        }

        System.out.println();

        {
            final Map<DirectionInterface, Integer> neighbours = board.getNeighbours(position);
            for (final Entry<DirectionInterface, Integer> entry : neighbours.entrySet()) {
                System.out.println(entry);
            }
        }

        System.out.println();

        {
            final Map<DirectionInterface, Integer> neighbours = board.getNeighbours(position, 2);
            for (final Entry<DirectionInterface, Integer> entry : neighbours.entrySet()) {
                System.out.println(entry);
            }
        }

        System.out.println();

        {
            final Map<DirectionInterface, Integer> neighbours = board.getNeighbours(position, 3);
            for (final Entry<DirectionInterface, Integer> entry : neighbours.entrySet()) {
                System.out.println(entry);
            }
        }

    }
}