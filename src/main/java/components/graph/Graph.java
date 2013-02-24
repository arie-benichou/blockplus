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

package components.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;


import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import components.graph.arc.ArcInterface;
import components.graph.arc.Arcs;
import components.graph.features.FeatureInterface;


// TODO has(EdgeInterface edge)
public final class Graph<T> implements GraphInterface<T> {

    public static <T> Graph<T> from(final GraphBuilder<T> builder, final boolean isInUserMode) {
        return new Graph<T>(builder, isInUserMode);
    }

    private final int order;

    @Override
    public int getOrder() {
        return this.order;
    }

    private final Arcs<T> arcsFactory;

    public Arcs<T> getArcFactory() {
        return this.arcsFactory;
    }

    private final boolean isInUserMode;

    private final Map<T, Integer> idByEndpoint;
    private final Map<Integer, T> endPointById;

    private final Map<T, List<ArcInterface<T>>> arcsByEndpoint;
    private final Map<T, List<T>> connectedEndPointsByEndPoint;

    private final Set<ArcInterface<T>> arcSet;
    private final Map<Integer, ArcInterface<T>> arcByHashCode;

    private final GraphBuilder<T> builder;

    // TODO ? revoir ce quick & dirty fix
    public GraphBuilder<T> getBuilder() {
        return this.builder;
    }

    private Graph(final GraphBuilder<T> builder, final boolean isInUserMode) {

        this.builder = builder;

        this.order = builder.getOrder();

        this.arcsFactory = builder.getArcFactory();

        this.isInUserMode = isInUserMode;

        this.idByEndpoint = ImmutableMap.copyOf(builder.getIdByEndpoint());

        final ImmutableSortedMap.Builder<Integer, T> endPointByIdBuilder = new ImmutableSortedMap.Builder<Integer, T>(Ordering.natural());
        for (final Entry<T, Integer> entry : this.idByEndpoint.entrySet())
            endPointByIdBuilder.put(entry.getValue(), entry.getKey());
        this.endPointById = endPointByIdBuilder.build();

        this.arcSet = ImmutableSet.copyOf(builder.getArcs());

        this.arcByHashCode = builder.getArcByHashCodeBuilder().build();

        final ImmutableMap.Builder<T, List<T>> connectedEndPointsByEndPointBuilder = new ImmutableMap.Builder<T, List<T>>();
        for (final Entry<T, List<T>> entry : builder.getConnectedEndPointsByEndPoint().entrySet())
            connectedEndPointsByEndPointBuilder.put(entry.getKey(), ImmutableList.copyOf(entry.getValue()));
        this.connectedEndPointsByEndPoint = connectedEndPointsByEndPointBuilder.build();

        this.arcsByEndpoint = Maps.newHashMap();

    }

    @Override
    public boolean hasEndPoint(final T endPoint) {
        Preconditions.checkArgument(endPoint != null);
        return this.idByEndpoint.containsKey(endPoint);
    }

    private T checkEndPoint(final T endPoint) {
        if (this.isInUserMode)
            if (!this.hasEndPoint(endPoint)) throw new NoSuchElementException("Node '" + endPoint + "' does not exist.");
        return endPoint;
    }

    private Integer getId(final T endPoint) {
        return this.idByEndpoint.get(endPoint);
    }

    @Override
    public Integer getOrdinal(final T endPoint) {
        return this.getId(this.checkEndPoint(endPoint)) - 1;
    }

    @Override
    public T get(final int ordinal) {
        Preconditions.checkArgument(ordinal > -1 && ordinal < this.getOrder());
        return this.endPointById.get(ordinal + 1);
    }

    @Override
    public List<T> getConnectedEndPoints(final T endPoint) {
        return this.connectedEndPointsByEndPoint.get(this.checkEndPoint(endPoint));
    }

    private Integer hashCode(final T endPoint1, final T endPoint2, final boolean asDuplicate) {
        return (asDuplicate ? -1 : 1) * (this.getOrder() * this.getId(endPoint1) + this.getId(endPoint2));
    }

    @Override
    public boolean hasArc(final T endPoint1, final T endPoint2) {
        return this.arcByHashCode.containsKey(this.hashCode(this.checkEndPoint(endPoint1), this.checkEndPoint(endPoint2), false));
    }

    private ArcInterface<T> getArc(final T endPoint1, final T endPoint2, final boolean isDuplicate) {
        return this.arcByHashCode.get(this.hashCode(this.checkEndPoint(endPoint1), this.checkEndPoint(endPoint2), isDuplicate));
    }

    public ArcInterface<T> getSymetric(final ArcInterface<T> arc) {
        if (arc.get().equals(arc)) return this.getArc(arc.getEndPoint2(), arc.getEndPoint1());
        return this.getArcDuplicate(arc.getEndPoint2(), arc.getEndPoint1());
    }

    public ArcInterface<T> getArcDuplicate(final T endPoint1, final T endPoint2) {
        final boolean hasArc = this.hasArc(endPoint1, endPoint2);
        Preconditions.checkState(hasArc, "Arc between " + endPoint1 + " and " + endPoint2 + " does not exist.");
        final ArcInterface<T> arc = this.getArc(endPoint1, endPoint2, true);
        Preconditions.checkState(arc != null, "Duplicate arc between " + endPoint1 + " and " + endPoint2 + " does not exist.");
        return arc;
    }

    @Override
    public ArcInterface<T> getArc(final T endPoint1, final T endPoint2) {
        final ArcInterface<T> arc = this.arcByHashCode.get(this.hashCode(this.checkEndPoint(endPoint1), this.checkEndPoint(endPoint2), false));
        if (this.isInUserMode) Preconditions.checkState(arc != null, "Arc (" + endPoint1 + " - " + endPoint2 + ") does not exist.");
        return arc;
    }

    @Override
    public List<ArcInterface<T>> getArcsFrom(final T endPoint1) {
        List<ArcInterface<T>> arcs = this.arcsByEndpoint.get(this.checkEndPoint(endPoint1)); //TODO ? ImmutableSortedSet
        if (arcs == null) {
            final ImmutableList.Builder<ArcInterface<T>> builder = ImmutableList.builder();
            final Set<T> endPoints = Sets.newHashSet();
            for (final T endPoint2 : this.getConnectedEndPoints(endPoint1)) {
                boolean isDuplicate;
                if (!endPoints.contains(endPoint2)) {
                    endPoints.add(endPoint2);
                    isDuplicate = false;
                }
                else isDuplicate = true;
                builder.add(this.getArc(endPoint1, endPoint2, isDuplicate));
            }
            arcs = builder.build();
            this.arcsByEndpoint.put(endPoint1, arcs);
        }
        return arcs;
    }

    @Override
    public Set<ArcInterface<T>> getSetOfArcs() {
        return this.arcSet;
    }

    @Override
    public Iterator<T> iterator() {
        return this.endPointById.values().iterator();
    }

    private final Map<Class<?>, FeatureInterface> features = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    public <F extends FeatureInterface> F fetch(final Class<F> featureClass) {
        F feature = (F) this.features.get(featureClass);
        if (feature == null) {
            Exception featureException = null;
            try {
                feature = featureClass.getConstructor(Graph.class).newInstance(this);
            }
            catch (final Exception exception) {
                featureException = exception;
            }
            Preconditions.checkState(feature != null, featureException);
            this.features.put(featureClass, feature);
        }
        return feature;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof Graph<?>)) return false;
        final Graph<?> graph = (Graph<?>) object;
        if (this.getOrder() != graph.getOrder()) return false;
        if (!this.get(0).getClass().equals(graph.get(0).getClass())) return false;
        @SuppressWarnings("unchecked")
        final Graph<T> that = (Graph<T>) graph;
        if (!this.idByEndpoint.keySet().equals(that.idByEndpoint.keySet())) return false;
        for (final T endPoint : this)
            if (!Sets.newHashSet(this.getArcsFrom(endPoint)).equals(Sets.newHashSet(that.getArcsFrom(endPoint)))) return false; // ? TODO getArcsFrom(): retouner un set
        return true;
    }

    /*
    @Override
    public String toString() {
        for (final T endpoint : this)
            this.getArcsFrom(endpoint);
        System.out.println(this.arcsByEndpoint);
        return Objects.toStringHelper(this).addValue(this.arcsByEndpoint).toString();
    }
    */

    @Override
    public String toString() {
        final ToStringHelper toStringHelper = Objects.toStringHelper(this);
        for (final T endpoint : this)
            toStringHelper.addValue(this.getArcsFrom(endpoint));
        return toStringHelper.toString();
    }

}