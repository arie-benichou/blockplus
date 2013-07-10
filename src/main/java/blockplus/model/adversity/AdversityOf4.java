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

package blockplus.model.adversity;

import java.util.Set;

import blockplus.model.context.Color;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;

public final class AdversityOf4 implements IAdversity<Color> {

    private static final int SIDES = 4;

    public final static class Builder {

        private final Set<Color> sides = Sets.newLinkedHashSet();

        public Builder add(final Color color) {
            Preconditions.checkArgument(color != null);
            Preconditions.checkState(!this.sides.contains(color));
            this.sides.add(color);
            return this;
        }

        public Builder add(final Color... colors) {
            for (final Color color : colors) {
                this.add(color);
            }
            return this;
        }

        public AdversityOf4 build() {
            Preconditions.checkState(SIDES == this.sides.size());
            final ImmutableBiMap.Builder<Integer, Color> builder = new ImmutableBiMap.Builder<Integer, Color>();
            int i = -1;
            for (final Color color : this.sides)
                builder.put(++i, color);
            return new AdversityOf4(builder.build());
        }

    }

    private final ImmutableBiMap<Integer, Color> sides;

    private AdversityOf4(final ImmutableBiMap<Integer, Color> sides) {
        this.sides = sides;
    }

    @Override
    public Iterable<Color> sides() {
        return this.sides.values();
    }

    @Override
    public Color getOpponent(final Color color) {
        return this.sides.get((this.sides.inverse().get(color) + 1) % 4);
    }

    @Override
    public int hashCode() {
        return this.sides.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof AdversityOf4);
        final AdversityOf4 that = (AdversityOf4) object;
        return Equivalences.equals().equivalent(this.sides, that.sides);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.sides.values()).toString();
    }

}