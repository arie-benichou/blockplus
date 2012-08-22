
package blockplus.model.board;

import components.board.Symbol;

public enum State implements Symbol {

    None,
    Other,
    Self,
    Shadow,
    Light;

    public boolean is(final Symbol other) {
        return this.equals(other);
    }

    public boolean isSelfOrOther() {
        return this.equals(Self) || this.equals(Other);
    }

}