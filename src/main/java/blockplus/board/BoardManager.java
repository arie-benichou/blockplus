/*
 * Copyright 2012 Arie Benichou
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

package blockplus.board;

import java.util.List;
import java.util.Map;

import blockplus.Color;
import blockplus.direction.Direction;
import blockplus.direction.DirectionInterface;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class BoardManager {

    public final static List<DirectionInterface> CORNERS_DIRECTIONS = ImmutableList.of(
            Direction.TOP_LEFT, Direction.TOP_RIGHT, Direction.BOTTOM_LEFT, Direction.BOTTOM_RIGHT);

    public final static List<DirectionInterface> SIDES_DIRECTIONS = ImmutableList.of(
            Direction.TOP, Direction.LEFT, Direction.RIGHT, Direction.BOTTOM);

    private final Board<Color> board;

    public BoardManager(final Board<Color> board) {
        this.board = board;
    }

    // TODO ? à rendre inutile via un board de potentiels
    public List<PositionInterface> getEmptyCellPositions() {
        final List<PositionInterface> emptyCellPositions = Lists.newArrayList();
        for (int i = 0; i < this.board.rows(); ++i)
            for (int j = 0; j < this.board.columns(); ++j)
                if (this.board.get(Position.from(i, j)).equals(Color.Transparent))
                    emptyCellPositions.add(Position.from(i, j));
        return emptyCellPositions;
    }

    // TODO ! à virer
    public Map<DirectionInterface, Color> getCorners(final PositionInterface position) {
        final Map<DirectionInterface, Color> neighbours = Maps.newHashMap();
        for (final DirectionInterface direction : CORNERS_DIRECTIONS)
            neighbours.put(direction, this.board.get(position.apply(direction)));
        return neighbours;
    }

    public Map<DirectionInterface, Color> getNeighbours(final PositionInterface position, final int distance) {
        Preconditions.checkArgument(position != null);
        Preconditions.checkArgument(distance >= 0); // TODO ? vraiment nécéssaire
        final Map<DirectionInterface, Color> neighbours = Maps.newHashMap();
        for (int i = -distance; i <= distance; ++i) {
            final int ii = Math.abs(i);
            for (int j = -distance; j <= distance; ++j) {
                if (ii == distance || Math.abs(j) == distance) {
                    final Color value = this.board.get(Position.from(position.row() + i, position.column() + j));
                    // TODO ?! laisser le BoardReferee décider de ce qu'il va en faire
                    if (!value.equals(Color.Black)) neighbours.put(Direction.from(i, j), value);

                }
            }
        }
        return neighbours;
    }

    public Map<DirectionInterface, Color> getAllNeighbours(final PositionInterface position, final int distance) {
        int n = distance;
        final Map<DirectionInterface, Color> neighbours = Maps.newHashMap();
        do
            neighbours.putAll(this.getNeighbours(position, n));
        while (--n > -1);
        return neighbours;
    }

    public Map<DirectionInterface, Color> getNeighbours(final PositionInterface position, final List<DirectionInterface> directions) {
        final Map<DirectionInterface, Color> neighbours = Maps.newHashMap();
        for (final DirectionInterface direction : directions) {
            final Color value = this.board.get(position.apply(direction));
            // TODO ?! laisser le BoardReferee décider de ce qu'il va en faire
            if (value != this.board.undefinedPositionvalue()) neighbours.put(direction, value);
        }
        return neighbours;
    }

}