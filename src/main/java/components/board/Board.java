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

package components.board;

import static components.position.Position.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import components.position.PositionInterface;

// TODO ! Builder
public final class Board<T extends Symbol> implements BoardInterface<T> {

    public static <T extends Symbol> BoardInterface<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> boardMutation,
            final Map<PositionInterface, T> mutationFragment) {
        return new Board<T>(rows, columns, initialPositionvalue, undefinedPositionvalue, boardMutation, mutationFragment);
    }

    public static <T extends Symbol> BoardInterface<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> boardMutation) {
        return from(rows, columns, initialPositionvalue, undefinedPositionvalue, boardMutation, new HashMap<PositionInterface, T>());
    }

    public static <T extends Symbol> BoardInterface<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue) {
        return from(rows, columns, initialPositionvalue, undefinedPositionvalue, new HashMap<PositionInterface, T>());
    }

    public static <T extends Symbol> BoardInterface<T> from(final Board<T> board, final Map<PositionInterface, T> cells) {
        return from(board.rows(), board.columns(), board.initialSymbol(), board.undefinedSymbol(), board.boardMutation(), cells);
    }

    public static <T extends Symbol> BoardInterface<T> from(final Board<T> board) {
        return from(board, new HashMap<PositionInterface, T>());
    }

    private final int rows;
    private final int columns;
    private final T initialSymbol;
    private final T undefinedSymbol;
    private final SortedMap<PositionInterface, T> boardMutation;

    private transient volatile Integer hashCode = null;;

    private SortedMap<PositionInterface, T> merge(final Map<PositionInterface, T> boardMutation, final Map<PositionInterface, T> mutationFragment) {
        final TreeMap<PositionInterface, T> merge = Maps.newTreeMap();
        if (mutationFragment.isEmpty()) merge.putAll(boardMutation);
        else if (boardMutation.isEmpty()) merge.putAll(mutationFragment);
        else {
            merge.putAll(boardMutation);
            for (final Entry<PositionInterface, T> mutation : mutationFragment.entrySet())
                if (mutation.getValue().equals(this.initialSymbol())) merge.remove(mutation.getKey());
                else merge.put(mutation.getKey(), mutation.getValue());
        }
        return merge;
    }

    private Board(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> boardMutation,
            final Map<PositionInterface, T> nullMutation) {
        this.rows = rows;
        this.columns = columns;
        this.initialSymbol = initialPositionvalue;
        this.undefinedSymbol = undefinedPositionvalue;
        this.boardMutation = this.merge(boardMutation, nullMutation);
    }

    @Override
    public int rows() {
        return this.rows;
    }

    @Override
    public int columns() {
        return this.columns;
    }

    @Override
    public T initialSymbol() {
        return this.initialSymbol;
    }

    @Override
    public T undefinedSymbol() {
        return this.undefinedSymbol;
    }

    private SortedMap<PositionInterface, T> boardMutation() {
        return this.boardMutation;
    }

    private T getBoardMutation(final PositionInterface position) {
        final T symbol = this.boardMutation.get(position);
        if (symbol != null) return symbol;
        return this.initialSymbol();
    }

    private boolean check(final int rowIndex, final int columnIndex) {
        return rowIndex < 0 || columnIndex < 0 || rowIndex >= this.rows() || columnIndex >= this.columns();
    }

    @Override
    public T get(final int row, final int column) {
        if (this.check(row, column)) return this.undefinedSymbol();
        return this.getBoardMutation(Position(row, column));
    }

    @Override
    public T get(final PositionInterface position) {
        if (this.check(position.row(), position.column())) return this.undefinedSymbol();
        return this.getBoardMutation(position);
    }

    @Override
    public BoardInterface<T> apply(final Map<PositionInterface, T> updatedPositions) {
        return Board.from(this, updatedPositions);
    }

    @Override
    public BoardInterface<T> copy() {
        return Board.from(this);
    }

    @Override
    // TODO add unit test
    public Map<PositionInterface, T> filter(final Predicate<Entry<PositionInterface, T>> predicate) {
        return Maps.filterEntries(this.boardMutation(), predicate);
    }

    @Override
    public final String toString() {
        return Objects.toStringHelper(this)
                .add("\n  rows", this.rows())
                .add("\n  columns", this.columns())
                .add("\n  initial", this.initialSymbol())
                .add("\n  undefined", this.undefinedSymbol())
                .add("\n  mutation", this.boardMutation + "\n")
                .toString();
    }

    @Override
    // TODO add unit test
    public int hashCode() {
        Integer value = this.hashCode;
        if (value == null) synchronized (this) {
            if ((value = this.hashCode) == null) this.hashCode = value = this.toString().hashCode();
        }
        return value;
    }

    @Override
    // TODO add unit test
    public boolean equals(final Object object) {
        boolean isEqual = false;
        if (object != null) {
            if (object == this) isEqual = true;
            else if (object instanceof BoardInterface) {
                @SuppressWarnings("rawtypes")
                final BoardInterface other = (BoardInterface) object;
                if (this.rows == other.rows() && this.columns == other.columns()) {
                    if (this.initialSymbol().equals(other.initialSymbol())) {
                        if (this.undefinedSymbol.equals(other.undefinedSymbol())) {
                            @SuppressWarnings("unchecked")
                            final BoardInterface<T> that = other;
                            final Predicate<Entry<PositionInterface, T>> nullFilterPredicate = Predicates.alwaysTrue();
                            isEqual = this.boardMutation().equals(that.filter(nullFilterPredicate));
                        }
                    }
                }
            }
        }
        return isEqual;
    }

}