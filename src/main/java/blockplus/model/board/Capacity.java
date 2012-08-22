
package blockplus.model.board;

import components.board.Symbol;

public enum Capacity implements Symbol {

    NONE,
    ONE,
    TWO,
    THREE,
    FOUR;

    private static final Capacity[] VALUES = Capacity.values();

    public static Capacity get(final int size) {
        return VALUES[size];
    }

}