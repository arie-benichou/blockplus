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

import static blockplus.board.BoardBuilder.parse;
import blockplus.board.Board;
import blockplus.board.BoardRenderer;
import blockplus.color.ColorInterface;

public class BoardBuilderDemo {

    public static void main(final String[] args) {

        {
            final int[][] data = {
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
            };
            final Board<ColorInterface> board = parse(data);
            //System.out.println(board);
            BoardRenderer.render(board);
        }

        {
            final String[][] data = {
                    { "..........." },
                    { ".b.......y." },
                    { "..B.....Y.." },
                    { "....O?Ø...." },
                    { "..G.....R.." },
                    { ".g.......r." },
                    { "..........." }
            };
            final Board<ColorInterface> board = parse(data);
            //System.out.println(board);
            BoardRenderer.render(board);
        }

    }

}