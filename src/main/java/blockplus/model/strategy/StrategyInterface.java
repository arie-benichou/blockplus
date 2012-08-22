
package blockplus.model.strategy;

import java.util.List;

import blockplus.model.game.GameContext;
import blockplus.model.move.Move;



public interface StrategyInterface {

    List<Move> sort(GameContext context, List<Move> options);

    Move chooseMove(GameContext context);

    //List<Move> sortOpponent(GameContext nextContext, List<Move> opponentLegalMoves);

}