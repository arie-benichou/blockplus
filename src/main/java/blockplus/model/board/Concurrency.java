
package blockplus.model.board;

import components.board.Symbol;

public enum Concurrency implements Symbol {

    NONE,
    ONE,
    TWO,
    THREE,
    FOUR;

    private static final Concurrency[] VALUES = Concurrency.values();

    public static Concurrency get(final int size) {
        return VALUES[size];
    }

}