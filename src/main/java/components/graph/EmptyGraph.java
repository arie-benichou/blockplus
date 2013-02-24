
package components.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import components.graph.arc.ArcInterface;

public class EmptyGraph<T> implements GraphInterface<T> {

    @Override
    public Iterator<T> iterator() {
        return Iterators.emptyIterator();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean hasEndPoint(final T endPoint) {
        return false;
    }

    @Override
    public Integer getOrdinal(final T endPoint) {
        return null;
    }

    @Override
    public T get(final int ordinal) {
        return null;
    }

    @Override
    public List<T> getConnectedEndPoints(final T endPoint) {
        return ImmutableList.of();
    }

    @Override
    public boolean hasArc(final T endPoint1, final T endPoint2) {
        return false;
    }

    @Override
    public List<ArcInterface<T>> getArcsFrom(final T endPoint) {
        return ImmutableList.of();
    }

    @Override
    public Set<ArcInterface<T>> getSetOfArcs() {
        return ImmutableSet.of();
    }

    @Override
    public ArcInterface<T> getArc(final T endPoint1, final T endPoint2) {
        return null;
    }

}