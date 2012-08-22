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

package blockplus.model.color;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public enum PrimeColors implements ColorInterface {

    Blue,
    NotBlue,

    Yellow,
    NotYellow,

    Red,
    NotRed,

    Green,
    NotGreen;

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean contains(final ColorInterface color) {
        return this.set().contains(color);
    }

    @Override
    public int count(final ColorInterface color) {
        return this.contains(color) ? 1 : 0;
    }

    @Override
    public boolean is(final ColorInterface other) {
        if (this.size() != other.size()) return false;
        if (!this.set().equals(other.set())) return false;
        for (final ColorInterface color : this.set())
            if (this.count(color) != other.count(color)) return false;
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ColorInterface remove(final ColorInterface color) { // TODO ? utiliser pour le not()
        if (color.is(this)) return Colors.Black;
        else return this;
    }

    @Override
    public Set<ColorInterface> set() { // TODO caching
        final ImmutableSet.Builder<ColorInterface> builder = ImmutableSet.builder();
        builder.add(this);
        return builder.build();
    }

    @Override
    public List<ColorInterface> list() { // TODO caching
        final ImmutableList.Builder<ColorInterface> builder = ImmutableList.builder();
        builder.add(this);
        return builder.build();
    }

    @Override
    public boolean isPrime() {
        return true;
    }

    @Override
    public Iterator<ColorInterface> iterator() {
        return this.list().iterator();
    }

}