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

import static blockplus.position.Position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import blockplus.position.PositionInterface;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class Board<T> {

    private static <T> SortedMap<PositionInterface, T> buildData(
            final int rows,
            final int columns,
            final T initialPositionvalue,
            final Map<PositionInterface, T> definedPosition) {
        final TreeMap<PositionInterface, T> data = Maps.newTreeMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j)
                data.put(Position(i, j), initialPositionvalue);
        data.putAll(definedPosition);
        return data;
    }

    private static <T> SortedMap<PositionInterface, T> mergeData(
            final Map<PositionInterface, T> definedPosition,
            final Map<PositionInterface, T> updatedPositions) {
        final TreeMap<PositionInterface, T> data = Maps.newTreeMap();
        data.putAll(definedPosition);
        data.putAll(updatedPositions);
        return data;
    }

    public static <T> Board<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> definedPositions,
            final Map<PositionInterface, T> updatedPositions) {
        return new Board<T>(rows, columns, initialPositionvalue, undefinedPositionvalue, definedPositions, updatedPositions);
    }

    public static <T> Board<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> definedPositions) {
        return from(rows, columns, initialPositionvalue, undefinedPositionvalue, definedPositions, new HashMap<PositionInterface, T>());
    }

    public static <T> Board<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue) {
        return from(rows, columns, initialPositionvalue, undefinedPositionvalue, new HashMap<PositionInterface, T>());
    }

    public static <T> Board<T> from(final Board<T> board, final Map<PositionInterface, T> cells) {
        return from(board.rows(), board.columns(), board.initialPositionvalue(), board.undefinedPositionvalue(), board.data(), cells);
    }

    private final int rows;
    private final int columns;

    private final T initialPositionvalue;

    private T initialPositionvalue() {
        return this.initialPositionvalue;
    }

    private final T undefinedPositionvalue;

    public T undefinedPositionvalue() {
        return this.undefinedPositionvalue;
    }

    private final SortedMap<PositionInterface, T> data;

    private SortedMap<PositionInterface, T> data() {
        return this.data;
    }

    private Board(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> definedPositions,
            final Map<PositionInterface, T> updatedPositions) {
        this.rows = rows;
        this.columns = columns;
        this.initialPositionvalue = initialPositionvalue;
        this.undefinedPositionvalue = undefinedPositionvalue;
        this.data = updatedPositions.isEmpty()
                ? buildData(rows, columns, initialPositionvalue, definedPositions)
                : mergeData(definedPositions, updatedPositions);
    }

    private Board(final Board<T> board) {
        this.rows = board.rows();
        this.columns = board.columns;
        this.initialPositionvalue = board.initialPositionvalue();
        this.undefinedPositionvalue = board.undefinedPositionvalue();
        this.data = Maps.newTreeMap(board.data);
    }

    public int rows() {
        return this.rows;
    }

    public int columns() {
        return this.columns;
    }

    public T get(final PositionInterface position) {
        final T value = this.data().get(position);
        return value == null ? this.undefinedPositionvalue() : value;
    }

    public Board<T> update(final Map<PositionInterface, T> updatedPositions) {
        return Board.from(this, updatedPositions);
    }

    public Board<T> copy() {
        return new Board<T>(this);
    }

    @Override
    public final String toString() {
        final String lineSeparator = "\n" + " " + Strings.repeat("----", this.columns()) + "-" + "\n";
        final String columnSeparator = " | ";
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows(); ++i) {
            sb.append(lineSeparator);
            for (int j = 0; j < this.columns(); ++j) {
                sb.append(columnSeparator);
                sb.append(this.get(Position(i, j)));
            }
            sb.append(columnSeparator);
        }
        sb.append(lineSeparator);
        return sb.toString();
    }

    // TODO ! à revoir
    // TODO ! appartient à Position
    // TODO ! caching
    public List<PositionInterface> getNeighboursPositions(final PositionInterface position, final int radius) {
        final List<PositionInterface> neighbours = Lists.newArrayList();
        for (int i = -radius; i <= radius; ++i) {
            final int ii = Math.abs(i);
            for (int j = -radius; j <= radius; ++j)
                if (ii == radius || Math.abs(j) == radius) neighbours.add(position.apply(i, j));
        }
        return neighbours;
    }

    // TODO ! à revoir
    // TODO ! appartient à Position
    // TODO ! caching
    public List<PositionInterface> getAllNeighboursPositions(final PositionInterface position, final int radius) {
        Preconditions.checkArgument(position != null);
        int n = radius;
        final List<PositionInterface> neighbours = Lists.newArrayList();
        do {
            neighbours.addAll(this.getNeighboursPositions(position, n));
        } while (--n > -1);
        return neighbours;
    }

}