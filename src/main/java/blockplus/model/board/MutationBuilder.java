
package blockplus.model.board;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

// TODO ?! utiliser directement une HashMap mutable
public final class MutationBuilder {

    private Set<PositionInterface> nonePositions = Sets.newHashSet();
    private Set<PositionInterface> potentialPositions = Sets.newHashSet();
    private Set<PositionInterface> selfPositions = Sets.newHashSet();
    private Set<PositionInterface> otherPositions = Sets.newHashSet();
    private Set<PositionInterface> shadowPositions = Sets.newHashSet();

    public MutationBuilder reset() {
        return new MutationBuilder();
    }

    public MutationBuilder setNonePositions(final Set<PositionInterface> positions) {
        this.nonePositions = positions;
        return this;
    }

    public MutationBuilder setNonePositions(final PositionInterface... positions) {
        return this.setNonePositions(Sets.newHashSet(positions));
    }

    public MutationBuilder setLightPositions(final Set<PositionInterface> positions) {
        this.potentialPositions = positions;
        return this;
    }

    public MutationBuilder setPotentialPositions(final PositionInterface... positions) {
        return this.setLightPositions(Sets.newHashSet(positions));
    }

    public MutationBuilder setSelfPositions(final Set<PositionInterface> positions) {
        this.selfPositions = positions;
        return this;
    }

    public MutationBuilder setSelfPositions(final PositionInterface... positions) {
        return this.setSelfPositions(Sets.newHashSet(positions));
    }

    public MutationBuilder setShadowPositions(final Set<PositionInterface> positions) {
        this.shadowPositions = positions;
        return this;
    }

    public MutationBuilder setShadowPositions(final PositionInterface... positions) {
        return this.setShadowPositions(Sets.newHashSet(positions));
    }

    public MutationBuilder setOtherPositions(final Set<PositionInterface> positions) {
        this.otherPositions = positions;
        return this;
    }

    public MutationBuilder setOtherPositions(final PositionInterface... positions) {
        return this.setOtherPositions(Sets.newHashSet(positions));
    }

    public Map<PositionInterface, State> build() {
        final ImmutableMap.Builder<PositionInterface, State> builder = new ImmutableMap.Builder<PositionInterface, State>();
        for (final PositionInterface position : this.selfPositions)
            builder.put(position, State.Self);
        for (final PositionInterface position : this.shadowPositions)
            builder.put(position, State.Shadow);
        for (final PositionInterface position : this.potentialPositions)
            builder.put(position, State.Light);
        for (final PositionInterface position : this.otherPositions)
            builder.put(position, State.Other);
        for (final PositionInterface position : this.nonePositions)
            builder.put(position, State.None);
        return builder.build();
    }

}