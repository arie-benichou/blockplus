
package blockplus.move;

import java.util.Map;

import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.piece.PieceInterface;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

public class MoveHandler {

    private final Board<Color> board;

    // TODO use guava bus event    
    public MoveHandler(final Board<Color> board) {
        this.board = board;
    }

    // TODO générer un objet BoardMutation (puis par la suite, un GameMutation...)
    public Board<Color> handle(final Move move) {
        final Map<PositionInterface, Color> cells = Maps.newHashMap();
        for (final PieceInterface component : move.getPiece()) {
            cells.put(component.getReferential(), move.getColor());
        }
        // TODO !!! gérer les positions potentielles: tester les positions et prendre en compte les potential colors dejà présente sur le board
        for (final PositionInterface potentialPosition : move.getPiece().getPotentialPositions()) {
            cells.put(potentialPosition, move.getColor().potential());
        }
        return Board.from(this.board, cells);
    }

}