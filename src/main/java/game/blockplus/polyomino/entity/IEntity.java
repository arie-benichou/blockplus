
package game.blockplus.polyomino.entity;

import components.cells.Positions.Position;

public interface IEntity extends Projectable {

    Integer radius();

    Integer type();

    Iterable<Position> shadows();

    Iterable<Position> lights();

    boolean isNull();

}
