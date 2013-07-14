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
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

public interface ICells<T> extends Supplier<Map<IPosition, T>> {

    int rows();

    int columns();

    @Override
    Map<IPosition, T> get();

    T get(int row, int column);

    T get(IPosition position);

    ICells<T> apply(Map<IPosition, T> mutations);

    ICells<T> copy();

    T undefinedSymbol();

    T initialSymbol();

    Map<IPosition, T> filter(Predicate<Entry<IPosition, T>> predicate);

}