
package game.blockplus.polyomino.projection;

import game.blockplus.polyomino.Polyomino;
import game.blockplus.polyomino.entity.Entity;
import game.blockplus.polyomino.entity.Projectable;

import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import components.cells.Positions;
import components.cells.Positions.Position;

public final class Plane {

    public final static Plane from(final int rows, final int columns) {
        return new Plane(rows, columns);
    }

    private final Positions positions;

    public Positions positions() {
        return this.positions;
    }

    private Plane(final int rows, final int columns) {
        this.positions = new Positions(rows, columns);
    }

    public Projection project(final Projectable projectable) {
        return new Projection(projectable);
    }

    private Iterable<Position> computePositions(final Projection projection) {
        final Position referential = projection.projectable.referential();
        final int deltaRow = projection.referential().row() - referential.row();
        final int deltaColumn = projection.referential().column() - referential.column();
        final Set<Position> newPositions = Sets.newTreeSet();
        for (final Position position : projection.projectable.positions()) {
            int row = position.row() + deltaRow;
            int column = position.column() + deltaColumn;
            if (projection.flip) {
                column = 2 * projection.referential().column() - column;
            }
            if (projection.rotate) {
                final int tmpRow = row;
                row = -column + projection.referential().column() + projection.referential().row();
                column = tmpRow + projection.referential().column() - projection.referential().row();
            }
            final Position newPosition = this.positions().get(row, column);
            if (newPosition.isNull()) {
                newPositions.clear();
                break;
            }
            else newPositions.add(newPosition);
        }
        return newPositions;
    }

    public final class Projection implements Projectable {

        private final Projectable projectable;
        private final Position referential;

        private final boolean flip;

        private volatile Iterable<Position> positions;
        private final boolean rotate;

        @Override
        public Position referential() {
            return this.referential;
        }

        @Override
        public Iterable<Position> positions() {
            Iterable<Position> positions = this.positions;
            if (positions == null) {
                synchronized (this) {
                    if ((positions = this.positions) == null) this.positions = positions = Plane.this.computePositions(this);
                }
            }
            return positions;
        }

        private Projection(final Projectable projectable, final Position referential, final boolean flip, final boolean rotate) {
            this.projectable = projectable;
            this.referential = referential;
            this.flip = flip;
            this.rotate = rotate;
        }

        private Projection(final Projectable projectable, final Position referential, final boolean flip) {
            this(projectable, referential, flip, false);
        }

        private Projection(final Projectable projectable, final Position referential) {
            this(projectable, referential, false);
        }

        private Projection(final Projectable projectable) {
            this(projectable, projectable.referential());
        }

        public Projection on(final int row, final int column) {
            final Position position = Plane.this.positions().get(row, column);
            if (position.isNull()) return null; // TODO NullProjection            
            if (position.equals(this.referential())) return this;
            return new Projection(this, position);
        }

        public Projection on(final Position position) {
            return this.on(position.row(), position.column());
        }

        public Projection flip() {
            return new Projection(this, this.referential, true);
        }

        public Projection rotate() {
            return new Projection(this, this.referential, false, true);
        }

        @Override
        public Iterator<Position> iterator() {
            return this.positions().iterator();
        }

        @Override
        public int hashCode() {
            // TODO memoize
            return this.positions().toString().hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (other == this) return true;
            if (other == null || !(other instanceof Projection)) return false;
            return this.hashCode() == other.hashCode();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("referential", this.referential())
                    .add("positions", this.positions())
                    .toString();
        }

        public int minRow() {
            int min = Integer.MAX_VALUE;
            for (final Position position : this) {
                if (position.row() < min) min = position.row();
            }
            return min;
        }

        public int minColumn() {
            int min = Integer.MAX_VALUE;
            for (final Position position : this) {
                if (position.column() < min) min = position.column();
            }
            return min;
        }

    }

    public static void main(final String[] args) {
        final Plane plane = Plane.from(20, 20);
        final Entity entity = Polyomino._3.get();
        {
            final Projection projection = plane.project(entity);
            for (final Position position : projection) {
                System.out.println(position);
            }
        }
        System.out.println();
        {
            final Projection projection = plane.project(entity).on(5, 5);
            for (final Position position : projection) {
                System.out.println(position);
            }
        }
        System.out.println();
        {
            final Projection projection = plane.project(entity).on(5, 5).flip();
            for (final Position position : projection) {
                System.out.println(position);
            }
        }
        System.out.println();
        {
            final Projection projection = plane.project(entity).on(5, 5).flip().rotate();
            for (final Position position : projection) {
                System.out.println(position);
            }
        }

        System.out.println();
        {
            final Projection projection1 = plane.project(entity).on(5, 5);
            final Projection projection2 = plane.project(entity).on(5, 5).flip();
            System.out.println(projection1.equals(projection2));
        }

    }

}