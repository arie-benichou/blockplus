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
public enum Color {

    OPAQUE(-1, "Ø") {

        @Override
        public Color potential() {
            return this;
        }
    },

    UNKNOWN(0, "?") {

        @Override
        public Color potential() {
            return this;
        }
    },

    TRANSPARENT(1, " ") {

        @Override
        public Color potential() {
            return this;
        }

    },

    blue(-2, "b") {

        @Override
        public Color potential() {
            return this;
        }

    },

    BLUE(blue),

    yellow(-3, "y") {

        @Override
        public Color potential() {
            return this;
        }

    },

    YELLOW(yellow),

    red(-5, "r") {

        @Override
        public Color potential() {
            return this;
        }

    },

    RED(red),

    green(-7, "g") {

        @Override
        public Color potential() {
            return this;
        }

    },

    GREEN(green),

    white(-Math.abs(blue.value() * yellow.value() * red.value() * green.value()), "o"),

    //WHITE(Blue.value() * Yellow.value() * Red.value() * Green.value(), "O");
    WHITE(white);

    private final int value;
    private final String literal;
    private final Color potential;

    private Color(final int value, final String literal) {
        this.potential = null;
        this.value = value;
        this.literal = literal;
    }

    private Color(final Color potential) {
        this.potential = potential;
        this.value = -potential.value();
        this.literal = potential.toString().toUpperCase();
    }

    public Color potential() {
        return this.potential;
    }

    public int value() {
        return this.value;
    }

    public boolean is(final Color color) {
        return this.equals(color);
    }

    public boolean contains(final Color color) {
        return Math.abs(this.value()) % Math.abs(color.value()) == 0;
    }

    public boolean hasOpacity() {
        if (this.is(Color.OPAQUE)) return true;
        //if (this.is(Color.TRANSPARENT)) return false;
        if (this.value() <= 1) return false;
        if (this.value() > GREEN.value()) return false;
        return true;
    }

    public boolean hasTransparency() {
        return !this.hasOpacity();
    }

    @Override
    public String toString() {
        return this.literal;
    }

}