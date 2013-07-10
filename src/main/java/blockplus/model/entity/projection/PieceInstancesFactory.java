
package blockplus.model.entity.projection;


import blockplus.model.entity.Polyomino;
import blockplus.model.entity.projection.PieceInstances.PieceInstance;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;

public final class PieceInstancesFactory {

    private final ImmutableSortedMap<Polyomino, Iterable<PieceInstance>> map;

    public PieceInstancesFactory(final PieceInstances pieceInstances) {
        final Builder<Polyomino, Iterable<PieceInstance>> builder = new ImmutableSortedMap.Builder<Polyomino, Iterable<PieceInstance>>(Ordering.natural());
        for (final Polyomino polyomino : Polyomino.set()) {
            final Iterable<PieceInstance> instances = pieceInstances.from(polyomino.get());
            builder.put(polyomino, instances);
        }
        this.map = builder.build();
    }

    public Iterable<PieceInstance> get(final Polyomino polyomino) {
        return this.map.get(polyomino);
    }

}