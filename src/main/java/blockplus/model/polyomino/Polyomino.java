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

package blockplus.model.polyomino;

import java.util.Set;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import components.cells.IPosition;

public enum Polyomino implements Supplier<Iterable<PolyominoInstance>> {

    _0(new String[] {
            " "
    }),

    _1(new String[] {
            "0"
    }),

    _2(new String[] {
            "00"
    }),

    _3(new String[] {
            "000"
    }),

    _4(new String[] {
            "00",
            "0 "
    }),

    _5(new String[] {
            "0000"
    }),

    _6(new String[] {
            "000",
            "0   "
    }),

    _7(new String[] {
            "000",
            " 0 "
    }),

    _8(new String[] {
            "00",
            "00"
    }),

    _9(new String[] {
            "00 ",
            " 00"
    }),

    _10(new String[] {
            "0000",
            "0   "
    }),

    _11(new String[] {
            "00000"
    }),

    _12(new String[] {
            "00  ",
            " 000"
    }),

    _13(new String[] {
            "000",
            " 00"
    }),

    _14(new String[] {
            "0 0",
            "000"
    }),

    _15(new String[] {
            " 0  ",
            "0000"
    }),

    _16(new String[] {
            "0  ",
            "000",
            "0  ",
    }),

    _17(new String[] {
            "000",
            "0  ",
            "0  ",
    }),

    _18(new String[] {
            "00 ",
            " 00",
            "  0",
    }),

    _19(new String[] {
            "0  ",
            "000",
            "  0",
    }),

    _20(new String[] {
            "0  ",
            "000",
            " 0 ",
    }),

    _21(new String[] {
            " 0 ",
            "000",
            " 0 ",
    });

    public final static char NONE = ' ';

    public final static Set<Polyomino> SET = new ImmutableSortedSet.Builder<Polyomino>(Ordering.natural()).add(Polyomino.values()).build();

    public static Set<Polyomino> set() {
        return SET;
    }

    public static Polyomino _(final int ordinal) {
        return Polyomino.valueOf("_" + ordinal);
    }

    private final PolyominoProperties properties;

    private final PolyominoInstances instances;

    private Polyomino(final String[] data) {
        this.properties = PolyominoProperties.from(data);
        this.instances = PolyominoInstances.from(this.properties);
    }

    public Iterable<IPosition> positions() {
        return this.properties.positions();
    }

    public int weight() {
        return this.properties.weight();
    }

    public IPosition referential() {
        return this.properties.referential();
    }

    public int radius() {
        return this.properties.radius();
    }

    public Iterable<IPosition> shadows() {
        return this.properties.shadows();
    }

    public Iterable<IPosition> lights() {
        return this.properties.lights();
    }

    @Override
    public Iterable<PolyominoInstance> get() {
        return this.instances.get();
    }

    @Override
    public String toString() {
        return this.weight() + "." + this.radius() + "\n" + PolyominoRenderer.render(this);
    }

    public static void main(final String[] args) {
        for (final Polyomino polyomino : Polyomino.set()) {
            System.out.println(polyomino);
            System.out.println();
        }
    }
}