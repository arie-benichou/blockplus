
package blockplus.model.game;

import blockplus.model.move.Move;
import blockplus.view.View;

public class BlockplusGame {

    private final BlockplusGameContext initialContext;

    public BlockplusGameContext getInitialContext() {
        return this.initialContext;
    }

    public BlockplusGame(final BlockplusGameContext initialContext) {
        this.initialContext = initialContext;
    }

    public BlockplusGame() {
        this(BlockplusGameContext.DEFAULT);
    }

    public BlockplusGameContext start(final int max) {

        int i = 0;
        BlockplusGameContext newContext = this.getInitialContext();

        // TODO listeners du contexte
        //final BoardView windowBoardView = View.as(blockplus.view.window.BoardView.class).show(newContext.getBoard()).up();
        final blockplus.view.console.BoardView consoleBoardView = View.as(blockplus.view.console.BoardView.class).show(newContext.getBoard()).up();

        while (newContext.hasNext() && i != max) {
            final Move move = newContext.getMove();

            newContext = newContext.apply(move);

            //windowBoardView.apply(newContext.getBoard()); // TODO à virer une fois listeners du contexte 
            consoleBoardView.apply(newContext.getBoard()); // TODO à virer une fois listeners du contexte

            if (!move.isNull()) { // TODO à virer
                /*
                try {
                    Thread.sleep(500);
                }
                catch (final InterruptedException e) {}
                */
            }

            /*
            System.out.println("//////////////////////////////////////////////////////////////////////////////////////");
            final Set<ColorInterface> colors = newContext.getBoard().getColors();
            final Map<?, Character> layerSymbols = ImmutableMap.of(Self, 'O', Other, 'X', None, ' ', Light, '.', Shadow, '#');
            final BoardConsoleView layerView = new BoardConsoleView(layerSymbols);
            for (final ColorInterface color : colors) {
                System.out.println(color);
                final BoardLayer layer = newContext.getBoard().getLayer(color);
                layerView.render(layer);
            }
            System.out.println("//////////////////////////////////////////////////////////////////////////////////////");
            */

            newContext = newContext.next();
            ++i;
        }
        return newContext;
    }

    public BlockplusGameContext start() {
        return this.start(-1);
    }

    public BlockplusGameContext reset() {
        return this.getInitialContext();
    }

    /*
    public BlockplusGameContext next() {
        BlockplusGameContext nextContext = this.getInitialContext();
        final ColorInterface color = nextContext.getColor();
        List<Move> nextOptions;
        do {
            nextContext = nextContext.next();
            nextOptions = nextContext.options();
        } while (nextOptions.size() == 1 && nextOptions.iterator().next().isNull() && !nextContext.getColor().equals(color));
        return nextContext;
    }
    */
}