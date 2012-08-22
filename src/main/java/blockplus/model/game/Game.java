
package blockplus.model.game;

import blockplus.model.move.Move;
import blockplus.view.View;
import blockplus.view.window.BoardView;

public class Game {

    private final GameContext initialContext;

    public GameContext getInitialContext() {
        return this.initialContext;
    }

    public Game(final GameContext initialContext) {
        this.initialContext = initialContext;
    }

    public Game() {
        this(GameContext.DEFAULT);
    }

    public GameContext start(final int max) {

        int i = 0;
        GameContext newContext = this.getInitialContext();

        // TODO listeners du contexte
        final BoardView windowBoardView = View.as(blockplus.view.window.BoardView.class).show(newContext.getBoard()).up();
        final blockplus.view.console.BoardView consoleBoardView = View.as(blockplus.view.console.BoardView.class).show(newContext.getBoard()).up();

        while (newContext.hasNext() && i != max) {
            final Move move = newContext.getMove();

            newContext = newContext.apply(move);

            windowBoardView.apply(newContext.getBoard()); // TODO à virer une fois listeners du contexte 
            consoleBoardView.apply(newContext.getBoard()); // TODO à virer une fois listeners du contexte

            if (!move.isNull()) { // TODO à virer
                try {
                    Thread.sleep(500);
                }
                catch (final InterruptedException e) {}
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

    public GameContext start() {
        return this.start(-1);
    }

    public GameContext reset() {
        return this.getInitialContext();
    }

}