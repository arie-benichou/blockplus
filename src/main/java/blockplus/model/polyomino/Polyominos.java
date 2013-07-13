
package blockplus.model.polyomino;

import java.util.Map;
import java.util.SortedSet;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;
import blockplus.model.polyomino.PolyominoProperties.Location;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;
import components.cells.Directions;
import components.cells.IPosition;

// TODO persist translated entities
public final class Polyominos {

    private final static Polyominos INSTANCE = new Polyominos();

    public static Polyominos getInstance() {
        return INSTANCE;
    }

    private final Map<String, Polyomino> typeIndexes;
    private final Map<String, PolyominoInstance> instanceIndexes;

    private Polyominos() {
        final Builder<String, Polyomino> typesBuilder = new ImmutableSortedMap.Builder<String, Polyomino>(Ordering.natural());
        final Builder<String, PolyominoInstance> instancesBuilder = new ImmutableSortedMap.Builder<String, PolyominoInstance>(Ordering.natural());
        for (final Polyomino polyomino : Polyomino.set()) {
            for (final PolyominoInstance instance : polyomino.get()) {
                final String representation = instance.toString();
                typesBuilder.put(representation, polyomino);
                instancesBuilder.put(representation, instance);
            }
        }
        this.typeIndexes = typesBuilder.build();
        this.instanceIndexes = instancesBuilder.build();
    }

    public PolyominoInstance getInstance(final String normalizedInstanceProjection) {
        return this.instanceIndexes.get(normalizedInstanceProjection);
    }

    public Polyomino getType(final String normalizedInstanceProjection) {
        return this.typeIndexes.get(normalizedInstanceProjection);
    }

    public PolyominoTranslatedInstance computeTranslatedInstance(final SortedSet<IPosition> positions, final PolyominoInstance instanceFromPositions) {
        if (positions.isEmpty()) return null; // TODO
        final IPosition position1 = positions.first();
        final IPosition position2 = ((SortedSet<IPosition>) instanceFromPositions.positions()).first();
        final int rowDelta = position1.row() - position2.row();
        final int columnDelta = position1.column() - position2.column();
        return PolyominoInstances.translate(instanceFromPositions, Directions.get(rowDelta, columnDelta));
    }

    public static void main(final String[] args) {
        final Polyomino polyomino = Polyomino._3;
        System.out.println(polyomino);
        final String string = polyomino.get().iterator().next().toString();
        System.out.println(string);
        final Polyominos polyominos = Polyominos.getInstance();
        final PolyominoInstance instance = polyominos.getInstance(string);
        final SortedSet<IPosition> positions = instance.apply(new Location(5, 5));
        final String rendering = PolyominoRenderer.render(positions);
        final PolyominoInstance instanceFromPositions = polyominos.getInstance(rendering);
        final PolyominoTranslatedInstance translatedInstance = polyominos.computeTranslatedInstance(positions, instanceFromPositions);
        System.out.println(translatedInstance.positions());
        System.out.println(translatedInstance.shadows());
        System.out.println(translatedInstance.lights());
    }

}