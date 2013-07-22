
package blockplus;

import java.util.List;
import java.util.Set;

import blockplus.model.Context;
import blockplus.model.Options;

import components.cells.IPosition;

public class Main {

    public static void main(final String[] args) throws Exception {
        final Context context = new Context.Builder().build();
        final Options options = context.options();
        final List<Set<IPosition>> list = options.toList();
        for (final Set<IPosition> positions : list) {
            System.out.println(positions);
        }
        System.out.println(list.size());
    }

}