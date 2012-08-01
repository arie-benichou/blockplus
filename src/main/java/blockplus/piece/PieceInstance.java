
package blockplus.piece;

import java.util.Iterator;
import java.util.List;

import blockplus.matrix.Matrix;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

// TODO finir le draft
public class PieceInstance implements PieceInterface {

    private final Matrix matrix;
    private final PositionInterface referential;
    private final int boxingSquareSide;
    private final int numberOfCells;
    private final int id;
    private final int instanceOrdinal;

    private PieceInstance(
            final int id,
            final Matrix matrix,
            final PositionInterface position,
            final int boxingSquareSide,
            final int numberOfCells,
            final int instanceOrdinal) {
        this.id = id;
        this.matrix = matrix;
        this.referential = position;
        this.boxingSquareSide = boxingSquareSide;
        this.numberOfCells = numberOfCells;
        this.instanceOrdinal = instanceOrdinal;
    }

    public PieceInstance(final PieceTemplateInterface piece, final Matrix matrix, final PositionInterface position) {
        this(piece.getId(), matrix, position, piece.getBoxingSquareSide(), piece.getNumberOfCells(), piece.getInstanceOrdinal());
    }

    @Override
    public Matrix getMatrix() {
        return this.matrix;
    }

    @Override
    public PositionInterface getReferential() {
        return this.referential;
    }

    @Override
    public int getBoxingSquareSide() {
        return this.boxingSquareSide;
    }

    @Override
    public int getNumberOfCells() {
        return this.numberOfCells;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Iterator<PositionInterface> iterator() {
        return this.getPositions().iterator();
    }

    @Override
    public PieceInterface get() {
        return this;
    }

    @Override
    public int getInstanceOrdinal() {
        return this.instanceOrdinal;
    }

    // TODO ! refactoring with PieceTemplate
    @Override
    public List<PositionInterface> getPositions() {
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
    public boolean isNull() {
        return false;
    }

    @Override
    public String toString() {
        return Piece.flatten(this); // TODO ! à revoir
    }

}