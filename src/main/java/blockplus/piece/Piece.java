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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import blockplus.matrix.Matrix;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

// TODO enlever les responsabilités qui n'appartiennent pas à PieceTemplate
// TODO ? renommer en PieceTemplate
public final class Piece implements PieceTemplateInterface {

    private final static int FULL_ROTATION_UNIT = 360;
    private final static int COUNTER_CLOCKWISE_ROTATION = 90;
    private final static int NUMBER_OF_ROTATIONS = FULL_ROTATION_UNIT / COUNTER_CLOCKWISE_ROTATION;
    private final static Matrix ROTATION = new Matrix(2, 2, new int[][] { { 0, -1 }, { 1, 0 } });
    private final static PositionInterface ORIGIN = Position.from(0, 0);
    private final static PieceTemplateInterface NULL = new PieceForNull();

    private static PieceData check(final PieceData pieceData) {
        Preconditions.checkArgument(pieceData != null); // TODO faire plus de vérifications
        return pieceData;
    }

    public static PieceTemplateInterface from(final PieceData pieceData) {
        if (check(pieceData).ordinal() == 0) return NULL;
        return new Piece(pieceData);
    }

    private final int id;
    private final Matrix matrix;
    private final PositionInterface referential;
    private final int boxingSquareSide;
    private final int instanceOrdinal;

    private transient volatile List<PositionInterface> positions = null;
    private transient volatile List<PieceTemplateInterface> rotations = null;
    private transient volatile String representation = null;
    private transient volatile Integer hashCode = null;

    public static Matrix translate(final Matrix matrix, final PositionInterface fixedPointPosition, final PositionInterface newFixedPointPosition) {
        final int numberOfCells = matrix.getNumberOfColumns();
        final int deltaY = newFixedPointPosition.row() - fixedPointPosition.row();
        final int deltaX = newFixedPointPosition.column() - fixedPointPosition.column();
        final int[] delta1 = new int[numberOfCells];
        Arrays.fill(delta1, deltaY);
        final int[] delta2 = new int[numberOfCells];
        Arrays.fill(delta2, deltaX);
        final Matrix translation = new Matrix(2, numberOfCells, new int[][] { delta1, delta2 });
        return matrix.add(translation);
    }

    private static PositionInterface computeReferential(final Matrix matrix) {
        return Position.from(matrix.get(0, 0), matrix.get(1, 0));
    }

    private static Matrix computeTranslation(final Matrix matrix) {
        return translate(matrix, computeReferential(matrix), ORIGIN);
    }

    private static int computeBoxingSquareSide(final Matrix matrix) {
        final PositionInterface p1 = matrix.getPositionHavingHighestValue();
        final PositionInterface p2 = matrix.getPositionHavingLowestValue();
        final int highestValue = matrix.get(p1.row(), p1.column());
        final int lowestValue = matrix.get(p2.row(), p2.column());
        return highestValue - lowestValue + 1;
    }

    // TODO ? PieceInstanceManager
    private Piece(final int id, final Matrix matrix, final PositionInterface referential, final int boxingSquareSide, final int instanceOrdinal) {
        this.id = id;
        this.matrix = matrix;
        this.referential = referential;
        this.boxingSquareSide = boxingSquareSide;
        this.instanceOrdinal = instanceOrdinal;
    }

    private Piece(final PieceTemplateInterface piece, final Matrix matrix, final int instanceOrdinal) {
        this(piece.getId(), matrix, piece.getReferential(), piece.getBoxingSquareSide(), instanceOrdinal);
    }

    // TODO !
    private Piece(final PieceInterface piece, final Matrix matrix) {
        this(piece.getId(), matrix, piece.getReferential(), piece.getBoxingSquareSide(), piece.getInstanceOrdinal());
    }

    //TODO ? à precomputer dans l'énumération
    private Piece(final int id, final Matrix matrix, final int instanceOrdinal) {
        this(id, computeTranslation(matrix), computeReferential(matrix), computeBoxingSquareSide(matrix), instanceOrdinal);
    }

    private Piece(final PieceData pieceData) {
        this(pieceData.ordinal(), pieceData.getMatrix(), 0); // TODO extract constant
    }

    @Override
    public PieceTemplateInterface get() {
        return this;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Matrix getMatrix() {
        return this.matrix;
    }

    @Override
    public int getNumberOfCells() {
        return this.getMatrix().getNumberOfColumns();
    }

    @Override
    public int getBoxingSquareSide() {
        return this.boxingSquareSide;
    }

    @Override
    public int getInstanceOrdinal() {
        return this.instanceOrdinal;
    }

    @Override
    public PositionInterface getReferential() {
        return this.referential;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    private List<PositionInterface> computePositions() {
        final Builder<PositionInterface> positionsBuilder = ImmutableList.builder();
        for (int n = 0; n < this.getNumberOfCells(); ++n) {
            final int row = this.getMatrix().get(0, n);
            final int column = this.getMatrix().get(1, n);
            final PositionInterface position = Position.from(row, column);
            positionsBuilder.add(position);
        }
        return positionsBuilder.build();
    }

    @Override
    public List<PositionInterface> getPositions() {
        List<PositionInterface> value = this.positions;
        if (value == null) {
            synchronized (this) {
                if ((value = this.positions) == null) this.positions = value = this.computePositions();
            }
        }
        return value;
    }

    @Override
    public List<PositionInterface> getPositions(final PositionInterface position) {
        return Lists.transform(this.getPositions(), new Function<PositionInterface, PositionInterface>() {

            @Override
            public PositionInterface apply(@Nullable final PositionInterface input) {
                return Position.from(input.row() + position.row(), input.column() + position.column());
            }

        });
    }

    @Override
    public Iterator<PositionInterface> iterator() {
        return this.getPositions().iterator();
    }

    private List<PieceTemplateInterface> computeRotations() {
        PieceTemplateInterface piece = this;
        final Builder<PieceTemplateInterface> builder = new ImmutableList.Builder<PieceTemplateInterface>();
        final Map<String, PieceTemplateInterface> rotations = Maps.newHashMap();
        rotations.put(flatten(piece), piece);
        builder.add(piece);
        for (int i = 1; i < NUMBER_OF_ROTATIONS; ++i) {
            piece = new Piece(piece, ROTATION.multiply(piece.getMatrix()), i);
            final String flattenTemplate = flatten(piece);
            final PieceTemplateInterface rotation = rotations.get(flattenTemplate);
            if (rotation == null) {
                rotations.put(flattenTemplate, piece);
                builder.add(piece);
            }
        }
        return builder.build();
    }

    @Override
    public List<PieceTemplateInterface> getRotations() {
        List<PieceTemplateInterface> value = this.rotations;
        if (value == null) {
            synchronized (this) {
                if ((value = this.rotations) == null) this.rotations = value = this.computeRotations();
            }
        }
        return value;
    }

    // TODO ? Bounds Object
    private static int[] computeBounds(final PieceInterface piece) {
        int minY = Integer.MAX_VALUE, minX = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE, maxX = Integer.MIN_VALUE;
        for (final PositionInterface position : piece) {
            final int row = position.row(), column = position.column();
            if (row < minY) minY = row;
            if (row > maxY) maxY = row;
            if (column < minX) minX = column;
            if (column > maxX) maxX = column;
        }
        return new int[] { minY, minX, maxY, maxX };
    }

    // TODO ? Delta Object
    private static int[] computeDelta(final PieceInterface piece, final int[] bounds) {
        final int minY = bounds[0], minX = bounds[1], maxY = bounds[2], maxX = bounds[3];
        int deltaY = 0, deltaX = 0;
        final int n = piece.getBoxingSquareSide();
        if (minY < 0) deltaY = 0 - minY;
        else if (maxY > n - 1) deltaY = n - 1 - maxY;
        if (minX < 0) deltaX = 0 - minX;
        else if (maxX > n - 1) deltaX = n - 1 - maxX;
        return new int[] { deltaY, deltaX };
    }

    private static PieceInterface computePieceSupplier(final PieceInterface piece, final int[] delta) {
        PieceInterface newPiece = piece;
        final int deltaY = delta[0], deltaX = delta[1];
        if (deltaY != 0 || deltaX != 0) {
            final int[] delta1 = new int[piece.getNumberOfCells()];
            Arrays.fill(delta1, deltaY);
            final int[] delta2 = new int[piece.getNumberOfCells()];
            Arrays.fill(delta2, deltaX);
            final Matrix translation = new Matrix(2, piece.getNumberOfCells(), new int[][] { delta1, delta2 });
            final Matrix add = piece.getMatrix().add(translation);
            newPiece = new Piece(piece, add);
        }
        return newPiece;
    }

    //TODO ? PieceRepresentation(Bounds, Delta, ...)
    public static String flatten(final PieceInterface piece) { // TODO appartient à un autre objet
        final int[] bounds = computeBounds(piece);
        final int[] delta = computeDelta(piece, bounds);
        final PieceInterface flattenTemplate = Piece.computePieceSupplier(piece, delta);
        final int n = flattenTemplate.getBoxingSquareSide();
        final boolean[][] square = new boolean[n][n];
        for (final PositionInterface position : flattenTemplate)
            square[position.row()][position.column()] = true;
        final StringBuilder sb = new StringBuilder();
        sb.append("Origin = " + Position.from(delta[0], delta[1]));
        sb.append("\n");
        for (final boolean[] row : square) {
            for (final boolean cell : row)
                sb.append((cell ? "x" : ".") + " ");
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String value = this.representation;
        if (value == null) {
            synchronized (this) {
                if ((value = this.representation) == null) this.representation = value = flatten(this);
            }
        }
        return value;
    }

    @Override
    public int hashCode() {
        Integer value = this.hashCode;
        if (value == null) {
            synchronized (this) {
                if ((value = this.hashCode) == null)
                    this.hashCode = value = Objects.toStringHelper(this).addValue(this.getId()).addValue(this.getMatrix()).toString().hashCode();
            }
        }
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceTemplateInterface)) return false;
        final PieceTemplateInterface that = (PieceTemplateInterface) object;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqual = this.getId() == that.getId() && this.getMatrix().equals(that.getMatrix());
        Preconditions.checkState(haveSameHashCode == isEqual);
        return isEqual;
    }

}