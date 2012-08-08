
package blockplus.board;

import java.util.Map;

import blockplus.color.Color;
import static blockplus.position.Position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

public final class BoardBuilder {

    private final static Map<Character, Color> COLOR_BY_STRING = Maps.newTreeMap();
    private final static Map<Integer, Color> COLOR_BY_VALUE = Maps.newTreeMap();

    static {
        for (final Color color : Color.values()) {
            COLOR_BY_STRING.put(color.toString().charAt(0), color);
            COLOR_BY_VALUE.put(color.value(), color);
        }
        COLOR_BY_STRING.put('.', Color.TRANSPARENT);
    }

    // TODO !? utiliser Move
    private static Map<PositionInterface, Color> updatePreDefinedPositions(
            final Map<PositionInterface, Color> definedPositions,
            final int rows,
            final int columns) {

        final PositionInterface p1 = Position(0, 0);
        if (definedPositions.get(p1).equals(Color.TRANSPARENT))
            definedPositions.put(p1, Color.white);

        final PositionInterface p2 = Position(0, columns - 1);
        if (definedPositions.get(p2).equals(Color.TRANSPARENT))
            definedPositions.put(p2, Color.white);

        final PositionInterface p3 = Position(rows - 1, 0);
        if (definedPositions.get(p3).equals(Color.TRANSPARENT))
            definedPositions.put(p3, Color.white);

        final PositionInterface p4 = Position(rows - 1, columns - 1);
        if (definedPositions.get(p4).equals(Color.TRANSPARENT))
            definedPositions.put(p4, Color.white);

        definedPositions.put(Position(-1, -1), Color.WHITE);
        definedPositions.put(Position(-1, columns), Color.WHITE);
        definedPositions.put(Position(rows, -1), Color.WHITE);
        definedPositions.put(Position(rows, columns), Color.WHITE);

        return definedPositions;
    }

    public static Board<Color> parse(final String[][] data) {
        final int rows = data.length;
        final int columns = data[0][0].length();
        final Map<PositionInterface, Color> definedPositions = Maps.newHashMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                final char c = data[i][0].charAt(j);
                Color color = COLOR_BY_STRING.get(c);
                if (color == null) color = Color.UNKNOWN;
                final PositionInterface position = Position(i, j);
                definedPositions.put(position, color);
            }
        updatePreDefinedPositions(definedPositions, rows, columns);
        return Board.from(rows, columns, Color.TRANSPARENT, Color.OPAQUE, definedPositions);
    }

    public static Board<Color> parse(final int[][] data) {
        final int rows = data.length;
        final int columns = data[0].length;
        final Map<PositionInterface, Color> definedPositions = Maps.newHashMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                Color color = COLOR_BY_VALUE.get(data[i][j]);
                if (color == null) color = Color.UNKNOWN;
                final PositionInterface position = Position(i, j);
                definedPositions.put(position, color);
            }
        updatePreDefinedPositions(definedPositions, rows, columns);
        return Board.from(rows, columns, Color.TRANSPARENT, Color.OPAQUE, definedPositions);
    }

    private BoardBuilder() {}

}