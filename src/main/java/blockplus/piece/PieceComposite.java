/*
 * Copyright 2012-2013 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.piece;

import static blockplus.piece.PieceComponent.PieceComponent;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import components.direction.Direction;
import components.direction.DirectionInterface;
import components.position.PositionInterface;

public final class PieceComposite implements PieceInterface {

    private final static boolean IS_FACTORY_CACHING = true;

    public final static class Factory {

        private final boolean isCaching;

        private long cacheHit = 0;

        private final Map<Set<PositionInterface>, PieceComposite> cache = Maps.newConcurrentMap();

        public Factory(final boolean isCaching) {
            this.isCaching = isCaching;
        }

        public Factory() {
            this(IS_FACTORY_CACHING);
        }

        private PieceComposite getFromNew(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
            return new PieceComposite(id, referential, positions);
        }

        private PieceComposite getFromCache(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
            return this.cache.get(positions);
        }

        public PieceComposite get(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
            PieceComposite instance = this.getFromCache(id, referential, positions);
            if (instance == null) {
                instance = this.getFromNew(id, referential, positions);
                if (this.isCaching) {
                    this.cache.put(positions, instance);
                }
            }
            else ++this.cacheHit;
            return instance;
        }

        public long cacheHits() {
            return this.cacheHit;
        }

        public int size() {
            return this.cache.size();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this.getClass().getCanonicalName())
                    .add("size", this.size())
                    .add("cacheHits", this.cacheHits())
                    .toString();
        }

    }

    /*----------------------------------------8<----------------------------------------*/

    public final static Factory FACTORY = new Factory();

    /*----------------------------------------8<----------------------------------------*/

    @SuppressWarnings("all")
    public static PieceInterface PieceComposite(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
        return FACTORY.get(id, referential, positions);
    }

    public static PieceInterface from(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
        return FACTORY.get(id, referential, positions);
    }

    /*----------------------------------------8<----------------------------------------*/

    private static Set<PieceInterface> extractComponents(final Set<PositionInterface> positions) {
        final Builder<PieceInterface> builder = ImmutableSet.builder();
        for (final PositionInterface position : positions)
            builder.add(PieceComponent(position));
        return builder.build();
    }

    private static Set<PositionInterface> extractCorners(final PieceInterface pieceInterface) {
        final ImmutableSortedSet.Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final PieceInterface component : pieceInterface)
            cornersBuilder.addAll(component.getCorners());
        return cornersBuilder.build();
    }

    private static Set<PositionInterface> extractSides(final PieceInterface pieceInterface) {
        final ImmutableSortedSet.Builder<PositionInterface> cornersBuilder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (final PieceInterface component : pieceInterface)
            cornersBuilder.addAll(component.getSides());
        return cornersBuilder.build();
    }

    /*----------------------------------------8<----------------------------------------*/

    private final int id;

    @Override
    public int getId() { // TODO ! cette responsabilité doit appartenir à la future classe ConcretePiece
        return this.id;
    }

    private final PositionInterface referential;

    @Override
    public PositionInterface getReferential() {
        return this.referential;
    }

    private final Set<PositionInterface> positions;

    @Override
    public Set<PositionInterface> getSelfPositions() {
        return this.positions;
    }

    private volatile Set<PieceInterface> components;

    private volatile Set<PositionInterface> corners;
    private volatile Set<PositionInterface> sides;

    private volatile Set<PositionInterface> lightPositions;
    private volatile Set<PositionInterface> shadowPositions;

    private volatile PieceInterface rotated;
    private volatile PieceInterface reflectedAlongVerticalAxis;
    private volatile PieceInterface reflectedAlongHorizontalAxis;

    private PieceComposite(final int id, final PositionInterface referential, final Set<PositionInterface> positions) {
        this.id = id;
        this.referential = referential;
        this.positions = ImmutableSortedSet.copyOf(positions);
    }

    @Override
    public Set<PieceInterface> get() {
        Set<PieceInterface> components = this.components;
        if (components == null) {
            synchronized (this) {
                if ((components = this.components) == null) this.components = components = extractComponents(this.positions);
            }
        }
        return components;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public Set<PositionInterface> getCorners() {
        Set<PositionInterface> corners = this.corners;
        if (corners == null) {
            synchronized (this) {
                if ((corners = this.corners) == null) this.corners = corners = extractCorners(this);
            }
        }
        return corners;
    }

    @Override
    public Set<PositionInterface> getSides() {
        Set<PositionInterface> sides = this.sides;
        if (sides == null) {
            synchronized (this) {
                if ((sides = this.sides) == null) this.sides = sides = extractSides(this);
            }
        }
        return sides;
    }

    @Override
    public Set<PositionInterface> getLightPositions() {
        Set<PositionInterface> lightPositions = this.lightPositions;
        if (lightPositions == null) {
            synchronized (this) {
                if ((lightPositions = this.lightPositions) == null)
                    this.lightPositions = lightPositions = Sets.difference(this.getCorners(), this.getSides());
            }
        }
        return lightPositions;
    }

    @Override
    public Set<PositionInterface> getShadowPositions() {
        Set<PositionInterface> shadowPositions = this.shadowPositions;
        if (shadowPositions == null) {
            synchronized (this) {
                if ((shadowPositions = this.shadowPositions) == null)
                    this.shadowPositions = shadowPositions = Sets.difference(this.getSides(), this.getSelfPositions());
            }
        }
        return shadowPositions;
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        final Set<PositionInterface> positions = Sets.newHashSet();
        for (final PositionInterface position : this.getSelfPositions())
            positions.add(position.apply(direction));
        return PieceComposite.from(this.getId(), this.getReferential().apply(direction), positions);
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        return this.translateBy(Direction.from(this.getReferential(), position));
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        final Set<PositionInterface> positions = Sets.newHashSet();
        for (final PieceInterface component : this)
            positions.addAll(component.rotateAround(referential).getSelfPositions());
        return PieceComposite.from(this.getId(), referential, positions);
    }

    @Override
    public PieceInterface rotate() {
        PieceInterface rotated = this.rotated;
        if (rotated == null) {
            synchronized (this) {
                if ((rotated = this.rotated) == null) this.rotated = rotated = this.rotateAround(this.getReferential());
            }
        }
        return rotated;
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis(final PositionInterface referential) {
        final Set<PositionInterface> positions = Sets.newHashSet();
        for (final PieceInterface component : this)
            positions.addAll(component.reflectAlongVerticalAxis(referential).getSelfPositions());
        return PieceComposite.from(this.getId(), referential, positions);
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis() {
        PieceInterface reflectedAlongVerticalAxis = this.reflectedAlongVerticalAxis;
        if (reflectedAlongVerticalAxis == null) synchronized (this) {
            if ((reflectedAlongVerticalAxis = this.reflectedAlongVerticalAxis) == null)
                this.reflectedAlongVerticalAxis = reflectedAlongVerticalAxis = this.reflectAlongVerticalAxis(this.getReferential());
        }
        return reflectedAlongVerticalAxis;
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis(final PositionInterface referential) {
        final Set<PositionInterface> positions = Sets.newHashSet();
        for (final PieceInterface component : this)
            positions.addAll(component.reflectAlongHorizontalAxis(referential).getSelfPositions());
        return PieceComposite.from(this.getId(), referential, positions);
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis() {
        PieceInterface reflectedAlongHorizontalAxis = this.reflectedAlongHorizontalAxis;
        if (reflectedAlongHorizontalAxis == null) synchronized (this) {
            if ((reflectedAlongHorizontalAxis = this.reflectedAlongHorizontalAxis) == null)
                this.reflectedAlongHorizontalAxis = reflectedAlongHorizontalAxis = this.reflectAlongHorizontalAxis(this.getReferential());
        }
        return reflectedAlongHorizontalAxis;
    }

    @Override
    public int hashCode() { // TODO ! caching
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceInterface)) return false;
        final PieceInterface that = (PieceInterface) object;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqual = this.getId() == that.getId()
                //&& this.getReferential().equals(that.getReferential())
                && this.get().equals(that.get());
        Preconditions.checkState(haveSameHashCode == isEqual, "this: " + this + "\nthat: " + that);
        return isEqual;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue("\n  " + this.getId())
                .addValue("\n  " + Joiner.on("\n  ").join(this.get()) + "\n")
                .toString();
    }

}