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
import blockplus.piece.PieceTemplateInterface;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

// TODO ? avoir un board de Color
// TODO nettoyage de la classe
public final class Board {

    public final static List<DirectionInterface> CORNERS_DIRECTIONS = ImmutableList.of(
            Direction.TOP_LEFT,
            Direction.TOP_RIGHT,
            Direction.BOTTOM_LEFT,
            Direction.BOTTOM_RIGHT
            );

    public final static List<DirectionInterface> SIDES_DIRECTIONS = ImmutableList.of(
            Direction.TOP,
            Direction.LEFT,
            Direction.RIGHT,
            Direction.BOTTOM
            );

    public final static int WIDTH = 7;
    public final static int HEIGHT = 7;
    public final static int SIZE = WIDTH * HEIGHT;

    public final static int EMPTY = 1;
    private final static int UNDEFINED = -1;

    private final int[][] data = new int[HEIGHT][WIDTH];

    public static Board from(final String data) {
        return new Board(data);
    }

    public static Board from(final char[] array) {
        return from(new String(array));
    }

    private Board(final String data) {
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                this.data[i][j] = Integer.parseInt(String.valueOf(data.charAt(WIDTH * i + j)));
    }

    private Board(final int[][] board) {
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                this.data[i][j] = board[i][j];
    }

    public int getCell(final int y, final int x) {

        // TODO pouvoir injecter les "special cases" au board
        if (y == -1 && x == -1) return 2 * 3 * 5 * 7;
        if (y == -1 && x == WIDTH) return 2 * 3 * 5 * 7;
        if (y == HEIGHT && x == -1) return 2 * 3 * 5 * 7;
        if (y == HEIGHT && x == WIDTH) return 2 * 3 * 5 * 7;

        if (y < 0) return UNDEFINED;
        if (x < 0) return UNDEFINED;
        if (y >= HEIGHT) return UNDEFINED;
        if (x >= WIDTH) return UNDEFINED;

        return this.data[y][x];
    }

    public int getCell(final PositionInterface position) {
        Preconditions.checkArgument(position != null);
        return this.getCell(position.row(), position.column());
    }

    public int getIndexFromPosition(final PositionInterface position) {
        return WIDTH * position.row() + position.column();
    }

    public PositionInterface getPositionFromIndex(final int index) {
        return Position.from(index / WIDTH, index % WIDTH);
    }

    public int getCell(final int index) {
        return this.getCell(index / WIDTH, index % WIDTH);
    }

    public boolean isEmpty(final int y, final int x) {
        return this.getCell(y, x) == EMPTY;
    }

    public boolean isEmpty(final int index) {
        return this.isEmpty(index / WIDTH, index % WIDTH);
    }

    public boolean isEmpty(final PositionInterface position) {
        return this.getCell(position) == EMPTY;
    }

    public List<Integer> getEmptyCells() {
        final List<Integer> emptyCells = Lists.newArrayList();
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                if (this.data[i][j] == EMPTY)
                    emptyCells.add(WIDTH * i + j);
        return emptyCells;
    }

    public List<PositionInterface> getEmptyCellPositions() {
        final List<PositionInterface> emptyCellPositions = Lists.newArrayList();
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                if (this.data[i][j] == EMPTY)
                    emptyCellPositions.add(Position.from(i, j));
        return emptyCellPositions;
    }

    // TODO NeighbourhoodFeature
    public Map<DirectionInterface, Integer> getCorners(final int y, final int x) {
        final Map<DirectionInterface, Integer> neighbours = Maps.newHashMap();
        for (final DirectionInterface direction : CORNERS_DIRECTIONS)
            neighbours.put(direction, this.getCell(y + direction.rowDelta(), x + direction.columnDelta()));
        return neighbours;
    }

    // TODO NeighbourhoodFeature
    public Map<DirectionInterface, Integer> getCorners(final int index) {
        return this.getCorners(index / WIDTH, index % WIDTH);
    }

    public Map<DirectionInterface, Integer> getCorners(final PositionInterface position) {
        return this.getCorners(position.row(), position.column());
    }

    // TODO NeighbourhoodFeature
    public Map<DirectionInterface, Integer> getNeighbours(final PositionInterface position, final int distance) {
        Preconditions.checkArgument(position != null);
        Preconditions.checkArgument(distance >= 0);
        final Map<DirectionInterface, Integer> neighbours = Maps.newHashMap();
        for (int i = -distance; i <= distance; ++i) {
            final int ii = Math.abs(i);
            for (int j = -distance; j <= distance; ++j) {
                if (ii == distance || Math.abs(j) == distance) {
                    final int value = this.getCell(position.row() + i, position.column() + j);
                    if (value != UNDEFINED) neighbours.put(Direction.from(i, j), value);
                }
            }
        }
        return neighbours;
    }

    // TODO NeighbourhoodFeature
    public Map<DirectionInterface, Integer> getNeighbours(final PositionInterface position) {
        return this.getNeighbours(position, 1);
    }

    // TODO NeighbourhoodFeature
    public Map<DirectionInterface, Integer> getAllNeighbours(final PositionInterface position, final int distance) {
        int n = distance;
        final Map<DirectionInterface, Integer> neighbours = Maps.newHashMap();
        do
            neighbours.putAll(this.getNeighbours(position, n));
        while (--n > -1);
        return neighbours;
    }

    // TODO NeighbourhoodFeature
    private Map<DirectionInterface, Integer> getNeighbours(final PositionInterface position, final List<DirectionInterface> directions) {
        final Map<DirectionInterface, Integer> neighbours = Maps.newHashMap();
        for (final DirectionInterface direction : directions) {
            final int value = this.getCell(position.row() + direction.rowDelta(), position.column() + direction.columnDelta());
            if (value != UNDEFINED) neighbours.put(direction, value);
        }
        return neighbours;
    }

    // TODO NeighbourhoodFeature    
    public Map<DirectionInterface, Integer> getCornerNeighbours(final PositionInterface position) {
        return this.getNeighbours(position, CORNERS_DIRECTIONS);
    }

    // TODO NeighbourhoodFeature    
    public Map<DirectionInterface, Integer> getSides(final PositionInterface position) {
        return this.getNeighbours(position, SIDES_DIRECTIONS);
    }

    public int[][] getFragment(final int boxingSquareSide, final List<PositionInterface> positions) {
        Preconditions.checkArgument(boxingSquareSide > 0);
        Preconditions.checkArgument(positions != null);
        final int[][] data = new int[boxingSquareSide][boxingSquareSide];
        int n = -1;
        for (int i = 0; i < boxingSquareSide; ++i) {
            for (int j = 0; j < boxingSquareSide; ++j) {
                final PositionInterface position = positions.get(++n);
                data[i][j] = this.getCell(position.row(), position.column());
            }
        }
        return data;
    }

    public char[] toCharArray() {
        final char[] array = new char[HEIGHT * WIDTH];
        for (int i = 0; i < HEIGHT; ++i)
            for (int j = 0; j < WIDTH; ++j)
                array[WIDTH * i + j] = String.valueOf(this.getCell(i, j)).charAt(0);
        return array;
    }

    // TODO ? utiliser un objet mutation pour éviter le couplage entre board et les autres objets
    public Board put(final Color color, final PieceTemplateInterface piece, final PositionInterface position) {
        final Board board = new Board(this.data);
        for (final PositionInterface coordinates : piece.getPositions(position))
            board.data[coordinates.row()][coordinates.column()] = color.getValue();
        return board;
    }

}