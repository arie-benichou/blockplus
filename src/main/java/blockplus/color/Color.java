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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public final class Color implements ColorInterface {

    public final static class Builder {

        private final Multiset<ColorInterface> colors = HashMultiset.create();

        private void addPrimeColor(final ColorInterface primeColor) {
            this.colors.add(primeColor);
        }

        private void addColor(final ColorInterface color) {
            if (color.isPrime()) this.addPrimeColor(color);
            else for (final ColorInterface colors : color)
                this.addColor(colors);
        }

        public Builder add(final ColorInterface color) {
            this.addColor(color);
            return this;
        }

        public ColorInterface build() {
            if (this.colors.isEmpty()) return Colors.Black;
            if (this.colors.size() == 1) {
                final ColorInterface color = this.colors.iterator().next();
                if (color.isPrime()) return color;
            }
            return new Color(this);
        }

    }

    private static List<ColorInterface> computeList(final Multiset<ColorInterface> data) {
        final List<PrimeColors> primeColors = Lists.newArrayList();
        for (final Entry<ColorInterface> entry : data.entrySet())
            for (int n = 0; n < entry.getCount(); ++n)
                primeColors.add((PrimeColors) entry.getElement());
        Collections.sort(primeColors);
        final ImmutableList.Builder<ColorInterface> builder = ImmutableList.builder();
        return builder.addAll(primeColors).build();
    }

    private final Multiset<ColorInterface> colorAsMultiSet;

    private transient volatile List<ColorInterface> colorAsList;

    private Color(final Multiset<ColorInterface> data) {
        this.colorAsMultiSet = data;
    }

    private Color(final Builder builder) {
        this(builder.colors);
    }

    @Override
    public List<ColorInterface> list() {
        List<ColorInterface> value = this.colorAsList;
        if (value == null)
            synchronized (this) {
                if ((value = this.colorAsList) == null) this.colorAsList = value = computeList(this.colorAsMultiSet);
            }
        return value;
    }

    @Override
    public Set<ColorInterface> set() {
        return this.colorAsMultiSet.elementSet();
    }

    @Override
    public int size() {
        return this.colorAsMultiSet.size();
    }

    @Override
    public Iterator<ColorInterface> iterator() {
        return this.list().iterator();
    }

    @Override
    public ColorInterface remove(final ColorInterface givenColor) {
        if (!this.contains(givenColor)) return this;
        final Builder builder = new Builder();
        for (final Entry<ColorInterface> entry : this.colorAsMultiSet.entrySet()) {
            final ColorInterface color = entry.getElement();
            if (!color.is(givenColor))
                for (int i = 0; i < entry.getCount(); ++i)
                    builder.add(color);
        }
        return builder.build();
    }

    @Override
    public int count(final ColorInterface color) {
        return this.colorAsMultiSet.count(color);
    }

    @Override
    public boolean contains(final ColorInterface color) {
        return this.colorAsMultiSet.contains(color);
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
    public int hashCode() {
        return this.list().toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        boolean isEqual = false;
        if (object != null) {
            if (object == this) isEqual = true;
            else if (object instanceof ColorInterface) isEqual = this.is((ColorInterface) object);
        }
        return isEqual;
    }

    @Override
    public boolean isEmpty() {
        return this.colorAsMultiSet.isEmpty();
    }

    @Override
    public boolean isPrime() {
        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.list().toString()).toString();
    }

}