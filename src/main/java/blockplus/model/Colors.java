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

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

/**
 * Blokus game colors
 */
public enum Colors {

    Blue,

    Yellow,

    Red,

    Green;

    private final static Set<Colors> SET = new ImmutableSortedSet.Builder<Colors>(Ordering.natural()).add(Colors.values()).build();

    public static Set<Colors> set() {
        return SET;
    }

}