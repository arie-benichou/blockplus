
package game.blockplus.polyomino.projection;

import game.blockplus.polyomino.Polyomino;
import game.blockplus.polyomino.entity.Entities;
import game.blockplus.polyomino.entity.Entity;
import game.blockplus.polyomino.entity.IEntity;
import game.blockplus.polyomino.entity.Projectable;
import game.blockplus.polyomino.projection.Plane.Projection;

import java.util.Iterator;
import java.util.Set;

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

    /*
    public static void main(final String[] args) {
        int n = 0;
        final PieceInstances pieceInstances = new PieceInstances(Plane.from(20, 20));
        {
            //final Set<Polyomino> set = Polyomino.set();
            //for (final Polyomino polyomino : set) {
            //System.out.println(polyomino.ordinal() + 1 + ":\n");
            //final Iterable<Projection> instances = pieceInstances.from(Entity.from(polyomino.get()));
            final Iterable<PieceInstance> instances = pieceInstances.from(Polyomino._(2).get());
            for (final PieceInstance instance : instances) {
                System.out.println(++n + "/102");
                final Iterable<Position> positions = instance.positions();
                for (final Position position : positions) {
                    System.out.println(position);
                }
                System.out.println();
            }
            //}
        }

        {
            final Entity entity = Polyomino._1.get();
            final Iterable<PieceInstance> instances = pieceInstances.from(entity);
            for (final PieceInstance pieceInstance : instances) {
                System.out.println(PieceInstances.computeLights(entity));
            }
        }

    }
    */

    public static void main(final String[] args) {
        final Plane plane = Plane.from(20, 20);
        final PieceInstances pieceInstances = new PieceInstances(plane);
        final Entity entity = Polyomino._(14).get();
        final Iterable<PieceInstance> instances = pieceInstances.from(entity);
        final Entities entities = new Entities(plane.positions());
        System.out.println("------------8<------------");
        for (final PieceInstance pieceInstance : instances) {
            //System.out.println(pieceInstance.referential());
            //System.out.println(pieceInstance);
            //System.out.println(pieceInstance.positions());
            final Set<Integer> ids = Sets.newTreeSet();
            for (final Position p : pieceInstance) {
                ids.add(p.id());
            }
            //System.out.println(ids);
            final IEntity iEntity = entities.get(ids);
            System.out.println(Entity.render(iEntity)); // TODO render dans projection
            System.out.println("------------8<------------");
        }
    }
}