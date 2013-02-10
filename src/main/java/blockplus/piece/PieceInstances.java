
package blockplus.piece;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

/**
 * Provides distinct instances for a given piece.
 */
//TODO extract interface
public final class PieceInstances implements Iterable<PieceInterface> {

    private static Set<PieceInterface> computeDistinctRotation(final PieceInterface oneSide) {
        final Set<PieceInterface> distinctRotationsForOneSide = Sets.newLinkedHashSet();
        distinctRotationsForOneSide.add(oneSide);
        final PieceInterface rotation1 = oneSide.rotate();
        distinctRotationsForOneSide.add(rotation1);
        final PieceInterface rotation2 = rotation1.rotate();
        distinctRotationsForOneSide.add(rotation2);
        final PieceInterface rotation3 = rotation2.rotate();
        distinctRotationsForOneSide.add(rotation3);
        return distinctRotationsForOneSide;
    }

    private static Set<PieceInterface> computeDistinctInstancesForEachSide(final int piece) {
        final Set<PieceInterface> distinctInstances = Sets.newLinkedHashSet();
        final PieceData pieceData = PieceData.PieceData(piece); // TODO à revoir
        final PieceInterface thisSide = PieceComposite.from(pieceData.id(), pieceData.referential(), pieceData.positions());
        distinctInstances.addAll(computeDistinctRotation(thisSide));
        final PieceInterface thatSide = thisSide.reflectAlongVerticalAxis(); // TODO ! PieceInterface.flip();
        distinctInstances.addAll(computeDistinctRotation(thatSide));
        return distinctInstances;
    }

    private static Map<Integer, PieceInterface> computeDistinctInstances(final PieceInstances pieceInstances) {
        final Builder<Integer, PieceInterface> builder = new ImmutableSortedMap.Builder<Integer, PieceInterface>(Ordering.natural());
        final Set<PieceInterface> distinctInstances = computeDistinctInstancesForEachSide(pieceInstances.getPiece());
        int id = -1;
        for (final PieceInterface instance : distinctInstances)
            builder.put(++id, instance);
        return builder.build();
    }

    private final int piece;

    public int getPiece() {
        return this.piece;
    }

    private volatile Map<Integer, PieceInterface> distinctInstances = null;

    // TODO passer un symbole Piece
    public PieceInstances(final int piece) {
        this.piece = piece;
    }

    private Map<Integer, PieceInterface> getDistinctInstances() {
        Map<Integer, PieceInterface> instances = this.distinctInstances;
        if (instances == null)
            synchronized (this) {
                if ((instances = this.distinctInstances) == null) this.distinctInstances = instances = computeDistinctInstances(this);
            }
        return this.distinctInstances;
    }

    public int getNumberOfDistinctInstances() {
        return this.getDistinctInstances().size();
    }

    public PieceInterface getDistinctInstance(final int instance) {
        return this.getDistinctInstances().get(instance);
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.getDistinctInstances().values().iterator();
    }

}