
package interfaces.board;

import interfaces.move.MoveInterface;

public interface BoardInterface {

    boolean isLegal(MoveInterface moveInterface);

    BoardInterface apply(MoveInterface moveInterface);

}