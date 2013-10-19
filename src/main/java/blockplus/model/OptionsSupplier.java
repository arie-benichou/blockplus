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

package blockplus.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import blockplus.model.polyomino.Polyomino;
import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.Polyominos;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import components.cells.IPosition;

public final class OptionsSupplier implements Supplier<Options> {

    private final static SortedMap<Integer, Set<Polyomino>> POLYOMINOS_BY_RADIUS = Polyominos.getInstance().byRadius();
    private final static int MIN_RADIUS = Math.max(0, POLYOMINOS_BY_RADIUS.firstKey());
    private final static int MAX_RADIUS = POLYOMINOS_BY_RADIUS.lastKey();

    private final Context context;

    public OptionsSupplier(final Context context) {
        this.context = context;
    }

    private Map<IPosition, Set<IPosition>> getPotentialPositionsByLight(final Board board, final Colors color, final Iterable<IPosition> lights,
            final int radius) {
        final Map<IPosition, Set<IPosition>> map = Maps.newLinkedHashMap();
        for (final IPosition light : lights) {
            final Set<IPosition> potentialReferentialPositions = Sets.newTreeSet();
            for (int k = 0; k <= radius; ++k)
                for (final IPosition neighbour : board.neighbours(light, k))
                    if (board.isMutable(color, neighbour)) potentialReferentialPositions.add(neighbour);
            map.put(light, potentialReferentialPositions);
        }
        return map;
    }

    private List<Set<IPosition>> getLegalPositions(final Colors color, final Board board, final IPosition position, final IPosition potentialPosition,
            final Iterable<PolyominoInstance> instances) {
        final List<Set<IPosition>> options = Lists.newArrayList();
        for (final PolyominoInstance instance : instances) {
            final SortedSet<IPosition> positions = instance.apply(potentialPosition);
            if (positions.contains(position) && board.isLegal(color, positions)) options.add(positions);
        }
        return options;
    }

    @Override
    public Options get() {
        final Colors color = this.context.side();
        final Board board = this.context.board();
        final Iterable<IPosition> lights = board.getLights(color);
        final Pieces remainingPieces = this.context.getPlayer().remainingPieces();
        final Table<IPosition, Polyomino, List<Set<IPosition>>> table = TreeBasedTable.create();
        for (int radius = MIN_RADIUS; radius <= MAX_RADIUS; ++radius) {
            final Map<IPosition, Set<IPosition>> potentialPositions = this.getPotentialPositionsByLight(board, color, lights, radius);
            final Set<Polyomino> polyominos = POLYOMINOS_BY_RADIUS.get(radius);
            for (final Polyomino polyomino : polyominos) {
                if (remainingPieces.contains(polyomino)) {
                    final Iterable<PolyominoInstance> instances = polyomino.get();
                    for (final Entry<IPosition, Set<IPosition>> entry : potentialPositions.entrySet()) {
                        final IPosition position = entry.getKey();
                        final List<Set<IPosition>> options = Lists.newArrayList();
                        for (final IPosition potentialPosition : entry.getValue())
                            options.addAll(this.getLegalPositions(color, board, position, potentialPosition, instances));
                        if (!options.isEmpty()) table.put(position, polyomino, options);
                    }
                }
            }
        }
        return new Options(table);
    }

}