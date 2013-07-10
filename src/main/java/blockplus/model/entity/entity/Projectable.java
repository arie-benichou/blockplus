
package blockplus.model.entity.entity;

import components.cells.Positions.Position;

public interface Projectable extends Iterable<Position> {

    Position referential();

    Iterable<Position> positions();

}