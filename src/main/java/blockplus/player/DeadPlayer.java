/*
 * Copyright 2012-2013 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.player;

import blockplus.color.ColorInterface;
import blockplus.piece.PiecesBag;
import blockplus.strategy.StrategyInterface;

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