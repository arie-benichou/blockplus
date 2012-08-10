
package blockplus.io;

import static blockplus.position.Position.Position;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JComponent;

import blockplus.board.Board;
import blockplus.color.ColorInterface;

import com.google.common.collect.ImmutableMap;

@SuppressWarnings("serial")
class BoardView extends JComponent {

    private static final Color DEFAULT = Color.decode("#2a2d30");

    public static final int SIZE = 22;

    private final static Map<ColorInterface, Color> COLOR_BY_COLOR = new ImmutableMap.Builder<ColorInterface, Color>()
            /*
            .put(ColorInterface.blue, Color.BLACK)
            .put(ColorInterface.red, Color.BLACK)
            .put(ColorInterface.yellow, Color.BLACK)
            .put(ColorInterface.green, Color.BLACK)
            .put(ColorInterface.white, Color.BLACK)
            */
            .put(ColorInterface.BLUE, Color.decode("#3971c4"))
            .put(ColorInterface.RED, Color.decode("#cc2b2b"))
            .put(ColorInterface.YELLOW, Color.decode("#eea435"))
            .put(ColorInterface.GREEN, Color.decode("#04a44b"))
            .put(ColorInterface.WHITE, Color.WHITE)
            .build();

    private final Board<ColorInterface> board;

    public BoardView(final Board<ColorInterface> board) {
        this.board = board;
    }

    private Color getColor(final int i, final int j) {
        Color color = COLOR_BY_COLOR.get(this.board.get(Position(i, j)));
        if (color == null) color = DEFAULT;
        return color;
    }

    private void drawComponent(final Graphics g, final int x, final int y, final Color color) {
        g.setColor(color);
        g.fillRect(x, y, SIZE, SIZE);
    }

    @Override
    public void paint(final Graphics g) {
        for (int i = 0; i < this.board.rows(); ++i) {
            for (int j = 0; j < this.board.columns(); ++j) {
                this.drawComponent(g, SIZE * j, SIZE * i, this.getColor(i, j));
            }
        }
    }
}