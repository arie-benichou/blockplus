
package blockplus.piece;

import java.util.Set;

import blockplus.direction.DirectionInterface;
import blockplus.position.PositionInterface;

import com.google.common.base.Supplier;

public interface PieceInterface extends Iterable<PieceInterface>, Supplier<Set<PieceInterface>> {

    int getId();

    PositionInterface getReferential();

    Set<PositionInterface> getCorners();

    Set<PositionInterface> getSides();

    Set<PositionInterface> getPotentialPositions();

    PieceInterface translateTo(PositionInterface position);

    PieceInterface translateBy(DirectionInterface direction);

    // TODO ! ajouter rotateOn

    PieceInterface rotateAround(PositionInterface referential);

    PieceInterface rotate();

}