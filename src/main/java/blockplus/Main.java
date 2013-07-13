
package blockplus;

import java.util.List;
import java.util.Set;

import blockplus.model.Context;
import blockplus.model.Context.Builder;
import blockplus.model.OptionsSupplier;
import blockplus.model.interfaces.IOptionsSupplier;
import blockplus.model.polyomino.Polyomino;

import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;
import components.cells.IPosition;

public class Main {

    public static void main(final String[] args) throws Exception {

        final IOptionsSupplier optionsSupplier = new OptionsSupplier();
        final Context context = new Builder().setOptionsSupplier(optionsSupplier).build();
        final TreeBasedTable<IPosition, Polyomino, List<Set<IPosition>>> options =
                                                                                   (TreeBasedTable<IPosition, Polyomino, List<Set<IPosition>>>)
                                                                                   context.options();
        int n = 0;
        for (final Cell<IPosition, Polyomino, List<Set<IPosition>>> object : options.cellSet()) {
            final List<Set<IPosition>> value = object.getValue();
            for (final Iterable<IPosition> positions : value) {
                System.out.println(positions);
                ++n;
            }

        }
        System.out.println(n);
    }
}