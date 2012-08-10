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

package blockplus.color;

// TODO ? NotBlue
// TODO ? NotYellow
// TODO ? NotRed
// TODO ? NotGreen
public enum PrimeColors implements ColorInterface {

    OPAQUE(-1, "Ø") { // TODO ? 0 (élément absorbant)

        @Override
        public ColorInterface potential() {
            return this;
        }
    },

    UNKNOWN(0, "?") { // TODO ? something else

        @Override
        public ColorInterface potential() {
            return this;
        }
    },

    TRANSPARENT(1, " ") { // TODO ? -1

        @Override
        public ColorInterface potential() {
            return this;
        }

    },

    blue(-2, "b") {

        @Override
        public ColorInterface potential() {
            return this;
        }

    },

    BLUE(blue),

    yellow(-3, "y") {

        @Override
        public ColorInterface potential() {
            return this;
        }

    },

    YELLOW(yellow),

    red(-5, "r") {

        @Override
        public ColorInterface potential() {
            return this;
        }

    },

    RED(red),

    green(-7, "g") {

        @Override
        public ColorInterface potential() {
            return this;
        }

    },

    GREEN(green),

    white(-Math.abs(blue.value() * yellow.value() * red.value() * green.value()), "o"),

    WHITE(white);

    private final int value;
    private final String literal;
    private final ColorInterface potential;

    private PrimeColors(final int value, final String literal) {
        this.potential = null;
        this.value = value;
        this.literal = literal;
    }

    private PrimeColors(final ColorInterface potential) {
        this.potential = potential;
        this.value = -potential.value();
        this.literal = potential.toString().toUpperCase();
    }

    @Override
    public ColorInterface potential() {
        return this.potential;
    }

    @Override
    public int value() {
        return this.value;
    }

    @Override
    public boolean is(final ColorInterface color) {
        return this.equals(color);
    }

    @Override
    public boolean contains(final ColorInterface color) {
        return Math.abs(this.value()) % Math.abs(color.value()) == 0;
    }

    @Override
    public boolean hasOpacity() {
        if (this.is(OPAQUE)) return true;
        //if (this.is(Color.TRANSPARENT)) return false;
        if (this.value() <= 1) return false;
        if (this.value() > GREEN.value()) return false;
        return true;
    }

    @Override
    public boolean hasTransparency() {
        return !this.hasOpacity();
    }

    @Override
    public String toString() {
        return this.literal;
    }

    public static ColorInterface from(final int newCellPotential) {
        return null;
    }

}