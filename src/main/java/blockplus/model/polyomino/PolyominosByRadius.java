
package blockplus.model.polyomino;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public final class PolyominosByRadius {

    private final static PolyominosByRadius INSTANCE = new PolyominosByRadius();

    public static PolyominosByRadius getInstance() {
        return INSTANCE;
    }

    private final ImmutableSortedMap<Integer, ImmutableSortedSet<Polyomino>> polyominosByRadius;

    private PolyominosByRadius() {
        final Map<Integer, ImmutableSortedSet.Builder<Polyomino>> tmpMap = Maps.newTreeMap();
        final TreeSet<Integer> radius = Sets.newTreeSet();
        for (final Polyomino polyomino : Polyomino.set()) {
            radius.add(polyomino.radius());
        }
        for (final Integer integer : radius) {
            tmpMap.put(integer, new ImmutableSortedSet.Builder<Polyomino>(Ordering.natural()));
        }
        for (final Polyomino polyomino : Polyomino.set()) {
            final ImmutableSortedSet.Builder<Polyomino> builder = tmpMap.get(polyomino.radius());
            builder.add(polyomino);
        }
        final Builder<Integer, ImmutableSortedSet<Polyomino>> builder = new ImmutableSortedMap.Builder
                                                                        <Integer, ImmutableSortedSet<Polyomino>>(Ordering.natural());
        for (final Entry<Integer, ImmutableSortedSet.Builder<Polyomino>> entry : tmpMap.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().build());
        }
        this.polyominosByRadius = builder.build();
    }

    public Iterable<Polyomino> getByRadius(final int radius) {
        return this.polyominosByRadius.get(radius);
    }

    public ImmutableSortedMap<Integer, ImmutableSortedSet<Polyomino>> getAllByRadius() {
        return this.polyominosByRadius;
    }

}