
package blockplus.move;

import blockplus.Color;
import blockplus.piece.NullPieceComponent;
import blockplus.piece.PieceInterface;

import com.google.common.base.Preconditions;

// TODO add caching
public final class Moves {

    private Moves() {}

    public static Move getMove(final Color color, final PieceInterface pieceInstance) {
        Preconditions.checkArgument(color != null);
        Preconditions.checkArgument(pieceInstance != null);
        Preconditions.checkArgument(pieceInstance.getId() != 0); // TODO extract proper PieceInstance type
        return new Move(color, pieceInstance);
    }

    public static Move getNullMove(final Color color) {
        Preconditions.checkArgument(color != null);
        return new Move(color, NullPieceComponent.getInstance());
    }

}