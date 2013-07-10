
package components.graph.arc;

public final class Arcs<T> {

    private final Arc.Factory<T> arcFactory;

    private final WeightedArc.Factory<T> weightedArcFactory;

    public Arcs() {
        this.arcFactory = new Arc.Factory<T>();
        this.weightedArcFactory = new WeightedArc.Factory<T>(this.arcFactory);
    }

    public ArcInterface<T> get(final T endPoint1, final T endPoint2) {
        return this.arcFactory.get(endPoint1, endPoint2);
    }

    public WeightedArcInterface<T> get(final T endPoint1, final T endPoint2, final Double weight) {
        return this.weightedArcFactory.get(endPoint1, endPoint2, weight);
    }

}