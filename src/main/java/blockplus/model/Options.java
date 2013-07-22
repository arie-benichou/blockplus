
package blockplus.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.model.polyomino.Polyomino;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import components.cells.IPosition;

// TODO à compléter
public class Options {

    private final Table<IPosition, Polyomino, List<Set<IPosition>>> options;

    public Options(final Table<IPosition, Polyomino, List<Set<IPosition>>> options) {
        this.options = options;
    }

    public boolean isEmpty() {
        return this.options.isEmpty();
    }

    public List<Set<IPosition>> toList() {
        final List<Set<IPosition>> list = Lists.newArrayList();
        for (final Cell<IPosition, Polyomino, List<Set<IPosition>>> object : this.options.cellSet()) {
            final List<Set<IPosition>> value = object.getValue();
            for (final Set<IPosition> positions : value) {
                list.add(positions);
            }
        }
        return list;
    }

    public Set<Entry<Polyomino, Map<IPosition, List<Set<IPosition>>>>> byPolyomino() {
        return this.options.columnMap().entrySet();
    }

}