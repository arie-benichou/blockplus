/*
 * Copyright 2012 Arie Benichou
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

import blockplus.color.Color;
import blockplus.piece.PiecesBag;

// TODO Player as composite pour les variantes de jeux
public class Player {

    private final Color color;
    private final PiecesBag bagOfPieces;

    public Player(final Color color, final PiecesBag bagOfPieces) {
        this.color = color;
        this.bagOfPieces = bagOfPieces;
    }

    public Color getColor() {
        return this.color;
    }

    public PiecesBag getAvailablePieces() {
        return this.bagOfPieces;
    }

}