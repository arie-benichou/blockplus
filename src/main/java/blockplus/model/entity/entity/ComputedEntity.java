
package blockplus.model.entity.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import components.cells.Positions.Position;

public class ComputedEntity implements IEntity {

    public static String computeId(final Iterable<Position> positions) {
        final JsonArray jsonArray = new JsonArray();
        for (final Position position : positions) {
            jsonArray.add(new JsonPrimitive(position.id()));
        }
        return jsonArray.toString();
    }

    public static ComputedEntity decorate(final IEntity entity) {
        final List<Position> positions = Lists.newArrayList(entity.positions());
        return new ComputedEntity(
                entity.type(),
                entity.radius(),
                entity.referential(),
                positions,
                (Set<Position>) entity.shadows(),
                (Set<Position>) entity.lights());
    }

    private final Integer type;

    private final Integer radius;

    private final Position referential;

    private final Iterable<Position> positions;

    private final Iterable<Position> shadows;

    private final Iterable<Position> lights;

    @JsonProperty("id")
    final String id;

    private ComputedEntity(
            @JsonProperty("type") final Integer type,
            @JsonProperty("radius") final Integer radius,
            @JsonProperty("referential") final Position referential,
            @JsonProperty("positions") final List<Position> positions,
            @JsonProperty("shadows") final Set<Position> shadows,
            @JsonProperty("lights") final Set<Position> lights) {
        this.type = type;
        this.radius = radius;
        this.referential = referential;
        this.positions = positions;
        this.shadows = shadows;
        this.lights = lights;
        this.id = computeId(positions);
    }

    @Override
    public Integer type() {
        return this.type;
    }

    @Override
    public Integer radius() {
        return this.radius;

    }

    @Override
    public Position referential() {
        return this.referential;
    }

    @Override
    public Iterable<Position> positions() {
        return this.positions;
    }

    @Override
    public Iterable<Position> shadows() {
        return this.shadows;
    }

    @Override
    public Iterable<Position> lights() {
        return this.lights;
    }

    @Override
    public Iterator<Position> iterator() {
        return this.positions().iterator();
    }

    @Override
    public boolean isNull() {
        return !this.positions.iterator().hasNext();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("type", this.type())
                .add("radius", this.radius())
                .add("referential", this.referential())
                .add("shadows", this.radius())
                .add("lights", this.referential())
                .toString();

    }

}