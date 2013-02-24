/*
 * Copyright 2012 Arie Benichou
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

package components.graph.edge;

import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import components.graph.arc.ArcInterface;

public final class WeightedEdge<T> implements WeightedEdgeInterface<T> {

    private static <T> void check(final Double weight) {
        Preconditions.checkArgument(weight != null);
        Preconditions.checkArgument(weight >= 0);
    }

    private static <T> int hashCode(final EdgeInterface<T> edge, final Double weight) {
        return edge.hashCode() * weight.hashCode();
    }

    private static <T> WeightedEdge<T> from(final EdgeInterface<T> edge, final Double weight, final int hashCode) {
        return new WeightedEdge<T>(edge, weight, hashCode);
    }

    public static class Factory<T> {

        private int cacheHit = 0;

        private final ConcurrentMap<Integer, WeightedEdge<T>> instances = Maps.newConcurrentMap();

        private final Edge.Factory<T> edgeFactory;

        public Factory(final Edge.Factory<T> arcFactory) {
            this.edgeFactory = arcFactory;
        }

        public Factory() {
            this(new Edge.Factory<T>());
        }

        public WeightedEdge<T> getEdge(final T endPoint1, final T endPoint2, final Double weight) {
            check(weight);
            final Edge<T> edge = this.edgeFactory.getEdge(endPoint1, endPoint2);
            final int hashCode = WeightedEdge.hashCode(edge, weight);
            WeightedEdge<T> instance = this.instances.get(hashCode);
            if (instance == null) {
                instance = WeightedEdge.from(edge, weight, hashCode);
                this.instances.put(hashCode, instance);
            }
            else {
                ++this.cacheHit;
            }
            return instance;
        }

        public int getCacheHit() {
            return this.cacheHit;
        }

        public int size() {
            return this.instances.size();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("cacheHit", this.getCacheHit())
                    .add("size", this.size())
                    .toString();
        }

    }

    public static <T> WeightedEdge<T> from(final T endPoint1, final T endPoint2, final Double weight) {
        check(weight);
        return new WeightedEdge<T>(Edge.from(endPoint1, endPoint2), weight);
    }

    private final EdgeInterface<T> edge;

    public EdgeInterface<T> getEdge() {
        return this.edge;
    }

    @Override
    public ArcInterface<T> getArcFromEndPoint1() {
        return this.getEdge().getArcFromEndPoint1();
    }

    @Override
    public ArcInterface<T> getArcFromEndPoint2() {
        return this.getEdge().getArcFromEndPoint2();
    }

    private final Double weight;

    @Override
    public Double getWeight() {
        return this.weight;
    }

    private final int hashCode;

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    private WeightedEdge(final EdgeInterface<T> edge, final Double weight, final int hashCode) {
        this.edge = edge;
        this.weight = weight;
        this.hashCode = hashCode;
    }

    private WeightedEdge(final EdgeInterface<T> edge, final Double weight) {
        this(edge, weight, hashCode(edge, weight));
    }

    @Override
    public EdgeInterface<T> get() {
        return this;
    }

    @Override
    public T getEndPoint1() {
        return this.getArcFromEndPoint1().getEndPoint1();
    }

    @Override
    public T getEndPoint2() {
        return this.getArcFromEndPoint2().getEndPoint1();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof WeightedEdge)) return false;
        final Object endPoint = ((WeightedEdge<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final WeightedEdge<?> that = (WeightedEdge<?>) object;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqal = this.getWeight().equals(that.getWeight()) && this.getEdge().equals(that.getEdge());
        Preconditions.checkState(hasSameHashCode == isEqal);
        return isEqal;
    }

    @Override
    public int compareTo(final WeightedEdgeInterface<T> that) {
        return Double.compare(this.getWeight(), that.getWeight());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("weight", this.getWeight())
                .add("endPoint1", this.getEndPoint1())
                .add("endPoint2", this.getEndPoint2())
                .toString();
    }

}