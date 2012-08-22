
package blockplus.model.strategy;

import java.util.List;

import blockplus.model.game.GameContext;
import blockplus.model.move.Move;



public final class FirstOptionStrategy implements StrategyInterface {

    @Override
    public Move chooseMove(final GameContext gameContext) {
        return gameContext.options().get(0);
    }

    @Override
    public List<Move> sort(final GameContext context, final List<Move> options) {
        return options;
    }

}