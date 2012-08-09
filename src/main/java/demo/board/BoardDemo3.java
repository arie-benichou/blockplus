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

import static blockplus.position.Position.Position;

import java.util.List;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.color.Color;
import blockplus.position.PositionInterface;

import com.google.common.base.Stopwatch;

public final class BoardDemo3 {

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
        final PositionInterface position = Position(3, 3);

        System.out.println();
        System.out.println("-----------------------------8<-----------------------------");
        System.out.println();

        final int radius = 1000;
        System.out.println("radius: " + radius);

        List<PositionInterface> neighbours = null;

        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        {
            //for (int i = 1; i < 1000; ++i)
            // 5.75 s
            neighbours = board.getNeighboursPositions(position, radius);
        }

        stopwatch.stop();

        System.out.println("number of neighbours : " + neighbours.size());

        System.out.println();
        System.out.println("-----------------------------8<-----------------------------");
        System.out.println();

        System.out.println(stopwatch);
    }

}