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

// TODO ? NotBlue
// TODO ? NotYellow
// TODO ? NotRed
// TODO ? NotGreen
public enum Color {

    Unknown(0, "?"),

    Blue(2, "B"),
    Yellow(3, "Y"),
    Red(5, "R"),
    Green(7, "G"),

    Black(-1, "Ø"),
    White(Blue.value() * Yellow.value() * Red.value() * Green.value(), "Ω"),

    Transparent(1, " ");

    private final int value;
    private final String literal;

    private Color(final int value, final String literal) {
        this.value = value;
        this.literal = literal;
    }

    public int value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.literal;
    }

}