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
import components.graph.arc.Arc;
import components.graph.arc.ArcInterface;

/**
 * abstract class sucks :p
 */
public final class Edge<T> implements EdgeInterface<T> {

    private static <T> int hashCode(final ArcInterface<T> arc1, final ArcInterface<T> arc2) {
        return arc1.hashCode() * arc2.hashCode();
    }

    private static <T> Edge<T> from(final ArcInterface<T> arc1, final ArcInterface<T> arc2, final int hashCode) {
        return new Edge<T>(arc1, arc2, hashCode);
    }

    public static class Factory<T> {

        private final components.graph.arc.Arc.Factory<T> arcFactory;

        public Factory(final Arc.Factory<T> arcFactory) {
            this.arcFactory = arcFactory;
        }

        public Factory() {
            this(new Arc.Factory<T>());
        }

        private int cacheHit = 0;

        private final ConcurrentMap<Integer, Edge<T>> instances = Maps.newConcurrentMap();

        public Edge<T> getEdge(final T endPoint1, final T endPoint2) {
            final Arc<T> arc1 = this.arcFactory.get(endPoint1, endPoint2);
            final Arc<T> arc2 = this.arcFactory.get(endPoint2, endPoint1);
            final int hashCode = Edge.hashCode(arc1, arc2);
            Edge<T> instance = this.instances.get(hashCode);
            if (instance == null) {
                instance = Edge.from(arc1, arc2, hashCode);
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

    public static <T> Edge<T> from(final T endPoint1, final T endPoint2) {
        return new Edge<T>(Arc.from(endPoint1, endPoint2), Arc.from(endPoint2, endPoint1));
    }

    private final ArcInterface<T> arc1;

    @Override
    public ArcInterface<T> getArcFromEndPoint1() {
        return this.arc1;
    }

    private final ArcInterface<T> arc2;

    private final int hashCode;

    @Override
    public ArcInterface<T> getArcFromEndPoint2() {
        return this.arc2;
    }

    @Override
    public T getEndPoint1() {
        return this.getArcFromEndPoint1().getEndPoint1();
    }

    @Override
    public T getEndPoint2() {
        return this.getArcFromEndPoint2().getEndPoint1();
    }

    private Edge(final ArcInterface<T> arc1, final ArcInterface<T> arc2, final int hashCode) {
        this.arc1 = arc1;
        this.arc2 = arc2;
        this.hashCode = hashCode;
    }

    private Edge(final ArcInterface<T> arc1, final ArcInterface<T> arc2) {
        this(arc1, arc2, Edge.hashCode(arc1, arc2));
    }

    @Override
    public EdgeInterface<T> get() {
        return this;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Edge)) return false;
        final Object endPoint = ((Edge<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final Edge<?> that = (Edge<?>) object;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqal = this.arc1.equals(that.arc1) && this.arc2.equals(that.arc2) || this.arc1.equals(that.arc2) && this.arc2.equals(that.arc1);
        Preconditions.checkState(hasSameHashCode == isEqal);
        return isEqal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("endPoint1", this.getEndPoint1())
                .add("endPoint2", this.getEndPoint2())
                .toString();
    }

    public static void main(final String[] args) {

        final Edge<String> edge1 = Edge.from("A", "B");
        System.out.println(edge1);
        final Edge<Integer> edge2 = Edge.from(1, 2);
        System.out.println(edge2);

        final Factory<String> factory = new Edge.Factory<String>();
        System.out.println(factory.size());
        final Edge<String> arc1 = factory.getEdge("A", "B");
        System.out.println(arc1);
        System.out.println(factory.getCacheHit());
        final Edge<String> arc2 = factory.getEdge("A", "B");
        System.out.println(arc2);
        System.out.println(factory.getCacheHit());
        System.out.println(factory.size());

    }

}