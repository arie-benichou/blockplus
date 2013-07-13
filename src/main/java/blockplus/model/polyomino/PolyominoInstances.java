
package blockplus.model.polyomino;

import java.util.Set;
import java.util.SortedSet;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.PolyominoProperties.Location;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import components.cells.Directions.Direction;
import components.cells.IPosition;

public final class PolyominoInstances implements Supplier<Iterable<PolyominoInstance>> {

    private static IPosition flipPosition(final IPosition position, final IPosition referential) {
        final int row = position.row();
        final int column = 2 * referential.column() - position.column();
        return new Location(row, column);
    }

    private static Iterable<IPosition> flipPositions(final Iterable<IPosition> positions, final IPosition referential) {
        final Set<IPosition> newPositions = Sets.newTreeSet();
        for (final IPosition position : positions)
            newPositions.add(flipPosition(position, referential));
        return newPositions;
    }

    private static PolyominoInstance flipInstance(final PolyominoInstance instance) {
        final IPosition referential = instance.referential();
        final Iterable<IPosition> positions = flipPositions(instance.positions(), referential);
        final Iterable<IPosition> shadows = flipPositions(instance.shadows(), referential);
        final Iterable<IPosition> lights = flipPositions(instance.lights(), referential);
        return new PolyominoInstance(referential, positions, shadows, lights);
    }

    private static IPosition rotatePosition(final IPosition position, final IPosition referential) {
        final int tmpRow = position.row();
        final int row = -position.column() + referential.column() + referential.row();
        final int column = tmpRow + referential.column() - referential.row();
        return new Location(row, column);
    }

    private static Iterable<IPosition> rotatePositions(final Iterable<IPosition> positions, final IPosition referential) {
        final Set<IPosition> newPositions = Sets.newTreeSet();
        for (final IPosition position : positions)
            newPositions.add(rotatePosition(position, referential));
        return newPositions;
    }

    private static PolyominoInstance rotateInstance(final PolyominoInstance instance) {
        final IPosition referential = instance.referential();
        final Iterable<IPosition> positions = rotatePositions(instance.positions(), referential);
        final Iterable<IPosition> shadows = rotatePositions(instance.shadows(), referential);
        final Iterable<IPosition> lights = rotatePositions(instance.lights(), referential);
        return new PolyominoInstance(referential, positions, shadows, lights);
    }

    private static Iterable<PolyominoInstance> computeDistinctRotations(final PolyominoInstance instance, final Set<PolyominoInstance> instances) {
        final PolyominoInstance newInstance = rotateInstance(instance);
        if (instances.contains(newInstance)) return instances;
        instances.add(newInstance);
        return computeDistinctRotations(newInstance, instances);
    }

    private static Iterable<PolyominoInstance> computeDistinctInstances(final PolyominoProperties properties) {
        final Set<PolyominoInstance> instances = Sets.newLinkedHashSet(); // TODO TreeSet
        final PolyominoInstance head = new PolyominoInstance(
                properties.referential(),
                properties.positions(),
                properties.shadows(),
                properties.lights()
                                       );
        computeDistinctRotations(head, instances);
        final PolyominoInstance tail = flipInstance(head);
        computeDistinctRotations(tail, instances);
        return instances;
    }

    private static Iterable<IPosition> translatePositions(final Iterable<IPosition> positions, final Direction direction) {
        final Set<IPosition> newPositions = Sets.newTreeSet();
        for (final IPosition position : positions)
            newPositions.add(((Location) position).apply(direction));
        return newPositions;
    }

    public static PolyominoTranslatedInstance translate(final PolyominoInstance instance, final Direction direction) {
        final Iterable<IPosition> positions = translatePositions(instance.positions(), direction);
        final Iterable<IPosition> shadows = translatePositions(instance.shadows(), direction);
        final Iterable<IPosition> lights = translatePositions(instance.lights(), direction);
        return new PolyominoTranslatedInstance(positions, shadows, lights);
    }

    public final static class PolyominoInstance {

        private final IPosition referential;
        private final Iterable<IPosition> positions;
        private final Iterable<IPosition> shadows;
        private final Iterable<IPosition> lights;

        private volatile String rendering;
        private volatile Integer hashCode;

        public PolyominoInstance(final IPosition referential, final Iterable<IPosition> positions, final Iterable<IPosition> shadows,
                final Iterable<IPosition> lights) {
            this.referential = referential;
            this.positions = positions;
            this.shadows = shadows;
            this.lights = lights;
        }

        public IPosition referential() {
            return this.referential;
        }

        public Iterable<IPosition> positions() {
            return this.positions;
        }

        public SortedSet<IPosition> apply(final IPosition referential) {
            final int deltaRow = referential.row() - this.referential().row();
            final int deltaColumn = referential.column() - this.referential().column();
            final SortedSet<IPosition> positions = Sets.newTreeSet();
            for (final IPosition position : this.positions())
                positions.add(new Location(position.row() + deltaRow, position.column() + deltaColumn));
            return positions;
        }

        public Iterable<IPosition> shadows() {
            return this.shadows;
        }

        public Iterable<IPosition> lights() {
            return this.lights;
        }

        @Override
        public String toString() {
            String rendering = this.rendering;
            if (rendering == null) {
                synchronized (this) {
                    if ((rendering = this.rendering) == null) this.rendering = rendering = PolyominoRenderer.render(this);
                }
            }
            return rendering;
        }

        @Override
        public int hashCode() {
            Integer hashCode = this.hashCode;
            if (hashCode == null) {
                synchronized (this) {
                    if ((hashCode = this.hashCode) == null) this.hashCode = hashCode = this.toString().hashCode();
                }
            }
            return hashCode;
        }

        @Override
        public boolean equals(final Object that) {
            Preconditions.checkArgument(that instanceof PolyominoInstance);
            return this.hashCode() == that.hashCode();
        }

    }

    public final static class PolyominoTranslatedInstance {

        private final Iterable<IPosition> positions;
        private final Iterable<IPosition> shadows;
        private final Iterable<IPosition> lights;

        public PolyominoTranslatedInstance(final Iterable<IPosition> positions, final Iterable<IPosition> shadows, final Iterable<IPosition> lights) {
            this.positions = positions;
            this.shadows = shadows;
            this.lights = lights;
        }

        public Iterable<IPosition> positions() {
            return this.positions;
        }

        public Iterable<IPosition> shadows() {
            return this.shadows;
        }

        public Iterable<IPosition> lights() {
            return this.lights;
        }

    }

    private final PolyominoProperties properties;
    private final Iterable<PolyominoInstance> instances;

    public PolyominoInstances(final PolyominoProperties properties) {
        this.properties = properties;
        this.instances = computeDistinctInstances(this.properties);
    }

    @Override
    public Iterable<PolyominoInstance> get() {
        return this.instances;
    }

}