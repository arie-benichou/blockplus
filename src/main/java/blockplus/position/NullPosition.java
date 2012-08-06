
package blockplus.position;

import blockplus.direction.DirectionInterface;

public class NullPosition implements PositionInterface {

    private final static PositionInterface INSTANCE = new NullPosition();

    public static PositionInterface getInstance() {
        return INSTANCE;
    }

    private NullPosition() {}

    @Override
    public int compareTo(final PositionInterface o) {
        return -1;
    }

    @Override
    public int row() {
        return Integer.MAX_VALUE; // TODO ! a prendre en compte dans la factory
    }

    @Override
    public int column() {
        return Integer.MAX_VALUE; // TODO ! a prendre en compte dans la factory
    }

    @Override
    public PositionInterface apply(final int row, final int column) {
        return this;
    }

    @Override
    public PositionInterface apply(final DirectionInterface direction) {
        return this;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}