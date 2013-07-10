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

package game.blockplus.board;

import game.blockplus.board.Layer.State;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import components.cells.Positions.Position;

// TODO à revoir
public final class LayerMutationBuilder {

    private Iterable<Position> potentialPositions = Sets.newHashSet();
    private Iterable<Position> selfPositions = Sets.newHashSet();
    private Iterable<Position> otherPositions = Sets.newHashSet();
    private Iterable<Position> shadowPositions = Sets.newHashSet();

    public LayerMutationBuilder setLightPositions(final Iterable<Position> positions) {
        this.potentialPositions = positions;
        return this;
    }

    public LayerMutationBuilder setLightPositions(final Position... positions) {
        return this.setLightPositions(Sets.newHashSet(positions));
    }

    public LayerMutationBuilder setSelfPositions(final Iterable<Position> positions) {
        this.selfPositions = positions;
        return this;
    }

    public LayerMutationBuilder setShadowPositions(final Iterable<Position> positions) {
        this.shadowPositions = positions;
        return this;
    }

    public LayerMutationBuilder setOtherPositions(final Iterable<Position> positions) {
        this.otherPositions = positions;
        return this;
    }

    public Map<Position, State> build() {
        final ImmutableMap.Builder<Position, State> builder = new ImmutableMap.Builder<Position, State>();
        for (final Position position : this.selfPositions)
            builder.put(position, State.Upekkha);
        for (final Position position : this.shadowPositions)
            builder.put(position, State.Karuna);
        for (final Position position : this.potentialPositions)
            builder.put(position, State.Metta);
        for (final Position position : this.otherPositions)
            builder.put(position, State.Mudita);
        return builder.build();
    }

}