
package blockplus.model.move;

import blockplus.model.context.Color;
import blockplus.model.entity.entity.IEntity;

public class Move implements IMove {

    private final Color side;

    private final IEntity entity;

    public Move(final Color side, final IEntity iEntity) {
        this.side = side;
        this.entity = iEntity;
    }

    public Color getSide() {
        return this.side;
    }

    public IEntity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isNull() {
        return this.entity == null || this.getEntity().isNull();
    }

    @Override
    public String toString() {
        if (this.entity == null) return "null move"; // TODO
        return this.entity.toString();
    }

}