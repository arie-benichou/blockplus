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

package components.cells.immutable;

import static components.position.Position.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import components.cells.CellsInterface;
import components.position.PositionInterface;

// TODO ? provides builder instead of static factory method
public final class Cells<T> implements CellsInterface<T> {

    public static <T> CellsInterface<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> boardMutation,
            final Map<PositionInterface, T> mutationFragment) {
        return new Cells<T>(rows, columns, initialPositionvalue, undefinedPositionvalue, boardMutation, mutationFragment);
    }

    public static <T> CellsInterface<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue,
            final Map<PositionInterface, T> boardMutation) {
        return from(rows, columns, initialPositionvalue, undefinedPositionvalue, boardMutation, new HashMap<PositionInterface, T>());
    }

    public static <T> CellsInterface<T> from(
            final int rows, final int columns,
            final T initialPositionvalue,
            final T undefinedPositionvalue) {
        return from(rows, columns, initialPositionvalue, undefinedPositionvalue, new HashMap<PositionInterface, T>());
    }

    private static <T> CellsInterface<T> from(final Cells<T> board, final Map<PositionInterface, T> cells) {
        return from(board.rows(), board.columns(), board.initialSymbol(), board.undefinedSymbol(), board.boardMutation(), cells);
    }

    public static <T> CellsInterface<T> from(final Cells<T> cells) {
        return from(cells, new HashMap<PositionInterface, T>());
    }

    private final Map<PositionInterface, T> boardMutation;

    private final int rows;
    private final int columns;
    private final T initialSymbol;
    private final T undefinedSymbol;

    private volatile Integer hashCode = null;

    private static <T> Map<PositionInterface, T> merge(final T initialPositionvalue, final Map<PositionInterface, T> left, final Map<PositionInterface, T> right) {
        final Builder<PositionInterface, T> builder = new ImmutableSortedMap.Builder<PositionInterface, T>(Ordering.natural());
        final MapDifference<PositionInterface, T> difference = Maps.difference(left, right);
        for (final Entry<PositionInterface, T> mutation : difference.entriesInCommon().entrySet())
            if (!mutation.getValue().equals(initialPositionvalue)) builder.put(mutation);
        for (final Entry<PositionInterface, T> mutation : difference.entriesOnlyOnLeft().entrySet())
            if (!mutation.getValue().equals(initialPositionvalue)) builder.put(mutation);
        for (final Entry<PositionInterface, T> mutation : difference.entriesOnlyOnRight().entrySet())
            if (!mutation.getValue().equals(initialPositionvalue)) builder.put(mutation);
        for (final Entry<PositionInterface, ValueDifference<T>> mutation : difference.entriesDiffering().entrySet()) {
            final T rightValue = mutation.getValue().rightValue();
            if (!rightValue.equals(initialPositionvalue)) builder.put(mutation.getKey(), rightValue);
        }
        return builder.build();
    }

    private Cells(
            final int rows, final int columns,
            final T initial, final T undefined,
            final Map<PositionInterface, T> left, final Map<PositionInterface, T> right)
    {
        this.boardMutation = merge(initial, left, right);
        this.rows = rows;
        this.columns = columns;
        this.initialSymbol = initial;
        this.undefinedSymbol = undefined;
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

    private Map<PositionInterface, T> boardMutation() {
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
    public CellsInterface<T> apply(final Map<PositionInterface, T> updatedPositions) {
        return Cells.from(this, updatedPositions);
    }

    @Override
    public CellsInterface<T> copy() {
        return Cells.from(this);
    }

    // TODO ! add unit test
    @Override
    public Map<PositionInterface, T> filter(final Predicate<Entry<PositionInterface, T>> predicate) {
        if (predicate == null) {
            final Predicate<Entry<PositionInterface, T>> nullFilterPredicate = Predicates.alwaysTrue();
            return Maps.filterEntries(this.boardMutation(), nullFilterPredicate);
        }
        return Maps.filterEntries(this.boardMutation(), predicate);
    }

    // TODO memoize
    @Override
    public final String toString() {
        return Objects.toStringHelper(this)
                .add("rows", this.rows())
                .add("columns", this.columns())
                .add("initial", this.initialSymbol())
                .add("undefined", this.undefinedSymbol())
                .add("mutation", this.boardMutation)
                .toString();
    }

    @Override
    public int hashCode() {
        Integer value = this.hashCode;
        if (value == null) synchronized (this) {
            if ((value = this.hashCode) == null) this.hashCode = value = this.toString().hashCode();
        }
        return value;
    }

    // TODO use Guava Equivalences
    @Override
    public boolean equals(final Object object) {
        boolean isEqual = false;
        if (object != null) {
            if (object == this) isEqual = true;
            else if (object instanceof CellsInterface) {
                @SuppressWarnings("rawtypes")
                final CellsInterface other = (CellsInterface) object;
                if (this.rows == other.rows() && this.columns == other.columns()) {
                    if (this.initialSymbol().equals(other.initialSymbol())) {
                        if (this.undefinedSymbol.equals(other.undefinedSymbol())) {
                            @SuppressWarnings("unchecked")
                            final CellsInterface<T> that = other;
                            isEqual = this.boardMutation().equals(that.filter(null));
                        }
                    }
                }
            }
        }
        return isEqual;
    }

}