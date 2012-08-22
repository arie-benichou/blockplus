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

package components.position;

import components.direction.DirectionInterface;

public class NullPosition implements PositionInterface {

    private final static PositionInterface INSTANCE = new NullPosition();

    public static PositionInterface getInstance() {
        return INSTANCE;
    }

    private NullPosition() {}

    @Override
    public int compareTo(final PositionInterface o) {
        return -1;
    }

    @Override
    public int row() {
        return Integer.MAX_VALUE; // TODO ! a prendre en compte dans la factory
    }

    @Override
    public int column() {
        return Integer.MAX_VALUE; // TODO ! a prendre en compte dans la factory
    }

    @Override
    public PositionInterface apply(final int row, final int column) {
        return this;
    }

    @Override
    public PositionInterface apply(final DirectionInterface direction) {
        return this;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}