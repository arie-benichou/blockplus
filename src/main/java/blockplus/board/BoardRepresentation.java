
package blockplus.board;

import java.util.Map;

import blockplus.Color;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

public final class BoardRepresentation {

    private final static Map<Character, Color> COLOR_BY_STRING = Maps.newTreeMap();
    private final static Map<Integer, Color> COLOR_BY_VALUE = Maps.newTreeMap();

    static {
        for (final Color color : Color.values()) {
            COLOR_BY_STRING.put(color.toString().charAt(0), color);
            COLOR_BY_VALUE.put(color.value(), color);
        }
        COLOR_BY_STRING.put('.', Color.Transparent);
    }

    private static Map<PositionInterface, Color> getDefinedPositions(final int rows, final int columns) {
        final Map<PositionInterface, Color> definedPositions = Maps.newHashMap();
        definedPositions.put(Position.from(-1, -1), Color.White);
        definedPositions.put(Position.from(-1, columns), Color.White);
        definedPositions.put(Position.from(rows, -1), Color.White);
        definedPositions.put(Position.from(rows, columns), Color.White);
        return definedPositions;
    }

    public static Board<Color> parse(final String[][] data) {
        final int rows = data.length;
        final int columns = data[0][0].length();
        final Map<PositionInterface, Color> definedPositions = getDefinedPositions(rows, columns);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                final char c = data[i][0].charAt(j);
                Color color = COLOR_BY_STRING.get(c);
                if (color == null) color = Color.Unknown;
                final PositionInterface position = Position.from(i, j);
                definedPositions.put(position, color);
            }
        return Board.from(rows, columns, Color.Transparent, Color.Black, definedPositions);
    }

    public static Board<Color> parse(final int[][] data) {
        final int rows = data.length;
        final int columns = data[0].length;
        final Map<PositionInterface, Color> definedPositions = getDefinedPositions(rows, columns);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                Color color = COLOR_BY_VALUE.get(data[i][j]);
                if (color == null) color = Color.Unknown;
                final PositionInterface position = Position.from(i, j);
                definedPositions.put(position, color);
            }
        return Board.from(rows, columns, Color.Transparent, Color.Black, definedPositions);
    }

    private BoardRepresentation() {}

    public static void main(final String[] args) {

        {
            final String[][] data = {
                    { "Y.....R" },
                    { "......." },
                    { "......." },
                    { "......." },
                    { "......." },
                    { "......." },
                    { "G.....B" },
            };
            System.out.println(parse(data));
        }

        {
            final int[][] data = {
                    { 3, 1, 1, 1, 1, 1, 5 },
                    { 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 1, 1, 1, 1, 1, 1 },
                    { 7, 1, 1, 1, 1, 1, 2 }
            };
            System.out.println(parse(data));
        }

    }

}