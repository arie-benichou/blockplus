
package demo;

import static blockplus.board.BoardBuilder.parse;
import blockplus.board.Board;
import blockplus.board.BoardRenderer;
import blockplus.color.Color;

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
            final Board<Color> board = parse(data);
            System.out.println(board);
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
            final Board<Color> board = parse(data);
            System.out.println(board);
            BoardRenderer.render(board);
        }

    }

}