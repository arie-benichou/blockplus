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

package blockplus.model;

import java.util.Set;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;

public final class Adversity {

    private static final int SIDES = 4;

    public final static class Builder {

        private final Set<Colors> sides = Sets.newLinkedHashSet();

        public Builder add(final Colors color) {
            Preconditions.checkArgument(color != null);
            Preconditions.checkState(!this.sides.contains(color));
            this.sides.add(color);
            return this;
        }

        public Builder add(final Colors... colors) {
            for (final Colors color : colors) {
                this.add(color);
            }
            return this;
        }

        public Adversity build() {
            Preconditions.checkState(SIDES == this.sides.size());
            final ImmutableBiMap.Builder<Integer, Colors> builder = new ImmutableBiMap.Builder<Integer, Colors>();
            int i = -1;
            for (final Colors color : this.sides)
                builder.put(++i, color);
            return new Adversity(builder.build());
        }

    }

    private final ImmutableBiMap<Integer, Colors> sides;

    private Adversity(final ImmutableBiMap<Integer, Colors> sides) {
        this.sides = sides;
    }

    public Iterable<Colors> sides() {
        return this.sides.values();
    }

    public Colors getOpponent(final Colors color) {
        return this.sides.get((this.sides.inverse().get(color) + 1) % 4);
    }

    @Override
    public int hashCode() {
        return this.sides.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof Adversity);
        final Adversity that = (Adversity) object;
        return Equivalences.equals().equivalent(this.sides, that.sides);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.sides.values()).toString();
    }

}