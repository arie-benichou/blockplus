
package game.blockplus.polyomino;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public final class Polyominos {

    private final static Polyominos INSTANCE = new Polyominos();

    public static Polyominos getInstance() {
        return INSTANCE;
    }

    private final ImmutableSortedMap<Integer, Polyomino> polyominoById;
    private final ImmutableSortedMap<Integer, ImmutableSortedSet<Polyomino>> polyominosByRadius;

    private Polyominos() {
        final Builder<Integer, Polyomino> builder1 = new ImmutableSortedMap.Builder<Integer, Polyomino>(Ordering.natural());
        final Map<Integer, ImmutableSortedSet.Builder<Polyomino>> tmpMap = Maps.newTreeMap();
        final TreeSet<Integer> radius = Sets.newTreeSet();
        for (final Polyomino polyomino : Polyomino.set()) {
            builder1.put(polyomino.get().type(), polyomino);
            radius.add(polyomino.get().radius());
        }
        this.polyominoById = builder1.build();
        for (final Integer integer : radius) {
            tmpMap.put(integer, new ImmutableSortedSet.Builder<Polyomino>(Ordering.natural()));
        }
        for (final Polyomino polyomino : Polyomino.set()) {
            final ImmutableSortedSet.Builder<Polyomino> builder = tmpMap.get(polyomino.get().radius());
            builder.add(polyomino);
        }
        final Builder<Integer, ImmutableSortedSet<Polyomino>> builder2 = new ImmutableSortedMap.Builder
                                                                         <Integer, ImmutableSortedSet<Polyomino>>(Ordering.natural());
        for (final Entry<Integer, ImmutableSortedSet.Builder<Polyomino>> entry : tmpMap.entrySet()) {
            builder2.put(entry.getKey(), entry.getValue().build());
        }
        this.polyominosByRadius = builder2.build();
    }

    public Polyomino getByTypeId(final int typeId) {
        return this.polyominoById.get(typeId);
    }

    public Iterable<Polyomino> getByRadius(final int radius) {
        return this.polyominosByRadius.get(radius);
    }

    public ImmutableSortedMap<Integer, ImmutableSortedSet<Polyomino>> getByRadius() {
        return this.polyominosByRadius;
    }

    public static void main(final String[] args) {
        final Polyominos polyominos = Polyominos.getInstance();
        System.out.println(polyominos.getByRadius(0));
        System.out.println(polyominos.getByRadius(1));
        System.out.println(polyominos.getByRadius(2));
    }

}