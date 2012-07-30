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

package blockplus.piece;

import com.google.common.base.Supplier;

public enum Pieces implements Supplier<PieceInterface> {

    // null object
    ENTRY0,

    // monominoes
    ENTRY1,

    // dominoes
    ENTRY2,

    // triominoes
    ENTRY3, ENTRY4,

    // tetrominoes
    ENTRY5, ENTRY6, ENTRY7, ENTRY8, ENTRY9,

    //pentominoes
    ENTRY10, ENTRY11, ENTRY12, ENTRY13, ENTRY14, ENTRY15, ENTRY16,
    ENTRY17, ENTRY18, ENTRY19, ENTRY20, ENTRY21;

    private static final String ENTRY_NAME_PATTERN = "ENTRY";

    public final static Pieces get(final int ordinal) {
        return Pieces.valueOf(ENTRY_NAME_PATTERN + ordinal);
    }

    private PieceInterface piece;

    private Pieces() {
        this.piece = Piece.from(PieceData.get(this.ordinal()));
    }

    @Override
    public PieceInterface get() {
        return this.piece;
    }

    @Override
    public String toString() {
        return this.get().toString();
    }

}