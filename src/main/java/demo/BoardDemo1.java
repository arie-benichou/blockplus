
package demo;

import static blockplus.position.Position.Position;

import java.util.Map;

import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

public class BoardDemo1 {

    public static void main(final String[] args) {

        final int n = 5 + 1 + 5;

        final int rows = n;
        final int columns = n;

        final Map<PositionInterface, Color> definedPositions = Maps.newHashMap();
        definedPositions.put(Position(-1, -1), Color.White);
        definedPositions.put(Position(-1, columns), Color.White);
        definedPositions.put(Position(rows, -1), Color.White);
        definedPositions.put(Position(rows, columns), Color.White);

        final Board<Color> board = Board.from(rows, columns, Color.TRANSPARENT, Color.OPAQUE, definedPositions);

        System.out.println(board);

        System.out.println(board.get(Position(-1, -2)).name());
        System.out.println(board.get(Position(-1, -1)).name());
        System.out.println(board.get(Position(-1, columns)).name());
        System.out.println(board.get(Position(rows, -1)).name());
        System.out.println(board.get(Position(rows, columns)).name());
        System.out.println(board.get(Position(0, 0)).name());

        final Map<PositionInterface, Color> updatedPositions = Maps.newHashMap();
        updatedPositions.put(Position(0, 0), Color.Blue);
        updatedPositions.put(Position(1, 0), Color.Yellow);
        updatedPositions.put(Position(2, 0), Color.Red);
        updatedPositions.put(Position(3, 0), Color.Green);

        updatedPositions.put(Position(0, columns - 1), Color.UNKNOWN);
        updatedPositions.put(Position(1, columns - 1), Color.OPAQUE);
        updatedPositions.put(Position(2, columns - 1), Color.White);

        System.out.println(board.update(updatedPositions));
        System.out.println(board);

    }

}