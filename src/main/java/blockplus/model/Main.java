
package blockplus.model;


import java.util.List;
import java.util.Set;

import blockplus.model.context.Context;
import blockplus.model.context.ContextBuilder;
import blockplus.model.entity.Polyomino;
import blockplus.model.entity.entity.IEntity;
import blockplus.model.entity.projection.PieceInstances;
import blockplus.model.entity.projection.Plane;
import blockplus.model.option.IOptionsSupplier;
import blockplus.model.option.OptionsSupplier;

import com.google.common.collect.Sets;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;
import components.cells.Positions.Position;

public class Main {

    public static void main(final String[] args) throws Exception {

        final IOptionsSupplier optionsSupplier = new OptionsSupplier(new PieceInstances(Plane.from(20, 20)));
        final Context context = new ContextBuilder().setOptionsSupplier(optionsSupplier).build();
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