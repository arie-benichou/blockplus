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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import blockplus.piece.matrix.Matrix;
import blockplus.position.NullPosition;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// TODO !? Renommer en Pieces
public enum PieceTemplate implements Supplier<PieceInterface>, PieceTemplateInterface {

    // null object
    ENTRY0 {

        @Override
        public PositionInterface getReferential() {
            return NullPosition.getInstance();
        }

        @Override
        public int getRadius() {
            return -1;
        }

        @Override
        public int getNumberOfRotations() {
            return 0;
        }

        @Override
        public PieceInterface get() {
            return NULL;
        }

    },

    // monominoes
    ENTRY1 {

        @Override
        public PositionInterface getReferential() {
            return Position.from();
        }

        @Override
        public int getRadius() {
            return 0;
        }

        @Override
        public int getNumberOfRotations() {
            return 1;
        }

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

    private final static int FULL_ROTATION_UNIT = 360;
    private final static int COUNTER_CLOCKWISE_ROTATION = 90;
    private final static int NUMBER_OF_ROTATIONS = FULL_ROTATION_UNIT / COUNTER_CLOCKWISE_ROTATION;

    private static PositionInterface extractReferential(final PieceTemplateData pieceData) {
        final Matrix matrix = pieceData.getMatrix();
        return Position.from(matrix.get(0, 0), matrix.get(0, 1)); // TODO extract constants
    }

    private static int computeRadius(final PieceTemplateData pieceData) {
        final Matrix matrix = pieceData.getMatrix();
        final int refY = matrix.get(0, 0); // TODO extract constant
        final int refX = matrix.get(1, 0); // TODO extract constant
        final int minY = matrix.min(0);
        final int minX = matrix.min(1);
        final int maxY = matrix.max(0);
        final int maxX = matrix.max(1);
        final List<Integer> deltas = Lists.newArrayList(
                Math.abs(refY - minY),
                Math.abs(refY - maxY),
                Math.abs(refX - minX),
                Math.abs(refX - maxX)
                );
        return Collections.max(deltas);
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
    private int radius;
    private int numberOfRotations;
    private PositionInterface referential;

    private PieceTemplate() {
        final int id = this.getId();
        if (id > 1) {
            final PieceTemplateData pieceData = PieceTemplateData.get(id);
            this.referential = extractReferential(pieceData);
            this.radius = computeRadius(pieceData);
            this.piece = PieceComposite.from(pieceData);
            this.numberOfRotations = computeNumberOfDistinctRotations(this.piece);
        }
    }

    @Override
    public int getId() {
        return this.ordinal();
    }

    public PositionInterface getReferential() {
        return this.referential;
    }

    @Override
    public int getRadius() {
        return this.radius;
    }

    @Override
    public int getNumberOfRotations() {
        return this.numberOfRotations;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("\n " + "radius", this.getRadius())
                .add("\n " + "rotations", this.getNumberOfRotations())
                .addValue("\n " + this.get() + "\n")
                .toString();
    }

    @Override
    public PieceInterface get() {
        return this.piece;
    }

}