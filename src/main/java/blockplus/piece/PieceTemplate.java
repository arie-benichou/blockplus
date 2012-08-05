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

import blockplus.position.Position;

import com.google.common.base.Supplier;

public enum PieceTemplate implements Supplier<PieceInterface> {

    // null object
    ENTRY0 {

        @Override
        public PieceInterface get() {
            return NULL;
        }

    },

    // monominoes
    ENTRY1 {

        @Override
        public PieceInterface get() {
            return UNIT;
        }

    },

    // dominoes
    ENTRY2,

    // triominoes
    ENTRY3, ENTRY4,

    // tetrominoes
    ENTRY5, ENTRY6, ENTRY7, ENTRY8, ENTRY9,

    //pentominoes
    ENTRY10, ENTRY11, ENTRY12, ENTRY13, ENTRY14, ENTRY15, ENTRY16,
    ENTRY17, ENTRY18, ENTRY19, ENTRY20, ENTRY21;

    private final static String ENTRY_NAME_PATTERN = "ENTRY";

    private final static PieceInterface NULL = NullPieceComponent.getInstance();
    private final static PieceInterface UNIT = PieceComponent.from(Position.ORIGIN);

    public final static PieceTemplate get(final int ordinal) {
        return PieceTemplate.valueOf(ENTRY_NAME_PATTERN + ordinal);
    }

    private PieceInterface piece;

    private PieceTemplate() {
        if (this.ordinal() > 1) this.piece = PieceComposite.from(PieceData.get(this.ordinal()));
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