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

package demo;

import blockplus.direction.Direction;

public final class DirectionDemo {

    public static void main(final String[] args) {

        System.out.println(Direction.from(0, 0));
        System.out.println(Direction.from(1, -1));
        System.out.println(Direction.from(-1, 1));
        System.out.println(Direction.NULL.apply(1, 2).apply(Direction.from(1, 2)));

        // TODO Factory.toString()
        System.out.println(Direction.Factory.size());
        System.out.println(Direction.Factory.cacheHits());

    }

}