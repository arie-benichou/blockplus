
package blockplus.model.player;

import blockplus.model.color.ColorInterface;
import blockplus.model.piece.PiecesBag;
import blockplus.model.strategy.StrategyInterface;

public class DeadPlayer implements PlayerInterface {

    private final PlayerInterface player;

    public DeadPlayer(final PlayerInterface player) {
        this.player = player;
    }

    @Override
    public StrategyInterface getStrategy() {
        return this.player.getStrategy();
    }

    @Override
    public PiecesBag getPieces() {
        return this.player.getPieces();
    }

    @Override
    public ColorInterface getColor() {
        return this.player.getColor();
    }

    @Override
    public boolean isDead() {
        return true;
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public ColorInterface getOpponentColor() {
        return this.player.getOpponentColor();
    }

    @Override
    public String toString() {
        return this.player.toString();
    }

}