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
import blockplus.strategy.FirstOptionStrategy;
import blockplus.strategy.StrategyInterface;

import com.google.common.base.Objects;

// TODO Player as composite pour les variantes de jeux
public class Player implements PlayerInterface {

    private static final StrategyInterface DEFAULT_STRATEGY = new FirstOptionStrategy();

    private final ColorInterface color;
    private final PiecesBag bagOfPieces;
    private final StrategyInterface strategy;
    private final ColorInterface opponentColor;

    public Player(final ColorInterface color, final PiecesBag bagOfPieces, final ColorInterface opponentColor, final StrategyInterface strategy) {
        this.color = color;
        this.bagOfPieces = bagOfPieces;
        this.opponentColor = opponentColor;
        this.strategy = strategy;
    }

    public Player(final ColorInterface color, final PiecesBag bagOfPieces, final ColorInterface opponentColor) {
        this(color, bagOfPieces, opponentColor, DEFAULT_STRATEGY);
    }

    @Override
    public ColorInterface getColor() {
        return this.color;
    }

    @Override
    public PiecesBag getPieces() {
        return this.bagOfPieces;
    }

    @Override
    public StrategyInterface getStrategy() {
        return this.strategy;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("color", this.getColor())
                .add("strategy", this.getStrategy().getClass().getSimpleName())
                .add("pieces", this.bagOfPieces)
                .toString();
    }

    @Override
    public ColorInterface getOpponentColor() {
        return this.opponentColor;
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

}