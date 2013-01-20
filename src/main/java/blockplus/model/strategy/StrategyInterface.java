
package blockplus.model.strategy;

import java.util.List;

import blockplus.model.game.BlockplusGameContext;
import blockplus.model.move.Move;



public interface StrategyInterface {

    List<Move> sort(BlockplusGameContext context, List<Move> options);

    Move chooseMove(BlockplusGameContext context);

    //List<Move> sortOpponent(GameContext nextContext, List<Move> opponentLegalMoves);

}