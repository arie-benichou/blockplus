
package blockplus.model.entity;


import java.util.Set;

import blockplus.model.entity.entity.Entity;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

public enum Polyomino implements Supplier<Entity> {

    _1(new String[] {
            "O"
    }),

    _2(new String[] {
            "OO"
    }),

    _3(new String[] {
            "OOO"
    }),

    _4(new String[] {
            "OO",
            "O "
    }),

    _5(new String[] {
            "OOOO"
    }),

    _6(new String[] {
            "OOO",
            "O   "
    }),

    _7(new String[] {
            "OOO",
            " O "
    }),

    _8(new String[] {
            "OO",
            "OO"
    }),

    _9(new String[] {
            "OO ",
            " OO"
    }),

    _10(new String[] {
            "OOOO",
            "O   "
    }),

    _11(new String[] {
            "OOOOO"
    }),

    _12(new String[] {
            "OO  ",
            " OOO"
    }),

    _13(new String[] {
            "OOO",
            " OO"
    }),

    _14(new String[] {
            "O O",
            "OOO"
    }),

    _15(new String[] {
            " O  ",
            "OOOO"
    }),

    _16(new String[] {
            "O  ",
            "OOO",
            "O  ",
    }),

    _17(new String[] {
            "OOO",
            "O  ",
            "O  ",
    }),

    _18(new String[] {
            "OO ",
            " OO",
            "  O",
    }),

    _19(new String[] {
            "O  ",
            "OOO",
            "  O",
    }),

    _20(new String[] {
            "O  ",
            "OOO",
            " O ",
    }),

    _21(new String[] {
            " O ",
            "OOO",
            " O ",
    }),

    /*
    _22(new String[] {
            "O O",
            "OOO",
            " O ",
    })
    */

    ;

    private final static Set<Polyomino> SET = new ImmutableSortedSet.Builder<Polyomino>(Ordering.natural()).add(Polyomino.values()).build();

    public static Set<Polyomino> set() {
        return SET;
    }

    public static Polyomino _(final int ordinal) {
        return Polyomino.valueOf("_" + ordinal);
    }

    private final Entity entity;

    private Polyomino(final String[] data) {
        this.entity = Entity.from(data);
    }

    @Override
    public Entity get() {
        return this.entity;
    }

}