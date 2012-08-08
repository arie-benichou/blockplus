
package blockplus.piece;

import blockplus.position.Position;

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

    //private final static PieceInterface NULL = NullPieceComponent.getInstance();
    //private final static PieceInterface UNIT = PieceComponent.from(Position.ORIGIN);

    public final static PieceInterface get(final int ordinal) {
        //System.out.println(PIECE_NAME_PATTERN + ordinal);
        //System.out.println(Pieces.valueOf(PIECE_NAME_PATTERN + ordinal));
        //return Pieces.PIECE2;
        return Pieces.valueOf(PIECE_NAME_PATTERN + ordinal).get();
    }

    private Pieces() {
        this.piece = Piece.Piece(PieceData.get(this.ordinal()));
    }

    @Override
    public PieceInterface get() {
        return this.piece;
    }

    public static void main(final String[] args) {
        System.out.println(Pieces.get(2));
        System.out.println(Pieces.get(2));
        System.out.println(Pieces.get(2).translateTo(Position.from(5, 5)));
        System.out.println(Pieces.get(2).translateTo(Position.from(5, 5)));
        System.out.println(Piece.FACTORY);
    }

}