
package blockplus.io;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

import blockplus.board.Board;
import blockplus.color.ColorInterface;

@SuppressWarnings("serial")
public class MainView extends JComponent {

    public static void render(final Board<ColorInterface> board) {
        final JFrame window = new JFrame();
        window.setTitle("Blockplus");
        window.setLocation(750, 300);
        window.setPreferredSize(new Dimension(board.columns() * BoardView.SIZE + 10, board.rows() * BoardView.SIZE + 30));
        final BoardView canvas = new BoardView(board);
        window.getContentPane().add(canvas);
        window.setVisible(true);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}