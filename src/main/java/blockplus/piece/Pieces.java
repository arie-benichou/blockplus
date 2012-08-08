
package blockplus.piece;

import static blockplus.piece.PieceData.PieceData;

import com.google.common.base.Supplier;

public enum Pieces implements Supplier<PieceInterface> {

    // null object
    PIECE0,

    // monominoes
    PIECE1,

    // dominoes
    PIECE2,

    // triominoes
    PIECE3, PIECE4,

    // tetrominoes
    PIECE5, PIECE6, PIECE7, PIECE8, PIECE9,

    //pentominoes
    PIECE10, PIECE11, PIECE12, PIECE13, PIECE14, PIECE15, PIECE16,
    PIECE17, PIECE18, PIECE19, PIECE20, PIECE21;

    private final static String PIECE_NAME_PATTERN = "PIECE";
    private final PieceInterface piece;

    //private final static PieceInterface NULL = NullPieceComponent.getInstance(); // TODO à revoir...
    //private final static PieceInterface UNIT = PieceComponent.from(Position.ORIGIN); // TODO à revoir...

    public final static PieceInterface get(final int ordinal) {
        return Pieces.valueOf(PIECE_NAME_PATTERN + ordinal).get();
    }

    private Pieces() {
        this.piece = Piece.Piece(PieceData(this.ordinal()));
    }

    @Override
    public PieceInterface get() {
        return this.piece;
    }

}