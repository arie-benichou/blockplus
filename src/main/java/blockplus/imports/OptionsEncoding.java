
package blockplus.imports;

import static components.cells.Positions.Position;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.model.Options;
import blockplus.model.polyomino.Polyomino;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import components.cells.IPosition;

public class OptionsEncoding {

    public OptionsEncoding() {}

    public Options decode(final JsonObject data) {
        final Table<IPosition, Polyomino, List<Set<IPosition>>> table = TreeBasedTable.create();
        final Set<Entry<String, JsonElement>> entrySet = data.entrySet();
        for (final Entry<String, JsonElement> entry : entrySet) {
            final int id = Integer.parseInt(entry.getKey());
            final IPosition light = Position(id / 20, id % 20); // TODO !!!
            final JsonObject polyominos = entry.getValue().getAsJsonObject();
            for (final Entry<String, JsonElement> positionsByPolyominos : polyominos.entrySet()) {
                final String ordinal = positionsByPolyominos.getKey();
                final Polyomino polyomino = Polyomino.values()[Integer.parseInt(ordinal)];
                final JsonArray positions = positionsByPolyominos.getValue().getAsJsonArray();
                final List<Set<IPosition>> list = Lists.newArrayList();
                for (final JsonElement jsonElement : positions) {
                    final Set<IPosition> set = Sets.newHashSet();
                    final JsonArray asJsonArray = jsonElement.getAsJsonArray();
                    for (final JsonElement jsonElement2 : asJsonArray) {
                        final int asInt = jsonElement2.getAsInt();
                        final IPosition p = Position(asInt / 20, asInt % 20); // TODO !!!
                        set.add(p);
                    }
                    list.add(set);
                }
                table.put(light, polyomino, list);
            }
        }
        return new Options(table);
    }

}