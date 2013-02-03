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

package blockplus.piece;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

public enum Pieces implements Iterable<PieceInterface> {

    // null piece
    PIECE0,

    // monominoes
    PIECE1,

    // dominoes
    PIECE2,

    // triominoes
    PIECE3, PIECE4,

    // tetrominoes
    PIECE5, PIECE6, PIECE7, PIECE8, PIECE9,

    // pentominoes
    PIECE10, PIECE11, PIECE12, PIECE13, PIECE14, PIECE15, PIECE16,
    PIECE17, PIECE18, PIECE19, PIECE20, PIECE21;

    private final static String PIECE_NAME_PATTERN = "PIECE";
    private final static Pieces[] VALUES = Pieces.values();

    public final static Pieces get(final int ordinal) {
        return Pieces.valueOf(PIECE_NAME_PATTERN + ordinal);
    }

    public static Set<Pieces> set() {
        final Set<Pieces> pieces = Sets.newLinkedHashSet();
        for (final Pieces piece : VALUES)
            pieces.add(piece);
        return pieces;
    }

    private final PieceInstances pieceInstances;

    private Pieces() {
        this.pieceInstances = new PieceInstances(this.ordinal());
    }

    public PieceInstances getInstances() {
        return this.pieceInstances;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.getInstances().iterator();
    }

}