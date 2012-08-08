
package blockplus.board;

import static blockplus.position.Position.Position;

import java.util.Map;

import blockplus.color.Color;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public final class BoardRenderer {

    private final static Map<Color, Character> COLOR_BY_STRING = Maps.newTreeMap();

    static {
        for (final Color color : Color.values())
            COLOR_BY_STRING.put(color, color.toString().charAt(0));
        COLOR_BY_STRING.put(Color.blue, '.');
        COLOR_BY_STRING.put(Color.red, '.');
        COLOR_BY_STRING.put(Color.yellow, '.');
        COLOR_BY_STRING.put(Color.green, '.');
        COLOR_BY_STRING.put(Color.white, '*');
    }

    public static <T> String getRendering(final Board<T> board) {
        final String lineSeparator = "\n" + " " + Strings.repeat("----", board.columns()) + "-" + "\n";
        final String columnSeparator = " | ";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.rows(); ++i) {
            sb.append(lineSeparator);
            for (int j = 0; j < board.columns(); ++j) {
                sb.append(columnSeparator);
                sb.append(COLOR_BY_STRING.get(board.get(Position(i, j))));
            }
            sb.append(columnSeparator);
        }
        sb.append(lineSeparator);
        return sb.toString();
    }

    public static <T> void render(final Board<T> board) {
        System.out.println(getRendering(board));
    }

    private BoardRenderer() {}

}