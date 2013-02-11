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

import static components.position.Position.Position;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import blockplus.piece.matrix.Matrix;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import components.direction.Direction;
import components.direction.DirectionInterface;
import components.position.Position;
import components.position.PositionInterface;

// TODO ? use Suppliers.memoize()
public final class PieceComponent implements PieceInterface {

    private final static boolean IS_FACTORY_CACHING = true;

    public final static class Factory {

        private final boolean isCaching;

        private long cacheHits = 0;

        private final Map<PositionInterface, PieceComponent> cache = Maps.newConcurrentMap();

        private static PositionInterface check(final PositionInterface position) {
            Preconditions.checkArgument(position != null);
            return position;
        }

        public Factory(final boolean isCaching) {
            this.isCaching = isCaching;
        }

        public Factory() {
            this(IS_FACTORY_CACHING);
        }

        private PieceComponent getFromNew(final PositionInterface position) {
            return new PieceComponent(position);
        }

        private PieceComponent getFromCache(final PositionInterface position) {
            return this.cache.get(position);
        }

        public PieceComponent get(final PositionInterface position) {
            PieceComponent instance = this.getFromCache(position);
            if (instance == null) {
                instance = this.getFromNew(position);
                if (this.isCaching) this.cache.put(check(position), instance);
            }
            else ++this.cacheHits;
            return instance;
        }

        public long cacheHits() {
            return this.cacheHits;
        }

        public int size() {
            return this.cache.size();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this.getClass().getCanonicalName())
                    .add("size", this.size())
                    .add("cacheHits", this.cacheHits())
                    .toString();
        }

    }

    public final static PieceComponent.Factory FACTORY = new Factory();

    private final static PositionInterface ORIGIN = Position.ORIGIN;
    private final static PieceComponent UNIT = FACTORY.get(ORIGIN);

    @SuppressWarnings("all")
    public static PieceComponent PieceComponent() {
        return UNIT;
    }

    @SuppressWarnings("all")
    public static PieceComponent PieceComponent(final PositionInterface position) {
        return FACTORY.get(position);
    }

    @SuppressWarnings("all")
    public static PieceComponent PieceComponent(final int row, final int column) {
        return PieceComponent(Position(row, column));
    }

    private final static int ID = 1; // TODO ? à revoir

    private final static Matrix ROTATION_MATRIX = new Matrix(2, 2, new int[][] { { 0, -1 }, { 1, 0 } });

    public final static Set<DirectionInterface> SIDES_DIRECTIONS = new ImmutableSortedSet.Builder<DirectionInterface>(Ordering.natural())
            .add(Direction.TOP).add(Direction.LEFT).add(Direction.RIGHT).add(Direction.BOTTOM).build();

    public final static Set<DirectionInterface> CORNERS_DIRECTIONS = new ImmutableSortedSet.Builder<DirectionInterface>(Ordering.natural())
            .add(Direction.TOP_LEFT).add(Direction.TOP_RIGHT).add(Direction.BOTTOM_LEFT).add(Direction.BOTTOM_RIGHT).build();

    private static Matrix computeMatrix(final PositionInterface position) {
        return new Matrix(2, 1, new int[][] { { position.row() }, { position.column() } });
    }

    private static PositionInterface extractPosition(final Matrix matrix) {
        return Position(matrix.get(0, 0), matrix.get(1, 0));
    }

    private static Set<PositionInterface> computeSides(final PieceComponent pieceInterface) {
        final Builder<PositionInterface> sidesBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final DirectionInterface direction : SIDES_DIRECTIONS) {
            sidesBuilder.add(pieceInterface.getReferential().apply(direction));
        }
        return sidesBuilder.build();
    }

    private static Set<PositionInterface> computeCorners(final PieceComponent pieceInterface) {
        final Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final DirectionInterface direction : CORNERS_DIRECTIONS) {
            cornersBuilder.add(pieceInterface.getReferential().apply(direction));
        }
        return cornersBuilder.build();
    }

    private final PositionInterface referential;
    private final Set<PositionInterface> positions;

    private volatile Set<PositionInterface> sides;
    private volatile Set<PositionInterface> corners;
    private volatile Set<PieceInterface> components;

    // TODO ? envisager de pouvoir passer un référentiel
    private PieceComponent(final PositionInterface position) {
        this.referential = position;
        this.positions = ImmutableSet.of(position);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public PositionInterface getReferential() {
        return this.referential;
    }

    @Override
    public Set<PositionInterface> getCorners() {
        Set<PositionInterface> corners = this.corners;
        if (corners == null) {
            synchronized (this) {
                if ((corners = this.corners) == null)
                    this.corners = corners = computeCorners(this);
            }
        }
        return corners;
    }

    @Override
    public Set<PositionInterface> getSides() {
        Set<PositionInterface> sides = this.sides;
        if (sides == null) {
            synchronized (this) {
                if ((sides = this.sides) == null)
                    this.sides = sides = computeSides(this);
            }
        }
        return sides;
    }

    @Override
    public Set<PositionInterface> getLightPositions() {
        return this.getCorners();
    }

    @Override
    public Set<PositionInterface> getShadowPositions() {
        return this.getSides();
    }

    @Override
    public Set<PieceInterface> get() {
        Set<PieceInterface> components = this.components;
        if (components == null) {
            synchronized (this) {
                if ((components = this.components) == null) {
                    final ImmutableSet.Builder<PieceInterface> builder = ImmutableSet.builder();
                    this.components = components = builder.add(this).build();
                }
            }
        }
        return this.components;
    }

    @Override
    public Set<PositionInterface> getSelfPositions() {
        return this.positions;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public PieceComponent translateTo(final PositionInterface position) {
        return PieceComponent(position);
    }

    @Override
    public PieceComponent translateBy(final DirectionInterface direction) {
        return PieceComponent(this.getReferential().apply(direction));
    }

    @Override
    public PieceComponent rotate() {
        return this;
    }

    @Override
    public PieceComponent rotateAround(final PositionInterface referential) {
        final DirectionInterface directionFromReferentialToOrigin = Direction.from(referential, ORIGIN);
        final PieceComponent relativelyTranslatedToOrigin = this.translateBy(directionFromReferentialToOrigin);
        final PositionInterface relativePositionToOrigin = relativelyTranslatedToOrigin.getReferential();
        // TODO ? extract unit constants
        final Matrix matrix = computeMatrix(relativePositionToOrigin);
        final PositionInterface rotatedRelativePositionToOrigin = extractPosition(ROTATION_MATRIX.multiply(matrix));
        final PieceComponent rotatedAroundOrigin = relativelyTranslatedToOrigin.translateTo(rotatedRelativePositionToOrigin);
        // TODO Direction.opposite(direction)
        final DirectionInterface directionFromOriginToReferential = Direction.from(ORIGIN, referential);
        return rotatedAroundOrigin.translateBy(directionFromOriginToReferential);
    }

    @Override
    public PieceComponent reflectAlongVerticalAxis(final PositionInterface referential) {
        final int axis = referential.column();
        final PositionInterface currentPosition = this.getReferential();
        final DirectionInterface direction = Direction.from(currentPosition, Position(currentPosition.row(), axis));
        final PositionInterface newPosition = Position(currentPosition.row(), referential.column() + direction.columnDelta());
        return this.translateTo(newPosition);
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis() {
        return this;
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis(final PositionInterface referential) {
        final int axis = referential.row();
        final PositionInterface currentPosition = this.getReferential();
        final DirectionInterface direction = Direction.from(currentPosition, Position(axis, currentPosition.column()));
        final PositionInterface newPosition = Position(referential.row() + direction.rowDelta(), currentPosition.column());
        return this.translateTo(newPosition);
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis() {
        return this;
    }

    @Override
    public int hashCode() {
        return this.getReferential().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceComponent)) return false;
        final PieceComponent that = (PieceComponent) object;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqual = this.getReferential().equals(that.getReferential());
        Preconditions.checkState(haveSameHashCode == isEqual);
        return isEqual;
    }

    @Override
    public String toString() {
        return "(" + this.getReferential().row() + ", " + this.getReferential().column() + ")";
    }

}