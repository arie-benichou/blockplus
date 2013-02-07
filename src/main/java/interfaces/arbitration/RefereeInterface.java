
package interfaces.arbitration;

import java.util.List;

import blockplus.context.Context;
import blockplus.move.Move;

public interface RefereeInterface {

    List<Move> getLegalMoves(Context context);

}