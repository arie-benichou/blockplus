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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;

public final class SidesOrdering {

    private static final int SIDES = 4;

    public final static class Builder {

        private final Set<Colors> sides = Sets.newLinkedHashSet();

        public Builder add(final Colors color) {
            Preconditions.checkArgument(color != null);
            Preconditions.checkState(!this.sides.contains(color));
            this.sides.add(color);
            return this;
        }

        public SidesOrdering build() {
            Preconditions.checkState(SIDES == this.sides.size());
            final ImmutableBiMap.Builder<Integer, Colors> builder = new ImmutableBiMap.Builder<Integer, Colors>();
            int i = -1;
            for (final Colors color : this.sides)
                builder.put(++i, color);
            return new SidesOrdering(builder.build());
        }

    }

    private final ImmutableBiMap<Integer, Colors> sides;

    private SidesOrdering(final ImmutableBiMap<Integer, Colors> sides) {
        this.sides = sides;
    }

    public Set<Colors> sides() {
        return this.sides.values();
    }

    public Colors next(final Colors color) {
        return this.sides.get((this.sides.inverse().get(color) + 1) % SIDES);
    }

    public Colors next(final int i) {
        return this.sides.get((i + 1) % SIDES);
    }

}