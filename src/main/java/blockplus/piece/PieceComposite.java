
package blockplus.piece;

import static blockplus.piece.PieceComponent.PieceComponent;

import java.util.Iterator;
import java.util.Set;

import blockplus.direction.Direction;
import blockplus.direction.DirectionInterface;
import blockplus.position.PositionInterface;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public final class PieceComposite implements PieceInterface {

    // use ONLY on debug purpose
    private final static boolean IS_FACTORY_CACHING = false;

    //    private final static int DIMENSION = 2;
    //    private final static int MINIMAL_NUMBER_OF_COMPONENTS = 2;
    //    private final static int MAXIMAL_NUMBER_OF_COMPONENTS = 5;
    //    private final static int MINIMAL_INDEX = 0;
    //    private final static int MAXIMAL_INDEX = MAXIMAL_NUMBER_OF_COMPONENTS - 1;

    /*
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
    */

    /*
    private static PositionInterface extractPosition(final Matrix matrix, final int n) {
        return Position(matrix.get(0, n), matrix.get(1, n));
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
    */

    /*----------------------------------------8<----------------------------------------*

    public final static class Factory { // FIXME

        private final boolean isCaching;

        private long cacheHit = 0;

        @SuppressWarnings("unchecked")
        private final Map<Object, Object>[] cache = new Map[22]; // TODO extract constant

        public Factory(final boolean isCaching) {
            //System.out.println("Loading " + this.getClass().getCanonicalName() + "...");
            for (int i = 0; i < 22; ++i) {// TODO extract constant
                this.cache[i] = Maps.newConcurrentMap();
            }
            this.isCaching = isCaching;
        }

        public Factory() {
            this(IS_FACTORY_CACHING);
        }

        private PieceInterface getFromNew(final int id, final Set<PieceInterface> components) {
            //return new PieceComposite(id, components);
            return null;
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
            else {
                if (!instance.getReferential().equals(components.iterator().next().getReferential())) {
                    System.out.println();
                    System.out.println("###########################################");
                    System.out.println(instance);
                    System.out.println("-------------------------------------------");
                    System.out.println(components);
                    System.out.println("###########################################");
                    System.out.println();
                    instance = this.getFromNew(id, components);
                }
                ++this.cacheHit;
            }
            return instance;
        }

        public long cacheHits() {
            return this.cacheHit;
        }

        public int size() {
            int size = 0;
            for (final Map<?, ?> map : this.cache)
                size += map.size();
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

    *----------------------------------------8<----------------------------------------*/

    //public final static Factory FACTORY = new Factory();

    /*----------------------------------------8<----------------------------------------*/

    /*
    public static PieceInterface from(final PieceData pieceData) {
        final int id = check(pieceData).ordinal();
        final int numberOfCells = check(pieceData.size());
        final Matrix matrix = check(pieceData.positions());
        final PositionInterface referential = extractPosition(matrix, 0); // TODO !!!
        final Set<PieceInterface> distinctComponents = check(matrix, numberOfCells);
        //return FACTORY.get(id, distinctComponents); // TODO !!!
        return new PieceComposite(id, referential, distinctComponents);
    }
    */

    /*----------------------------------------8<----------------------------------------*/

    @SuppressWarnings("all")
    public static PieceInterface PieceComposite(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
        return new PieceComposite(id, referential, positions);
    }

    public static PieceInterface from(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
        return new PieceComposite(id, referential, positions);
    }

    /*----------------------------------------8<----------------------------------------*/

    private static Set<PieceInterface> extractComponents(final Set<PositionInterface> positions) {
        final Builder<PieceInterface> builder = ImmutableSet.builder();
        for (final PositionInterface position : positions)
            builder.add(PieceComponent(position));
        return builder.build();
    }

    private static Set<PositionInterface> extractCorners(final PieceInterface pieceInterface) {
        final ImmutableSortedSet.Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final PieceInterface component : pieceInterface)
            cornersBuilder.addAll(component.getCorners());
        return cornersBuilder.build();
    }

    private static Set<PositionInterface> extractSides(final PieceInterface pieceInterface) {
        final ImmutableSortedSet.Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final PieceInterface component : pieceInterface)
            cornersBuilder.addAll(component.getSides());
        return cornersBuilder.build();
    }

    /*----------------------------------------8<----------------------------------------*/

    private final int id;

    @Override
    public int getId() {
        return this.id;
    }

    private final PositionInterface referential;

    @Override
    public PositionInterface getReferential() {
        return this.referential;
    }

    private final Set<PositionInterface> positions;

    @Override
    public Set<PositionInterface> getPositions() {
        return this.positions;
    }

    private transient volatile Set<PieceInterface> components;
    private transient volatile Set<PositionInterface> corners;
    private transient volatile Set<PositionInterface> sides;
    private transient volatile Set<PositionInterface> potentialPositions;
    private transient volatile PieceInterface rotated;

    public PieceComposite(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
        this.id = id;
        this.referential = referential;
        this.positions = ImmutableSortedSet.copyOf(positions);
    }

    @Override
    public Set<PieceInterface> get() {
        Set<PieceInterface> components = this.components;
        if (components == null) {
            synchronized (this) {
                if ((components = this.components) == null) this.components = components = extractComponents(this.positions);
            }
        }
        return components;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public Set<PositionInterface> getCorners() {
        Set<PositionInterface> corners = this.corners;
        if (corners == null) {
            synchronized (this) {
                if ((corners = this.corners) == null) this.corners = corners = extractCorners(this);
            }
        }
        return corners;
    }

    @Override
    public Set<PositionInterface> getSides() {
        Set<PositionInterface> sides = this.sides;
        if (sides == null) {
            synchronized (this) {
                if ((sides = this.sides) == null) this.sides = sides = extractSides(this);
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
        final Set<PositionInterface> positions = Sets.newHashSet();
        for (final PositionInterface position : this.getPositions())
            positions.add(position.apply(direction));
        return PieceComposite.from(this.getId(), this.getReferential().apply(direction), positions);
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        return this.translateBy(Direction.from(this.getReferential(), position));
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        final Set<PositionInterface> positions = Sets.newHashSet();
        for (final PieceInterface component : this)
            positions.addAll(component.rotateAround(referential).getPositions());
        return PieceComposite.from(this.getId(), referential, positions);
    }

    @Override
    public PieceInterface rotate() {
        PieceInterface rotated = this.rotated;
        if (rotated == null) {
            synchronized (this) {
                if ((rotated = this.rotated) == null) this.rotated = rotated = this.rotateAround(this.getReferential());
            }
        }
        return rotated;
    }

    @Override
    public int hashCode() { // TODO ! caching
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceInterface)) return false;
        final PieceInterface that = (PieceInterface) object;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqual = this.getId() == that.getId()
                //&& this.getReferential().equals(that.getReferential())
                && this.get().equals(that.get());
        Preconditions.checkState(haveSameHashCode == isEqual, "this: " + this + "\nthat: " + that);
        return isEqual;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue("\n  " + this.getId())
                //.addValue("\n  " + this.getReferential())
                .addValue("\n  " + Joiner.on("\n  ").join(this.get()) + " \n  ")
                .toString();
    }

}