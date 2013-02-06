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

package blockplus.piece;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import components.direction.DirectionInterface;
import components.position.NullPosition;
import components.position.PositionInterface;

public final class NullPieceComponent implements PieceInterface {

    private final static int ID = 0;
    private final static PositionInterface REFERENTIAL = NullPosition.getInstance();
    private final static Set<PieceInterface> COMPONENTS = ImmutableSet.of();
    private final static Set<PositionInterface> POSITIONS = ImmutableSet.of();
    private final static Set<PositionInterface> CORNERS = ImmutableSet.of();
    private final static Set<PositionInterface> SIDES = ImmutableSet.of();
    private final static Set<PositionInterface> LIGHT_POSITIONS = ImmutableSet.of();
    private static final Set<PositionInterface> SHADOW_POSITIONS = ImmutableSet.of();

    private final static PieceInterface INSTANCE = new NullPieceComponent();

    public static PieceInterface getInstance() {
        return INSTANCE;
    }

    private NullPieceComponent() {}

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public PositionInterface getReferential() {
        return REFERENTIAL;
    }

    @Override
    public Set<PositionInterface> getCorners() {
        return CORNERS;
    }

    @Override
    public Set<PositionInterface> getSides() {
        return SIDES;
    }

    @Override
    public Set<PositionInterface> getLightPositions() {
        return LIGHT_POSITIONS;
    }

    @Override
    public Set<PositionInterface> getShadowPositions() {
        return SHADOW_POSITIONS;
    }

    @Override
    public Set<PieceInterface> get() {
        return COMPONENTS;
    }

    @Override
    public Set<PositionInterface> getSelfPositions() {
        return POSITIONS;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        return this;
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        return this;
    }

    @Override
    public PieceInterface rotate() {
        return this;
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        return this;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false; // TODO ? retourner true
        if (object == this) return true;
        return false;
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis(final PositionInterface referential) {
        return this;
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis() {
        return this;
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis(final PositionInterface referential) {
        return this;
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis() {
        return this;
    }

}