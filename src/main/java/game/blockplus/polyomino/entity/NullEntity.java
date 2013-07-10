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

package game.blockplus.polyomino.entity;

import java.util.Iterator;

import components.cells.Positions.Position;

public final class NullEntity implements IEntity {

    @Override
    public Position referential() {
        return null;
    }

    @Override
    public Iterable<Position> positions() {
        return null;
    }

    @Override
    public Iterator<Position> iterator() {
        return null;
    }

    @Override
    public Integer radius() {
        return null;
    }

    @Override
    public Integer type() {
        return 0;
    }

    @Override
    public Iterable<Position> shadows() {
        return null;
    }

    @Override
    public Iterable<Position> lights() {
        return null;
    }

    @Override
    public boolean isNull() {
        return true;
    }

}