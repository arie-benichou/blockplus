
package blockplus;

import java.util.List;
import java.util.Set;

import blockplus.model.Context;
import blockplus.model.OptionsSupplier;
import blockplus.model.Context.Builder;
import blockplus.model.entity.IEntity;
import blockplus.model.entity.PieceInstances;
import blockplus.model.entity.Plane;
import blockplus.model.entity.Polyomino;
import blockplus.model.interfaces.IOptionsSupplier;

import com.google.common.collect.Sets;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;
import components.cells.Positions.Position;

public class Main {

    public static void main(final String[] args) throws Exception {

        final IOptionsSupplier optionsSupplier = new OptionsSupplier(new PieceInstances(Plane.from(20, 20)));
        final Context context = new Builder().setOptionsSupplier(optionsSupplier).build();
        final TreeBasedTable<Position, Polyomino, List<Set<Position>>> options =
                                                                                 (TreeBasedTable<Position, Polyomino, List<Set<Position>>>)
                                                                                 context.options();
        int n = 0;
        for (final Cell<Position, Polyomino, List<Set<Position>>> object : options.cellSet()) {
            final List<Set<Position>> value = object.getValue();
            for (final Iterable<Position> position : value) {
                final Set<Integer> positions = Sets.newTreeSet();
                for (final Position p : position) {
                    positions.add(p.id());
                    System.out.print(p.id() + " ");
                }
                System.out.println();
                final IEntity entity = context.ENTITIES.get(positions);
                //System.out.println(Entity.render(entity)); // TODO à revoir
                ++n;
            }

        }
        System.out.println(n);
    }
}