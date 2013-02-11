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

package blockplus.adversity;

import interfaces.adversity.AdversityInterface;

import java.util.Set;

import blockplus.Color;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Sets;

public final class AdversityOf4 implements AdversityInterface<Color> {

    private static final int SIDES = 4;

    public final static class Builder {

        private final Set<Color> sides = Sets.newLinkedHashSet();

        public Builder add(final Color color) {
            Preconditions.checkArgument(color != null);
            Preconditions.checkArgument(!this.sides.contains(color));
            this.sides.add(color);
            return this;
        }

        public Builder remove(final Color color) {
            Preconditions.checkArgument(color != null);
            Preconditions.checkArgument(this.sides.contains(color));
            this.sides.remove(color);
            return this;
        }

        public AdversityOf4 build() {
            Preconditions.checkArgument(SIDES == this.sides.size());
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
    public Color getOpponent(final Color color) {
        return this.sides.get((this.sides.inverse().get(color) + 1) % 4);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.sides).toString();
    }

}