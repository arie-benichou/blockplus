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

package blockplus.model.polyomino;

import static components.cells.Directions.Direction;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import components.cells.IPosition;

public final class Polyominos {

    private final static Polyominos INSTANCE = new Polyominos();

    public static Polyominos getInstance() {
        return INSTANCE;
    }

    private final Map<String, Polyomino> typeIndexes;
    private final Map<String, PolyominoInstance> instanceIndexes;
    private final SortedMap<Integer, Set<Polyomino>> radiusIndexes;

    private static SortedMap<Integer, Set<Polyomino>> computeRadiusIndexes(final Set<Integer> radius) {
        final Map<Integer, ImmutableSortedSet.Builder<Polyomino>> tmpMap = Maps.newTreeMap();
        for (final Integer integer : radius)
            tmpMap.put(integer, new ImmutableSortedSet.Builder<Polyomino>(Ordering.natural()));
        for (final Polyomino polyomino : Polyomino.set())
            tmpMap.get(polyomino.radius()).add(polyomino);
        final Builder<Integer, Set<Polyomino>> builder = new ImmutableSortedMap.Builder<Integer, Set<Polyomino>>(Ordering.natural());
        for (final Entry<Integer, ImmutableSortedSet.Builder<Polyomino>> entry : tmpMap.entrySet())
            builder.put(entry.getKey(), entry.getValue().build());
        return builder.build();
    }

    private Polyominos() {
        final Builder<String, Polyomino> typesBuilder = new ImmutableSortedMap.Builder<String, Polyomino>(Ordering.natural());
        final Builder<String, PolyominoInstance> instancesBuilder = new ImmutableSortedMap.Builder<String, PolyominoInstance>(Ordering.natural());
        final Set<Integer> radius = Sets.newTreeSet();
        for (final Polyomino polyomino : Polyomino.set()) {
            for (final PolyominoInstance instance : polyomino.get()) {
                final String representation = instance.toString();
                typesBuilder.put(representation, polyomino);
                instancesBuilder.put(representation, instance);
                radius.add(polyomino.radius());
            }
        }
        this.typeIndexes = typesBuilder.build();
        this.instanceIndexes = instancesBuilder.build();
        this.radiusIndexes = computeRadiusIndexes(radius);
    }

    public PolyominoInstance getInstance(final String normalizedInstanceProjection) {
        return this.instanceIndexes.get(normalizedInstanceProjection);
    }

    public Polyomino getType(final String normalizedInstanceProjection) {
        return this.typeIndexes.get(normalizedInstanceProjection);
    }

    public SortedMap<Integer, Set<Polyomino>> byRadius() {
        return this.radiusIndexes;
    }

    private PolyominoTranslatedInstance computeTranslatedInstance(final SortedSet<IPosition> positions, final PolyominoInstance instanceFromPositions,
            final Polyomino type) {
        if (positions.isEmpty()) return PolyominoInstances.translate(instanceFromPositions, Direction(0, 0), type);
        final IPosition position1 = positions.first();
        final IPosition position2 = ((SortedSet<IPosition>) instanceFromPositions.positions()).first();
        final int rowDelta = position1.row() - position2.row();
        final int columnDelta = position1.column() - position2.column();
        return PolyominoInstances.translate(instanceFromPositions, Direction(rowDelta, columnDelta), type);
    }

    // TODO memoize
    public PolyominoTranslatedInstance get(final SortedSet<IPosition> positions) {
        final String rendering = PolyominoRenderer.render(positions);
        return this.computeTranslatedInstance(positions, this.getInstance(rendering), this.getType(rendering));
    }

}