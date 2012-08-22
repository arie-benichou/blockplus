
package blockplus.view.console;

import static blockplus.model.color.Colors.*;

import java.util.Map;

import blockplus.model.board.Board;
import blockplus.view.ViewBuilderInterface;
import blockplus.view.ViewInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import components.board.rendering.StringRendering;

public class BoardView implements ViewInterface {

    private final static Map<?, Character> DEFAULT_SYMBOLS = new ImmutableMap.Builder<Object, Character>()
            .put(Blue, 'B')
            .put(Yellow, 'Y')
            .put(Red, 'R')
            .put(Green, 'G')
            .put(White, ' ')
            .put(Black, 'Ø')
            .build();

    private final Board board;
    private final Map<?, Character> symbols;
    private final StringRendering stringRendering;

    @SuppressWarnings("unchecked")
    public BoardView(final ViewBuilderInterface builder) {
        final Object inputSymbol = builder.getInputSymbol();
        Preconditions.checkArgument(inputSymbol instanceof Board);
        this.board = (Board) inputSymbol;
        final Object outputSymbol = builder.getOutputSymbol();
        if (outputSymbol == null) this.symbols = DEFAULT_SYMBOLS;
        else {
            Preconditions.checkArgument(outputSymbol instanceof Map);
            this.symbols = (Map<?, Character>) outputSymbol;
        }
        this.stringRendering = new StringRendering(this.symbols);
    }

    private BoardView render(final Board board) {
        System.out.println(this.stringRendering.apply(board));
        return this;
    }

    @Override
    public BoardView render() {
        return this.render(this.board);
    }

    @Override
    public BoardView apply(final Object object) {
        Preconditions.checkArgument(object instanceof Board, object);
        return this.render((Board) object);
    }

}