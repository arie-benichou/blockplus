
package blockplus.piece;

import static blockplus.position.Position.Position;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import blockplus.direction.Direction;
import blockplus.direction.DirectionInterface;
import blockplus.piece.matrix.Matrix;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

public final class PieceComponent implements PieceInterface {

    // use on debug purpose 
    private final static boolean IS_FACTORY_CACHING = true;

    private final static int ID = 1;

    private final static PositionInterface ORIGIN = Position.ORIGIN;

    public final static Set<DirectionInterface> SIDES_DIRECTIONS = new ImmutableSortedSet.Builder<DirectionInterface>(Ordering.natural())
            .add(Direction.TOP)
            .add(Direction.LEFT)
            .add(Direction.RIGHT)
            .add(Direction.BOTTOM)
            .build();

    public final static Set<DirectionInterface> CORNERS_DIRECTIONS = new ImmutableSortedSet.Builder<DirectionInterface>(Ordering.natural())
            .add(Direction.TOP_LEFT)
            .add(Direction.TOP_RIGHT)
            .add(Direction.BOTTOM_LEFT)
            .add(Direction.BOTTOM_RIGHT)
            .build();

    private final static Matrix ROTATION_MATRIX = new Matrix(2, 2, new int[][] { { 0, -1 }, { 1, 0 } });

    private static PositionInterface extractPosition(final Matrix matrix) {
        return Position(matrix.get(0, 0), matrix.get(1, 0));
    }

    private static PositionInterface check(final PositionInterface position) {
        Preconditions.checkArgument(position != null);
        return position;
    }

    public final static class Factory {

        private final boolean isCaching;

        private long cacheHit = 0;

        private final ConcurrentMap<PositionInterface, PieceComponent> cache = Maps.newConcurrentMap();

        public Factory(final boolean isCaching) {
            //System.out.println("Loading " + this.getClass().getCanonicalName() + "...");
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
            else ++this.cacheHit;
            return instance;
        }

        public long cacheHits() {
            return this.cacheHit;
        }

        public int size() {
            return this.cache.size();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this.getClass().getCanonicalName())
                    .add("size", this.size())
                    .add("cacheHit", this.cacheHits())
                    .toString();
        }

    }

    public final static Factory FACTORY = new Factory();

    public static PieceComponent from(final PositionInterface position) {
        return FACTORY.get(position);
    }

    public static PieceComponent from() {
        return from(ORIGIN);
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

    private static Matrix computeMatrix(final PositionInterface position) {
        return new Matrix(2, 1, new int[][] { { position.row() }, { position.column() } });
    }

    private final PositionInterface position;
    private transient volatile Set<PositionInterface> sides;
    private transient volatile Set<PositionInterface> corners;
    private transient volatile Set<PieceInterface> components;
    private transient volatile PieceComponent rotationAroundOrigin;

    private PieceComponent(final PositionInterface position) {
        this.position = position;
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public PositionInterface getReferential() {
        return this.position;
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
    public Set<PositionInterface> getPotentialPositions() {
        return this.getCorners();
    }

    @Override
    public Set<PieceInterface> get() {
        Set<PieceInterface> components = this.components;
        if (components == null) {
            synchronized (this) {
                if ((components = this.components) == null) this.components = components = ImmutableSet.of((PieceInterface) this);
            }
        }
        return this.components;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public PieceComponent translateTo(final PositionInterface position) {
        return from(position);
    }

    @Override
    public PieceComponent translateBy(final DirectionInterface direction) {
        return from(this.position.apply(direction));
    }

    @Override
    public PieceComponent rotate() {
        return this;
    }

    private PieceComponent rotateUnit() {
        PieceComponent rotationAroundOrigin = this.rotationAroundOrigin;
        if (rotationAroundOrigin == null) {
            synchronized (this) {
                if ((rotationAroundOrigin = this.rotationAroundOrigin) == null) {
                    final Matrix newMatrix = ROTATION_MATRIX.multiply(computeMatrix(this.getReferential()));
                    final PositionInterface newPosition = extractPosition(newMatrix);
                    this.rotationAroundOrigin = rotationAroundOrigin = this.translateTo(newPosition);
                }
            }
        }
        return rotationAroundOrigin;
    }

    @Override
    public PieceComponent rotateAround(final PositionInterface parentReferential) { // TODO !? à revoir
        final int k = Math.min(Math.abs(parentReferential.row()), Math.abs(parentReferential.column()));
        return this.translateBy(Direction.from(-k, -k)).rotateUnit().translateBy(Direction.from(k, k));
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
        return Objects.toStringHelper(this).
                addValue("(" + this.getReferential().row() + ", " + this.getReferential().column() + ")")
                .toString();
    }

}