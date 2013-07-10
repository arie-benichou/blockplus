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

package components.cells;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Maps;
import components.cells.Positions.Position;

public class Neighbourhood {

    private final Map<Position, Map<Integer, Iterable<Position>>> NEIGHBOURS_BY_RADIUS_BY_POSITION = Maps.newTreeMap();

    private final Positions positions;

    public Neighbourhood(final Positions positions) {
        this.positions = positions;
    }

    private Position getNeighbour(final Position position, final int rowDelta, final int columnDelta) {
        return this.positions.get(position.row() + rowDelta, position.column() + columnDelta);
    }

    private Iterable<Position> computeNeighbours(final Position position, final int radius) {
        final Builder<Position> builder = ImmutableList.builder();
        for (int i = -radius; i <= radius; ++i) {
            final int absi = Math.abs(i);
            for (int j = -radius; j <= radius; ++j) {
                if (absi == radius || Math.abs(j) == radius) {
                    final Position neighbour = this.getNeighbour(position, i, j);
                    if (!neighbour.isNull()) builder.add(neighbour);
                }
            }
        }
        return builder.build();
    }

    public Iterable<Position> getNeighboursPositions(final Position position, final int radius) {
        Map<Integer, Iterable<Position>> neighboursByRadius = this.NEIGHBOURS_BY_RADIUS_BY_POSITION.get(position);
        if (neighboursByRadius == null) this.NEIGHBOURS_BY_RADIUS_BY_POSITION.put(position, neighboursByRadius = Maps.newTreeMap());
        Iterable<Position> neighbours = neighboursByRadius.get(radius);
        if (neighbours == null) neighboursByRadius.put(radius, neighbours = this.computeNeighbours(position, radius));
        return neighbours;
    }

}