
package blockplus.piece;

import java.util.Iterator;
import java.util.Set;

import blockplus.direction.DirectionInterface;
import blockplus.position.NullPosition;
import blockplus.position.PositionInterface;

import com.google.common.collect.ImmutableSet;

public final class NullPieceComponent implements PieceInterface {

    private final static int ID = 0;
    private final static PositionInterface REFERENTIAL = NullPosition.getInstance();
    private final static Set<PieceInterface> COMPONENTS = ImmutableSet.of();
    private final static Set<PositionInterface> POSITIONS = ImmutableSet.of();
    private final static Set<PositionInterface> CORNERS = ImmutableSet.of();
    private final static Set<PositionInterface> SIDES = ImmutableSet.of();
    private final static Set<PositionInterface> POTENTIAL_POSITIONS = ImmutableSet.of();

    private final static PieceInterface INSTANCE = new NullPieceComponent();

    public static PieceInterface getInstance() {
        return INSTANCE;
    }

    private NullPieceComponent() {}

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public PositionInterface getReferential() {
        return REFERENTIAL;
    }

    @Override
    public Set<PositionInterface> getCorners() {
        return CORNERS;
    }

    @Override
    public Set<PositionInterface> getSides() {
        return SIDES;
    }

    @Override
    public Set<PositionInterface> getPotentialPositions() {
        return POTENTIAL_POSITIONS;
    }

    @Override
    public Set<PieceInterface> get() {
        return COMPONENTS;
    }

    @Override
    public Set<PositionInterface> getPositions() {
        return POSITIONS;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        return this;
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        return this;
    }

    @Override
    public PieceInterface rotate() {
        return this;
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        return this;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false; // TODO ? retourner true
        if (object == this) return true;
        return false;
    }

    public static void main(final String[] args) {
        System.out.println(new NullPieceComponent());
    }

}