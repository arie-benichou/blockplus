
package interfaces.arbitration;

import interfaces.context.ContextInterface;
import interfaces.move.MoveInterface;

import java.util.Comparator;
import java.util.List;

public interface RefereeInterface {

    List<MoveInterface> getLegalMoves(ContextInterface<?> contextInterface, Comparator<MoveInterface> comparator);

    List<MoveInterface> getLegalMoves(ContextInterface<?> contextInterface);

}