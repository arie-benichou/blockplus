
package blockplus.model.player;

import blockplus.model.color.ColorInterface;
import blockplus.model.piece.PiecesBag;
import blockplus.model.strategy.StrategyInterface;

public interface PlayerInterface {

    StrategyInterface getStrategy();

    PiecesBag getPieces();

    ColorInterface getColor();

    boolean isDead();

    boolean isAlive();

    ColorInterface getOpponentColor();

}