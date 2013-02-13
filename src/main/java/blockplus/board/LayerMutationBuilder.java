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

package blockplus.board;

import java.util.Map;
import java.util.Set;

import blockplus.board.Layer.State;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

// TODO à revoir
public final class LayerMutationBuilder {

    private final Set<PositionInterface> nonePositions = Sets.newHashSet();
    private Set<PositionInterface> potentialPositions = Sets.newHashSet();
    private Set<PositionInterface> selfPositions = Sets.newHashSet();
    private Set<PositionInterface> otherPositions = Sets.newHashSet();
    private Set<PositionInterface> shadowPositions = Sets.newHashSet();

    public LayerMutationBuilder setLightPositions(final Set<PositionInterface> positions) {
        this.potentialPositions = positions;
        return this;
    }

    public LayerMutationBuilder setSelfPositions(final Set<PositionInterface> positions) {
        this.selfPositions = positions;
        return this;
    }

    public LayerMutationBuilder setShadowPositions(final Set<PositionInterface> positions) {
        this.shadowPositions = positions;
        return this;
    }

    public LayerMutationBuilder setOtherPositions(final Set<PositionInterface> positions) {
        this.otherPositions = positions;
        return this;
    }

    public Map<PositionInterface, State> build() {
        final ImmutableMap.Builder<PositionInterface, State> builder = new ImmutableMap.Builder<PositionInterface, State>();
        for (final PositionInterface position : this.selfPositions)
            builder.put(position, State.Upekkha);
        for (final PositionInterface position : this.shadowPositions)
            builder.put(position, State.Karuna);
        for (final PositionInterface position : this.potentialPositions)
            builder.put(position, State.Metta);
        for (final PositionInterface position : this.otherPositions)
            builder.put(position, State.Mudita);
        for (final PositionInterface position : this.nonePositions)
            builder.put(position, State.Nirvana);
        return builder.build();
    }

}