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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

public final class Cells<T> implements ICells<T> {

    public static <T> ICells<T> from(
            final Positions cellPositions,
            final T initialSymbol,
            final T undefinedSymbol,
            final Map<IPosition, T> cells,
            final Map<IPosition, T> cellsMutations) {
        return new Cells<T>(cellPositions, initialSymbol, undefinedSymbol, cells, cellsMutations);
    }

    public static <T> ICells<T> from(
            final Positions cellPositions,
            final T initialSymbol,
            final T undefinedSymbol,
            final Map<IPosition, T> cells) {
        return from(cellPositions, initialSymbol, undefinedSymbol, cells, new HashMap<IPosition, T>());
    }

    public static <T> ICells<T> from(
            final Positions cellPositions,
            final T initialSymbol,
            final T undefinedSymbol) {
        return from(cellPositions, initialSymbol, undefinedSymbol, new HashMap<IPosition, T>());
    }

    private static <T> ICells<T> from(final Cells<T> cells, final Map<IPosition, T> cellsMutations) {
        return from(cells.cellPositions, cells.initialSymbol(), cells.undefinedSymbol(), cells.get(), cellsMutations);
    }

    public static <T> ICells<T> from(final Cells<T> cells) {
        return from(cells, new HashMap<IPosition, T>());
    }

    private final Positions cellPositions;
    private final Map<IPosition, T> cells;
    private final T initialSymbol;
    private final T undefinedSymbol;

    private volatile Integer hashCode = null;

    private static <T> Map<IPosition, T> merge(final T initialSymbol, final Map<IPosition, T> left, final Map<IPosition, T> right) {
        final Builder<IPosition, T> builder = new ImmutableSortedMap.Builder<IPosition, T>(Ordering.natural());
        final MapDifference<IPosition, T> difference = Maps.difference(left, right);
        for (final Entry<IPosition, T> mutation : difference.entriesInCommon().entrySet())
            if (!mutation.getValue().equals(initialSymbol)) builder.put(mutation);
        for (final Entry<IPosition, T> mutation : difference.entriesOnlyOnLeft().entrySet())
            if (!mutation.getValue().equals(initialSymbol)) builder.put(mutation);
        for (final Entry<IPosition, T> mutation : difference.entriesOnlyOnRight().entrySet())
            if (!mutation.getValue().equals(initialSymbol)) builder.put(mutation);
        for (final Entry<IPosition, ValueDifference<T>> mutation : difference.entriesDiffering().entrySet()) {
            final T rightValue = mutation.getValue().rightValue();
            if (!rightValue.equals(initialSymbol)) builder.put(mutation.getKey(), rightValue);
        }
        return builder.build();
    }

    private Cells(
            final Positions cellPositions,
            final T initialSymbol, final T undefinedSymbol,
            final Map<IPosition, T> left, final Map<IPosition, T> right)
    {
        this.cellPositions = cellPositions;
        this.initialSymbol = initialSymbol;
        this.undefinedSymbol = undefinedSymbol;
        this.cells = merge(initialSymbol, left, right);
    }

    private Positions positions() {
        return this.cellPositions;
    }

    @Override
    public IPosition position(final int row, final int column) {
        return this.positions().get(row, column);
    }

    /*
    @Override
    public Position position(final int id) {
        return this.positions().get(id);
    }
    */

    @Override
    public int rows() {
        return this.positions().rows();
    }

    @Override
    public int columns() {
        return this.positions().columns();
    }

    @Override
    public T initialSymbol() {
        return this.initialSymbol;
    }

    @Override
    public T undefinedSymbol() {
        return this.undefinedSymbol;
    }

    @Override
    public Map<IPosition, T> get() {
        return this.cells;
    }

    // TODO initialSymbol could be removed, just use initial symbol
    private T getCellSymbol(final IPosition position) {
        final T symbol = this.cells.get(position);
        if (symbol != null) return symbol;
        return this.initialSymbol();
    }

    private boolean isDefined(final int row, final int column) {
        return row > -1 && column > -1 && row < this.rows() && column < this.columns();
    }

    @Override
    public T get(final IPosition position) {
        //if (position.isNull()) return this.undefinedSymbol();
        if (this.isDefined(position.row(), position.column())) return this.getCellSymbol(position);
        return this.undefinedSymbol();
    }

    @Override
    public T get(final int row, final int column) {
        final IPosition position = this.positions().get(row, column);
        return this.get(position);
    }

    /*
    @Override
    public T get(final int id) {
        return this.get(this.positions().get(id));
    }
    */

    @Override
    public ICells<T> apply(final Map<IPosition, T> updatedPositions) {
        return Cells.from(this, updatedPositions);
    }

    @Override
    public ICells<T> copy() {
        return Cells.from(this);
    }

    @Override
    public Map<IPosition, T> filter(final Predicate<Entry<IPosition, T>> predicate) {
        Preconditions.checkArgument(predicate != null);
        return Maps.filterEntries(this.get(), predicate); // TODO ? allow predicate on initial symbol
    }

    // TODO memoize
    @Override
    public final String toString() {
        return Objects.toStringHelper(this)
                .add("rows", this.rows())
                .add("columns", this.columns())
                .add("initial", this.initialSymbol())
                .add("undefined", this.undefinedSymbol())
                .add("mutation", this.cells)
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

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof ICells, object);
        if (object == this) return true;
        final ICells that = (ICells) object;
        return this.rows() == that.rows()
                && this.columns() == that.columns()
                && this.initialSymbol().equals(that.initialSymbol())
                && this.undefinedSymbol().equals(that.undefinedSymbol())
                && this.get().equals(that.get());
    }

}