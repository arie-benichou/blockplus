
package blockplus.move;

import interfaces.move.MoveInterface;

import java.util.Comparator;

public final class MoveComparator implements Comparator<MoveInterface> {

    private final static MoveComparator INSTANCE = new MoveComparator();

    public static MoveComparator getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(final MoveInterface thisMoveInterface, final MoveInterface thatMoveInterface) {
        final Move thisMove = (Move) thisMoveInterface;
        final Move thatMove = (Move) thatMoveInterface;
        final int size1 = thisMove.getPiece().getSelfPositions().size();
        final int size2 = thatMove.getPiece().getSelfPositions().size();
        final int compare1 = size1 - size2;
        if (compare1 < 0) return 1;
        if (compare1 > 0) return -1;
        final int compare2 = thisMove.getPiece().getId() - thatMove.getPiece().getId();
        if (compare2 < 0) return 1;
        if (compare2 > 0) return -1;
        return thatMove.getPiece().getReferential().compareTo(thisMove.getPiece().getReferential());
    }

    private MoveComparator() {}

}