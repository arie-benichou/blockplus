/*
 * Copyright 2012-2013 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.model.polyomino;

import java.util.Set;
import java.util.SortedSet;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;
import components.cells.Directions.Direction;
import components.cells.IPosition;
import components.cells.Positions.Position;

public final class PolyominoInstances implements Supplier<Iterable<PolyominoInstance>> {

    public static PolyominoInstances from(final PolyominoProperties properties) {
        return new PolyominoInstances(properties);
    }

    private static IPosition flipPosition(final IPosition position, final IPosition referential) {
        final int row = position.row();
        final int column = 2 * referential.column() - position.column();
        return new Position(row, column);
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
        return new Position(row, column);
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
            newPositions.add(((Position) position).apply(direction));
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
                positions.add(new Position(position.row() + deltaRow, position.column() + deltaColumn));
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