
package blockplus.piece;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import blockplus.direction.Direction;
import blockplus.direction.DirectionInterface;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Piece implements PieceInterface {

    private final static boolean IS_FACTORY_CACHING = true;

    private static String asString(final int id, final PositionInterface referential, final int rotationOrdinal) {
        return Objects.toStringHelper(Piece.class)
                .add("id", id)
                .add("referential", referential)
                .add("rotation", rotationOrdinal)
                .toString();
    }

    private static int hashCode(final int id, final PositionInterface referential, final int rotationOrdinal) {
        return asString(id, referential, rotationOrdinal).hashCode();
    }

    public final static class Factory {

        private final boolean isCaching;

        private long cacheHit = 0;

        private final Map<Integer, PieceInterface> cache = Maps.newConcurrentMap();

        public Factory(final boolean isCaching) {
            this.isCaching = isCaching;
        }

        public Factory() {
            this(IS_FACTORY_CACHING);
        }

        public PieceInterface get(final int id, final PositionInterface referential, final int rotationOrdinal) {
            final int hashCode = Piece.hashCode(id, referential, rotationOrdinal);
            PieceInterface instance = this.cache.get(hashCode);
            if (instance == null) {

                if (rotationOrdinal != 0) {
                    instance = this.get(id, referential, 0);
                }

                final Piece piece = (Piece) Pieces.get(id);

                if (instance == null) {
                    final Set<PieceInterface> rotations = piece.get();
                    final Set<PieceInterface> translatedComponents = Sets.newLinkedHashSet();
                    for (final PieceInterface pieceInterface : rotations) {
                        translatedComponents.add(pieceInterface.translateTo(referential));
                    }
                    final Iterable<PieceInterface> components = Iterables.limit(
                            Iterables.skip(Iterables.cycle(translatedComponents), rotationOrdinal), rotations.size());
                    instance = new Piece(piece.getPieceData(), rotationOrdinal, Sets.newLinkedHashSet(components));
                }
                else {
                    final Iterable<PieceInterface> components = Iterables.limit(
                            Iterables.skip(Iterables.cycle(instance.get()), rotationOrdinal), instance.get().size());
                    instance = new Piece(piece.getPieceData(), rotationOrdinal, Sets.newLinkedHashSet(components));
                }

                if (this.isCaching) {
                    this.cache.put(hashCode, instance); // TODO ...
                }

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

    /*----------------------------------------8<----------------------------------------*/

    public final static Factory FACTORY = new Factory();

    /*----------------------------------------8<----------------------------------------*/

    @SuppressWarnings("all")
    public static PieceInterface Piece(final PieceData pieceData) {
        final int rotationOrdinal = 0;
        final PieceInterface rotation0 = PieceComposite.from(pieceData.id(), pieceData.referential(), pieceData.positions());
        final PieceInterface rotation1 = rotation0.rotate();
        final PieceInterface rotation2 = rotation1.rotate();
        final PieceInterface rotation3 = rotation2.rotate();
        final Set<PieceInterface> components = Sets.newLinkedHashSet();
        components.add(rotation0);
        components.add(rotation1);
        components.add(rotation2);
        components.add(rotation3);
        final Piece piece = new Piece(pieceData, rotationOrdinal, components);
        FACTORY.cache.put(hashCode(pieceData.id(), pieceData.referential(), rotationOrdinal), piece);
        return piece;
    }

    @SuppressWarnings("all")
    public static PieceInterface Piece(final Piece piece) {
        final Set<PieceInterface> rotations = piece.get();
        final int rotationOrdinal = (piece.getRotationOrdinal() + 1) % rotations.size();
        return FACTORY.get(piece.getId(), piece.getReferential(), rotationOrdinal);
    }

    @SuppressWarnings("all")
    public static PieceInterface Piece(final Piece piece, final DirectionInterface direction) {
        return FACTORY.get(piece.getId(), piece.getReferential().apply(direction), piece.getRotationOrdinal());
    }

    private final PieceData pieceData;
    private final int rotationOrdinal;
    private final Set<PieceInterface> components;

    public static long counter = 0;

    public Piece(final PieceData pieceData, final int rotationOrdinal, final Iterable<PieceInterface> components) {
        ++counter;
        this.pieceData = pieceData;
        this.rotationOrdinal = rotationOrdinal;
        this.components = ImmutableSet.copyOf(components);
    }

    public PieceData getPieceData() {
        return this.pieceData;
    }

    public int getRotationOrdinal() {
        return this.rotationOrdinal;
    }

    @Override
    public Set<PieceInterface> get() { // TODO Iterable<PI>
        return this.components;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public int getId() {
        return this.pieceData.id();
    }

    @Override
    public PositionInterface getReferential() {
        return this.iterator().next().getReferential();
    }

    @Override
    public Set<PositionInterface> getPositions() {
        return this.iterator().next().getPositions();
    }

    @Override
    public Set<PositionInterface> getCorners() {
        return this.iterator().next().getCorners();
    }

    @Override
    public Set<PositionInterface> getSides() {
        return this.iterator().next().getSides();
    }

    @Override
    public Set<PositionInterface> getPotentialPositions() {
        return this.iterator().next().getPotentialPositions();
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        final PositionInterface referential = this.iterator().next().getReferential();
        final DirectionInterface direction = Direction.from(referential, position);
        return Piece(this, direction);
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        return Piece(this, direction);
    }

    @Override
    public PieceInterface rotate() {
        return Piece(this);
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        return null; // TODO
    }

    @Override
    public String toString() {
        return asString(this.getId(), this.getReferential(), this.getRotationOrdinal());
    }

    public static void main(final String[] args) {

        final PieceInterface rotated0 = Piece(PieceData.get(5));

        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println(rotated0);
        for (final PieceInterface pieceInterface : rotated0)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated1 = rotated0.rotate();
        System.out.println(rotated1);
        for (final PieceInterface pieceInterface : rotated1)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated2 = rotated1.rotate();
        System.out.println(rotated2);
        for (final PieceInterface pieceInterface : rotated2)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated3 = rotated2.rotate();
        System.out.println(rotated3);
        for (final PieceInterface pieceInterface : rotated3)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println();
        final PieceInterface rotated4 = rotated3.rotate();
        System.out.println(rotated4);
        for (final PieceInterface pieceInterface : rotated4)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();

        final PieceInterface translateTo = rotated4.translateTo(Position.from(5, 5));
        System.out.println(translateTo.get());
        System.out.println(translateTo);

        rotated3.rotate().rotate().rotate().rotate().rotate();
        rotated4.translateTo(Position.from(5, 5));
        System.out.println(FACTORY);

    }

}