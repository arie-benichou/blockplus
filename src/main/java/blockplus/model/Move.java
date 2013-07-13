
package blockplus.model;

import java.util.SortedSet;

import blockplus.model.interfaces.IMove;

import components.cells.IPosition;

public class Move implements IMove {

    private final Colors color;

    private final SortedSet<IPosition> positions;

    public Move(final Colors color, final SortedSet<IPosition> positions) {
        this.color = color;
        this.positions = positions;
    }

    public Colors color() {
        return this.color;
    }

    public SortedSet<IPosition> positions() {
        return this.positions;
    }

    @Override
    public boolean isNull() {
        return this.positions.isEmpty();
    }

}