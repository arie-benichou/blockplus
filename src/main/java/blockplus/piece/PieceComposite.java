
package blockplus.piece;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import blockplus.direction.Direction;
import blockplus.direction.DirectionInterface;
import blockplus.piece.matrix.Matrix;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public final class PieceComposite implements PieceInterface {

    // use ONLY on debug purpose
    private final static boolean IS_FACTORY_CACHING = true;

    private final static int DIMENSION = 2;

    private final static int MINIMAL_NUMBER_OF_COMPONENTS = 2;
    private final static int MAXIMAL_NUMBER_OF_COMPONENTS = 5;

    private final static int MINIMAL_INDEX = 0;
    private final static int MAXIMAL_INDEX = MAXIMAL_NUMBER_OF_COMPONENTS - 1;

    private static PieceData check(final PieceData pieceData) {
        Preconditions.checkArgument(pieceData != null);
        return pieceData;
    }

    private static int check(final int numberOfCells) {
        Preconditions.checkArgument(numberOfCells >= MINIMAL_NUMBER_OF_COMPONENTS);
        Preconditions.checkArgument(numberOfCells <= MAXIMAL_NUMBER_OF_COMPONENTS);
        return numberOfCells;
    }

    private static Matrix check(final Matrix matrix) {
        Preconditions.checkArgument(matrix != null);
        Preconditions.checkArgument(matrix.getNumberOfRows() == DIMENSION);
        return matrix;
    }

    private static PositionInterface extractPosition(final Matrix matrix, final int n) {
        return Position.from(matrix.get(0, n), matrix.get(1, n));
    }

    private static PositionInterface check(final PositionInterface position) {
        Preconditions.checkArgument(position.row() >= MINIMAL_INDEX);
        Preconditions.checkArgument(position.row() <= MAXIMAL_INDEX);
        Preconditions.checkArgument(position.column() >= MINIMAL_INDEX);
        Preconditions.checkArgument(position.column() <= MAXIMAL_INDEX);
        return position;
    }

    private static Set<PieceInterface> check(final Matrix matrix, final int numberOfCells) {
        final Set<PieceInterface> distinctComponents = Sets.newLinkedHashSet();
        for (int n = 0; n < numberOfCells; ++n) {
            final PieceComponent component = PieceComponent.from(check(extractPosition(matrix, n)));
            Preconditions.checkArgument(!distinctComponents.contains(component));
            distinctComponents.add(component);
        }
        return distinctComponents;
    }

    /*----------------------------------------8<----------------------------------------*/

    public final static class Factory {

        private final boolean isCaching;

        private long cacheHit = 0;

        @SuppressWarnings("unchecked")
        private final Map<Object, Object>[] cache = new Map[22]; // TODO extract constant

        public Factory(final boolean isCaching) {
            System.out.println("Loading " + this.getClass().getCanonicalName() + "...");
            for (int i = 0; i < 22; ++i) {// TODO extract constant
                this.cache[i] = Maps.newConcurrentMap();
            }
            this.isCaching = isCaching;
        }

        public Factory() {
            this(IS_FACTORY_CACHING);
        }

        private PieceInterface getFromNew(final int id, final Set<PieceInterface> components) {
            return new PieceComposite(id, components);
        }

        private PieceInterface getFromCache(final int id, final Set<PieceInterface> components) {
            return (PieceComposite) this.cache[id].get(components);
        }

        public PieceInterface get(final int id, final Set<PieceInterface> components) {
            PieceInterface instance = this.getFromCache(id, components);
            if (instance == null) {
                instance = this.getFromNew(id, components);
                if (this.isCaching) this.cache[id].put(components, instance);
            }
            else ++this.cacheHit;
            return instance;
        }

        public long cacheHits() {
            return this.cacheHit;
        }

        public int size() {
            int size = 0;
            for (final Map<?, ?> map : this.cache) {
                /*
                System.out.println(map.size());
                for (final Entry<?, ?> entry : map.entrySet()) {
                    System.out.println(entry);
                }
                */
                size += map.size();
            }
            return size;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this.getClass().getCanonicalName())
                    .add("size", this.size())
                    .add("cacheHit", this.cacheHits())
                    .toString();
        }

    }

    /*----------------------------------------8<----------------------------------------*/

    public final static Factory FACTORY = new Factory();

    /*----------------------------------------8<----------------------------------------*/

    public static PieceInterface from(final PieceData pieceData) {
        final int id = check(pieceData).ordinal();
        final int numberOfCells = check(pieceData.getNumberOfCells());
        final Matrix matrix = check(pieceData.getMatrix());
        final Set<PieceInterface> distinctComponents = check(matrix, numberOfCells);
        return FACTORY.get(id, distinctComponents);
    }

    /*----------------------------------------8<----------------------------------------*/

    private static PieceInterface from(final PieceInterface pieceInterface, final DirectionInterface direction) {
        final int id = pieceInterface.getId();
        final Set<PieceInterface> orderedComponents = Sets.newLinkedHashSet();
        for (final PieceInterface component : pieceInterface) {
            orderedComponents.add(component.translateBy(direction));
        }
        return FACTORY.get(id, orderedComponents);
    }

    // TODO !? à revoir
    // TODO !? utiliser un rotationOrdinal
    private static PieceInterface from(final PieceInterface pieceInterface, final PositionInterface referential) {
        final int id = pieceInterface.getId();
        final Set<PieceInterface> orderedComponents = Sets.newLinkedHashSet();
        for (final PieceInterface component : pieceInterface) {
            orderedComponents.add(component.rotateAround(referential));
        }
        final PieceInterface rotated = FACTORY.get(id, orderedComponents);
        final PositionInterface rotationReferential = rotated.getReferential();
        final DirectionInterface direction = Direction.from(rotationReferential, referential);
        return rotated.translateBy(direction);
    }

    /*----------------------------------------8<----------------------------------------*/

    private static Set<PositionInterface> computeCorners(final PieceInterface pieceInterface) {
        final ImmutableSortedSet.Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final PieceInterface component : pieceInterface) {
            cornersBuilder.addAll(component.getCorners());
        }
        return cornersBuilder.build();
    }

    private static Set<PositionInterface> computeSides(final PieceInterface pieceInterface) {
        final ImmutableSortedSet.Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final PieceInterface component : pieceInterface) {
            cornersBuilder.addAll(component.getSides());
        }
        return cornersBuilder.build();
    }

    private final int id;
    private final PieceInterface anchorPoint;

    private final Set<PieceInterface> components;

    private transient volatile Set<PositionInterface> corners;
    private transient volatile Set<PositionInterface> sides;
    private transient volatile Set<PositionInterface> potentialPositions;
    private transient volatile PieceInterface rotateAround;

    private PieceComposite(final int id, final Set<PieceInterface> components) {
        this.id = id;
        //this.components = ImmutableSet.copyOf(components); // LinkedHashSet iteration order is kept
        this.components = components; // LinkedHashSet iteration order is kept
        this.anchorPoint = components.iterator().next(); // TODO ? à passer en argument au constructeur
    }

    @Override
    public Set<PieceInterface> get() {
        return this.components;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public PositionInterface getReferential() {
        return this.anchorPoint.getReferential(); // TODO !? renommer en getAnchorPosition()
    }

    @Override
    public Set<PositionInterface> getCorners() {
        Set<PositionInterface> corners = this.corners;
        if (corners == null) {
            synchronized (this) {
                if ((corners = this.corners) == null) this.corners = corners = computeCorners(this);
            }
        }
        return corners;
    }

    @Override
    public Set<PositionInterface> getSides() {
        Set<PositionInterface> sides = this.sides;
        if (sides == null) {
            synchronized (this) {
                if ((sides = this.sides) == null) this.sides = sides = computeSides(this);
            }
        }
        return sides;
    }

    @Override
    public Set<PositionInterface> getPotentialPositions() {
        Set<PositionInterface> potentialPositions = this.potentialPositions;
        if (potentialPositions == null) {
            synchronized (this) {
                if ((potentialPositions = this.potentialPositions) == null)
                    this.potentialPositions = potentialPositions = Sets.difference(this.getCorners(), this.getSides());
            }
        }
        return potentialPositions;
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        return PieceComposite.from(this, direction);
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        return this.translateBy(Direction.from(this.getReferential(), position));
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        return PieceComposite.from(this, referential);
    }

    @Override
    public PieceInterface rotate() {// TODO ?! PieceTemplate
        PieceInterface rotateAround = this.rotateAround;
        if (rotateAround == null) {
            synchronized (this) {
                if ((rotateAround = this.rotateAround) == null) this.rotateAround = rotateAround = this.rotateAround(this.getReferential());
            }
        }
        return rotateAround;
    }

    /*
    @Override
    public int hashCode() {
        return 0; // TODO
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceInterface)) return false;
        final PieceInterface that = (PieceInterface) object;
        if (this.getId() != that.getId()) return false;
        if (!this.getReferential().equals(that.getReferential())) return false;

        final Iterable<PieceInterface> components = that.get();
        for (final PieceInterface pieceInterface : this) {
            //components.c
        }

        return false; // TODO
    }
    */

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.get()).toString();
    }

}