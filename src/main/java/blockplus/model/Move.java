
package blockplus.model;

import blockplus.model.entity.IEntity;
import blockplus.model.interfaces.IMove;

public class Move implements IMove {

    private final Colors color;

    private final IEntity entity;

    public Move(final Colors color, final IEntity iEntity) {
        this.color = color;
        this.entity = iEntity;
    }

    public Colors color() {
        return this.color;
    }

    public IEntity entity() {
        return this.entity;
    }

    @Override
    public boolean isNull() {
        return this.entity == null || this.entity().isNull();
    }

    @Override
    public String toString() {
        if (this.entity == null) return "null move"; // TODO
        return this.entity.toString();
    }

}