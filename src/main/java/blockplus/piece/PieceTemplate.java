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

import java.util.Set;

import blockplus.piece.matrix.Matrix;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

// TODO Renommer en Pieces
public enum PieceTemplate implements Supplier<PieceInterface>, PieceTemplateInterface {

    // null object
    ENTRY0 {

        @Override
        public PieceInterface get() {
            return NULL;
        }

        @Override
        public int getBoxingSquareSide() {
            return 0; // TODO 0 -1
        }

        @Override
        public int getNumberOfRotations() {
            return 0;
        }

    },

    // monominoes
    ENTRY1 {

        @Override
        public PieceInterface get() {
            return UNIT;
        }

        @Override
        public int getBoxingSquareSide() {
            return 1; // TODO 0
        }

        @Override
        public int getNumberOfRotations() {
            return 1;
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

    private final static int FULL_ROTATION_UNIT = 360;
    private final static int COUNTER_CLOCKWISE_ROTATION = 90;
    private final static int NUMBER_OF_ROTATIONS = FULL_ROTATION_UNIT / COUNTER_CLOCKWISE_ROTATION;

    private static int computeBoxingSquareSide(final Matrix matrix) {
        final PositionInterface p1 = matrix.getPositionHavingHighestValue(); // TODO !! Matrix.min();
        final PositionInterface p2 = matrix.getPositionHavingLowestValue(); // TODO !! Matrix.max();
        final int highestValue = matrix.get(p1.row(), p1.column());
        final int lowestValue = matrix.get(p2.row(), p2.column());
        return highestValue - lowestValue + 1;
    }

    private static int computeNumberOfDistinctRotations(final PieceInterface pieceInterface) {
        PieceInterface rotatedPiece = pieceInterface;
        final Set<PieceInterface> rotations = Sets.newHashSet();
        rotations.add(rotatedPiece);
        for (int i = 1; i < NUMBER_OF_ROTATIONS; ++i) {
            rotatedPiece = rotatedPiece.rotate();
            rotations.add(rotatedPiece);
        }
        return rotations.size();
    }

    public final static PieceTemplate get(final int ordinal) {
        return PieceTemplate.valueOf(ENTRY_NAME_PATTERN + ordinal);
    }

    private PieceInterface piece;
    private int boxingSquareSide;
    private int numberOfRotations;

    private PieceTemplate() {
        final int id = this.getId();
        if (id > 1) {
            final PieceTemplateData pieceData = PieceTemplateData.get(id);
            this.boxingSquareSide = computeBoxingSquareSide(pieceData.getMatrix());
            this.piece = PieceComposite.from(pieceData);
            this.numberOfRotations = computeNumberOfDistinctRotations(this.piece);
        }
    }

    @Override
    public int getId() {
        return this.ordinal();
    }

    @Override
    public PieceInterface get() {
        return this.piece;
    }

    // TODO ! calculer un rayon plutot qu'un diametre
    @Override
    public int getBoxingSquareSide() {
        return this.boxingSquareSide;
    }

    @Override
    public int getNumberOfRotations() {
        return this.numberOfRotations;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("\n " + "diameter", this.getBoxingSquareSide())
                .add("\n " + "rotations", this.getNumberOfRotations())
                .addValue("\n " + this.get() + "\n")
                .toString();
    }

}