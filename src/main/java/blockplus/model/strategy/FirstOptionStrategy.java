
package blockplus.model.strategy;

import java.util.List;

import blockplus.model.game.BlockplusGameContext;
import blockplus.model.move.Move;



public final class FirstOptionStrategy implements StrategyInterface {

    @Override
    public Move chooseMove(final BlockplusGameContext gameContext) {
        return gameContext.options().get(0);
    }

    @Override
    public List<Move> sort(final BlockplusGameContext context, final List<Move> options) {
        return options;
    }

}