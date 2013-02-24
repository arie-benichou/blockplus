
package components.graph.arc;


public interface WeightedArcInterface<T> extends ArcInterface<T>, Comparable<WeightedArcInterface<T>> {

    public Double getWeight();

    @Override
    int compareTo(final WeightedArcInterface<T> that); // TODO use Comparator instead

}