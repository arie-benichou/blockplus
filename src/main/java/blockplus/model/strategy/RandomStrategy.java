
package blockplus.model.strategy;

import java.util.List;
import java.util.Random;

import blockplus.model.game.BlockplusGameContext;
import blockplus.model.move.Move;



public final class RandomStrategy implements StrategyInterface {

    private final Random random = new Random();

    @Override
    public Move chooseMove(final BlockplusGameContext gameContext) {
        final List<Move> options = gameContext.options();
        return options.get(this.random.nextInt(options.size()));
    }

    @Override
    public List<Move> sort(final BlockplusGameContext context, final List<Move> options) {
        return options;
    }
}