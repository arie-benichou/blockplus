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

package components.graph.arc;

import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class WeightedArc<T> implements WeightedArcInterface<T> {

    private static final Double DEFAULT_WEIGHT = 1.0;

    private static <T> Double check(final Double weight) {
        Preconditions.checkArgument(weight != null);
        Preconditions.checkArgument(weight >= 0);
        return weight;
    }

    private static <T> ArcInterface<T> check(final ArcInterface<T> arc) {
        Preconditions.checkArgument(arc != null);
        return arc;
    }

    private static <T> String toString(final ArcInterface<T> arc, final Double weight) {
        return Objects.toStringHelper(WeightedArc.class)
                .addValue(arc)
                .addValue(weight)
                .toString();
    }

    private static <T> int hashCode(final ArcInterface<T> arc, final Double weight) {
        return toString(arc, weight).hashCode();
    }

    private static <T> WeightedArc<T> from(final ArcInterface<T> arc, final Double weight, final int hashCode) {
        return new WeightedArc<T>(arc, weight, hashCode);
    }

    public static class Factory<T> {

        private int cacheHit = 0;

        private final ConcurrentMap<Integer, WeightedArc<T>> instances = Maps.newConcurrentMap();

        private final components.graph.arc.Arc.Factory<T> arcFactory;

        public Factory(final Arc.Factory<T> arcFactory) {
            this.arcFactory = arcFactory;
        }

        public Factory() {
            this(new Arc.Factory<T>());
        }

        public WeightedArc<T> get(final T endPoint1, final T endPoint2, final Double weight) {
            check(weight);
            final ArcInterface<T> arc = this.arcFactory.get(endPoint1, endPoint2);
            //if (true) return WeightedArc.from(endPoint1, endPoint2, weight); // TODO ! pouvoir activer ou désactiver le cache de la factory
            final int hashCode = WeightedArc.hashCode(arc, weight);
            WeightedArc<T> instance = this.instances.get(hashCode);
            if (instance == null) {
                instance = WeightedArc.from(arc, weight, hashCode);
                this.instances.put(hashCode, instance);
            }
            else {
                Preconditions.checkState(instance.getEndPoint1().equals(endPoint1), "Hash Code Error !");
                Preconditions.checkState(instance.getEndPoint2().equals(endPoint2), "Hash Code Error !");
                Preconditions.checkState(instance.getWeight().equals(weight), "Hash Code Error !");
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

    public static <T> WeightedArc<T> from(final ArcInterface<T> arc, final Double weight) {
        return new WeightedArc<T>(check(arc), check(weight));
    }

    public static <T> WeightedArc<T> from(final T endPoint1, final T endPoint2, final Double weight) {
        return from(Arc.from(endPoint1, endPoint2), check(weight));
    }

    public static <T> WeightedArc<T> from(final T endPoint1, final T endPoint2) {
        return from(endPoint1, endPoint2, DEFAULT_WEIGHT);
    }

    private final ArcInterface<T> arc;

    @Override
    public T getEndPoint1() {
        return this.arc.getEndPoint1();
    }

    @Override
    public T getEndPoint2() {
        return this.arc.getEndPoint2();
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

    private WeightedArc(final ArcInterface<T> arc, final Double weight, final int hashCode) {
        this.arc = arc;
        this.weight = weight;
        this.hashCode = hashCode;
    }

    private WeightedArc(final ArcInterface<T> arc, final Double weight) {
        this(arc, weight, hashCode(arc, weight));
    }

    @Override
    public ArcInterface<T> get() {
        return this;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof WeightedArc)) return false;
        final Object endPoint = ((WeightedArc<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final WeightedArc<?> that = (WeightedArc<?>) object;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqal = this.getWeight().equals(that.getWeight()) && this.arc.equals(that.arc);
        Preconditions.checkState(hasSameHashCode == isEqal);
        return isEqal;
    };

    @Override
    public int compareTo(final WeightedArcInterface<T> that) {
        return Double.compare(this.getWeight(), that.getWeight());
    }

    @Override
    public String toString() {
        return toString(this.arc, this.getWeight());
    }

}