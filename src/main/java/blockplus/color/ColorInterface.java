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

package blockplus.color;

import java.util.List;
import java.util.Set;

import components.board.Symbol;

public interface ColorInterface extends Symbol, Iterable<ColorInterface> {

    int size();

    int count(ColorInterface color);

    boolean contains(ColorInterface color);

    boolean is(ColorInterface color);

    boolean isEmpty();

    ColorInterface remove(ColorInterface color);

    Set<ColorInterface> set();

    List<ColorInterface> list();

    boolean isPrime();

}