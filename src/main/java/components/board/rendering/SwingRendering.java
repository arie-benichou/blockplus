
package components.board.rendering;

import static components.position.Position.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import components.board.BoardInterface;

@SuppressWarnings("serial")
public class SwingRendering extends JComponent {

    private final static class BoardView extends JComponent {

        public static final int SIZE = 22;

        //private static final Color DEFAULT = Color.decode("#2a2d30");
        private static final Color DEFAULT = Color.MAGENTA;

        private final Map<?, Color> colorBySymbol;

        private BoardInterface<?> board;

        public BoardView setBoard(final BoardInterface<?> board) {
            this.board = board;
            return this;
        }

        public BoardInterface<?> getBoard() {
            return this.board;
        }

        public BoardView(final Map<?, Color> colorBySymbol) {
            this.colorBySymbol = colorBySymbol;
        }

        private Color getColor(final int i, final int j) {
            Color color = this.colorBySymbol.get(this.getBoard().get(Position(i, j)));
            if (color == null) color = DEFAULT;
            return color;
        }

        private void drawComponent(final Graphics g, final int x, final int y, final Color color) {
            g.setColor(color);
            g.fillRect(x, y, SIZE, SIZE);
        }

        @Override
        protected void paintComponent(final Graphics g) {
            for (int i = 0; i < this.getBoard().rows(); ++i) {
                for (int j = 0; j < this.getBoard().columns(); ++j) {
                    this.drawComponent(g, SIZE * j, SIZE * i, Color.BLACK);
                }
            }
            for (int i = 0; i < this.getBoard().rows(); ++i) {
                for (int j = 0; j < this.getBoard().columns(); ++j) {
                    this.drawComponent(g, SIZE * j, SIZE * i, this.getColor(i, j));
                }
            }
        }
    }

    private final JFrame window;

    public JFrame getWindow() {
        return this.window;
    }

    private final BoardView canvas;

    public BoardView getCanvas() {
        return this.canvas;
    }

    public SwingRendering(final Map<?, Color> colorBySymbol) {
        this.window = new JFrame();
        this.window.setTitle("Blockplus");
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.canvas = new BoardView(colorBySymbol);
        this.window.getContentPane().add(this.getCanvas());
        this.window.setLocation(750, 300);
        //this.window.setUndecorated(true);
    }

    public void apply(final BoardInterface<?> board) {
        //this.window.setPreferredSize(new Dimension(board.columns() * BoardView.SIZE, board.rows() * BoardView.SIZE));
        this.window.setPreferredSize(new Dimension(board.columns() * BoardView.SIZE + 10, board.rows() * BoardView.SIZE + 35));
        this.getCanvas().setBoard(board);
        this.window.repaint();
        this.window.setVisible(true);
        this.window.pack();
    }

    /*
    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.canvas.getBoard()).toString();
    }
    */

}