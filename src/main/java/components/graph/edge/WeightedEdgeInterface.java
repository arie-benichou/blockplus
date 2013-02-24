
package components.graph.edge;


public interface WeightedEdgeInterface<T> extends EdgeInterface<T>, Comparable<WeightedEdgeInterface<T>> {

    public Double getWeight();

    @Override
    int compareTo(final WeightedEdgeInterface<T> that);

}