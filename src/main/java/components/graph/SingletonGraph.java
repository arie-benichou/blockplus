
package components.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import components.graph.arc.ArcInterface;
import components.graph.arc.WeightedArc;

public class SingletonGraph<T> implements GraphInterface<T> {

    private final T singlePoint;

    private final Set<T> nodeSet;
    private final ImmutableList<T> nodeList;

    private final ArcInterface<T> arc;
    private final ImmutableList<ArcInterface<T>> arcs;

    private final ImmutableList<T> emptyList = ImmutableList.of();

    public SingletonGraph(final T singlePoint) {
        Preconditions.checkArgument(singlePoint != null);
        this.singlePoint = singlePoint;
        this.nodeSet = ImmutableSet.of(this.singlePoint);
        this.nodeList = ImmutableList.of(this.singlePoint);
        this.arc = WeightedArc.from(this.singlePoint, this.singlePoint);
        this.arcs = ImmutableList.of(this.arc);
    }

    @Override
    public Iterator<T> iterator() {
        return this.nodeSet.iterator();
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public boolean hasEndPoint(final T endPoint) {
        return this.singlePoint.equals(endPoint);
    }

    @Override
    public Integer getOrdinal(final T endPoint) {
        return this.hasEndPoint(endPoint) ? 0 : null;
    }

    @Override
    public T get(final int ordinal) {
        return ordinal == 0 ? this.singlePoint : null;
    }

    @Override
    public List<T> getConnectedEndPoints(final T endPoint) {
        return this.hasEndPoint(endPoint) ? this.nodeList : this.emptyList;
    }

    @Override
    public boolean hasArc(final T endPoint1, final T endPoint2) {
        return this.hasEndPoint(endPoint1) && this.hasEndPoint(endPoint2);
    }

    @Override
    public List<ArcInterface<T>> getArcsFrom(final T endPoint) {
        return this.arcs;
    }

    @Override
    public Set<ArcInterface<T>> getSetOfArcs() {
        return ImmutableSet.copyOf(this.arcs);
    }

    @Override
    public ArcInterface<T> getArc(final T endPoint1, final T endPoint2) {
        return this.hasArc(endPoint1, endPoint2) ? this.arc : null;
    }

}