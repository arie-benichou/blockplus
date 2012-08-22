
package blockplus.model.board;

import components.board.Symbol;

public enum Remaining implements Symbol {

    NONE,
    ONE,
    TWO,
    THREE,
    ALL;

    private static final Remaining[] VALUES = Remaining.values();

    public static Remaining get(final int size) {
        return VALUES[size];
    }

}