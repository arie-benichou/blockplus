
package blockplus.model.entity.projection;


import java.util.Iterator;
import java.util.Set;

import blockplus.model.entity.entity.IEntity;
import blockplus.model.entity.entity.Projectable;
import blockplus.model.entity.projection.Plane.Projection;

import com.google.common.collect.Sets;
import components.cells.Positions.Position;

public class PieceInstances {

    public class PieceInstance implements Projectable {

        private final Projection projection;

        public final IEntity entity; // TODO

        public PieceInstance(final IEntity entity, final Projection projection) {
            this.entity = entity;
            this.projection = projection;
        }

        @Override
        public Iterator<Position> iterator() {
            return this.projection.iterator();
        }

        @Override
        public Position referential() {
            return this.projection.referential();
        }

        @Override
        public Iterable<Position> positions() {
            return this.projection.positions();
        }

        public PieceInstance on(final Position position) {
            return new PieceInstance(this.entity, this.projection.on(position));
        }

    }

    private final Plane plane;

    public PieceInstances(final Plane plane) {
        this.plane = plane;
    }

    private Set<Projection> computeDistinctRotations(final Projection projection, final Set<Projection> projections) {
        final Projection newProjection = projection.rotate();
        final Projection normalizedProjection = this.normalize(newProjection);
        if (projections.contains(normalizedProjection)) return projections;
        projections.add(normalizedProjection);
        return this.computeDistinctRotations(newProjection, projections);
    }

    private Projection normalize(final Projection newProjection) {

        final int minRow = newProjection.minRow(); // TODO minTopLeftCorner()
        final int minColumn = newProjection.minColumn(); // TODO minTopLeftCorner()

        final Set<Position> newTreeSet = Sets.newTreeSet();
        for (final Position position : newProjection)
            newTreeSet.add(this.plane.positions().get(position.row() - minRow, position.column() - minColumn));

        final Position ref = this.plane.positions().get(newProjection.referential().row() - minRow, newProjection.referential().column() - minColumn);

        final Projectable projectable = new Projectable() {

            @Override
            public Iterator<Position> iterator() {
                return this.positions().iterator();
            }

            @Override
            public Position referential() {
                return ref;
            }

            @Override
            public Iterable<Position> positions() {
                return newTreeSet;
            }
        };

        return this.plane.project(projectable);
    }

    // TODO lazy    
    public Iterable<PieceInstance> from(final IEntity entity) {
        final Set<Projection> projections = Sets.newLinkedHashSet();
        final Projection thisSide = this.plane.project(entity).on(entity.radius(), entity.radius());
        final Projection thatSide = thisSide.flip();
        this.computeDistinctRotations(thisSide, projections);
        this.computeDistinctRotations(thatSide, projections);
        final Set<PieceInstance> instances = Sets.newLinkedHashSet(); // TODO ? immutable
        for (final Projection projection : projections) {
            instances.add(new PieceInstance(entity, projection));
        }
        return instances;
    }

}