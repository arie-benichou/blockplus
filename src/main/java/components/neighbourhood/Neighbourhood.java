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

package components.neighbourhood;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Maps;
import components.position.PositionInterface;

public final class Neighbourhood {

    private final static Map<PositionInterface, Map<Integer, List<PositionInterface>>> NEIGHBOURS_BY_RADIUS_BY_POSITION = Maps.newConcurrentMap();

    private static List<PositionInterface> computeNeighbours(final PositionInterface position, final int radius) {
        final Builder<PositionInterface> builder = ImmutableList.builder();
        for (int i = -radius; i <= radius; ++i) {
            final int ii = Math.abs(i);
            for (int j = -radius; j <= radius; ++j)
                if (ii == radius || Math.abs(j) == radius) builder.add(position.apply(i, j));
        }
        return builder.build();
    }

    public static List<PositionInterface> getNeighboursPositions(final PositionInterface position, final int radius) {
        Map<Integer, List<PositionInterface>> neighboursByRadius = NEIGHBOURS_BY_RADIUS_BY_POSITION.get(position);
        if (neighboursByRadius == null) NEIGHBOURS_BY_RADIUS_BY_POSITION.put(position, neighboursByRadius = Maps.newConcurrentMap());
        List<PositionInterface> neighbours = neighboursByRadius.get(radius);
        if (neighbours == null) neighboursByRadius.put(radius, neighbours = computeNeighbours(position, radius));
        return neighbours;
    }

    private Neighbourhood() {}

}