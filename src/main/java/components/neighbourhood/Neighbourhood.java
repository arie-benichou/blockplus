
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