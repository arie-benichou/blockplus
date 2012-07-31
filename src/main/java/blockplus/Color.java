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

package blockplus;

public enum Color {

    //TODO Black(-1),
    //TODO White(2*3*5*7),
    //TODO Transparent(1),

    Blue(2),
    Yellow(3),
    Red(5),
    Green(7);

    private final int value;

    private Color(final int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}