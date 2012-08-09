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

package demo.board;

import java.util.Set;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.color.Color;
import static blockplus.position.Position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.Sets;

public final class BoardDemo2 {

    public static void main(final String[] args) {

        final int[][] data = {
                //0  1  2  3  4  5  6 
                { 3, 3, 3, 3, 3, 3, 3 },// 0
                { 3, 2, 2, 2, 2, 2, 3 },// 1
                { 3, 2, 1, 1, 1, 2, 3 },// 2
                { 3, 2, 1, 0, 1, 2, 3 },// 3
                { 3, 2, 1, 1, 1, 2, 3 },// 4
                { 3, 2, 2, 2, 2, 2, 3 },// 5
                { 3, 3, 3, 3, 3, 3, 3 } // 6
        };

        final Board<Color> board = BoardBuilder.parse(data);
        System.out.println(board);

        final PositionInterface position = Position(3, 3);
        System.out.println(position);

        for (int radius = -1; radius < 4; ++radius) {
            System.out.println();
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println();
            System.out.println("radius: " + radius);
            final Set<PositionInterface> neighbours = Sets.newTreeSet(board.getNeighboursPositions(position, radius));
            for (final PositionInterface neighbour : neighbours) {
                System.out.println(neighbour + ": " + board.get(neighbour));
            }
            System.out.println(neighbours.size());
        }
        System.out.println();
        System.out.println("-----------------------------8<-----------------------------");
        System.out.println();
    }

}