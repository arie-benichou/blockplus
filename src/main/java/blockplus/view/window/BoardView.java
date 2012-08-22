
package blockplus.view.window;

import static blockplus.model.color.Colors.*;

import java.awt.Color;
import java.util.Map;

import blockplus.model.board.Board;
import blockplus.model.board.BoardParser;
import blockplus.model.color.ColorInterface;
import blockplus.view.View;
import blockplus.view.ViewBuilderInterface;
import blockplus.view.ViewInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import components.board.rendering.SwingRendering;

public class BoardView implements ViewInterface {

    private final static Map<?, Color> DEFAULT_SYMBOLS = new ImmutableMap.Builder<ColorInterface, Color>()
            .put(Blue, Color.decode("#3971c4"))
            .put(Red, Color.decode("#cc2b2b"))
            .put(Yellow, Color.decode("#eea435"))
            .put(Green, Color.decode("#04a44b"))
            .put(White, Color.decode("#2a2d30"))
            .build();

    private Board board;
    private final Map<?, Color> symbols;

    private final SwingRendering swingRendering;

    @SuppressWarnings("unchecked")
    public BoardView(final ViewBuilderInterface builder) {
        final Object inputSymbol = builder.getInputSymbol();
        Preconditions.checkArgument(inputSymbol instanceof Board);
        this.board = (Board) inputSymbol;
        final Object outputSymbol = builder.getOutputSymbol();
        if (outputSymbol == null) this.symbols = BoardView.DEFAULT_SYMBOLS;
        else {
            Preconditions.checkArgument(outputSymbol instanceof Map);
            this.symbols = (Map<?, Color>) outputSymbol;
        }
        this.swingRendering = new SwingRendering(this.symbols);
    }

    @Override
    public BoardView render() {
        this.swingRendering.apply(this.board.colorize());
        return this;
    }

    @Override
    public Object apply(final Object object) {
        Preconditions.checkArgument(object instanceof Board);
        this.board = (Board) object;
        this.swingRendering.apply(this.board.colorize());
        return this;
    }

    public static void main(final String[] args) {
        final String[][] data = {
                { "B.........Y" },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "G.........R" }
        };
        final Board board = new BoardParser().parse(data);
        View.as(BoardView.class).show(board).up();
        View.as(BoardView.class).show(board).up();
    }

}