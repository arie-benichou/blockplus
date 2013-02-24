
package components.graph.arc;


public final class Arcs<T> {

    private final Arc.Factory<T> arcFactory;

    private final WeightedArc.Factory<T> weightedArcFactory;

    private final ArcDuplicate.Factory<T> arcDuplicateFactory;

    public Arcs() {
        this.arcFactory = new Arc.Factory<T>();
        this.arcDuplicateFactory = new ArcDuplicate.Factory<T>();
        this.weightedArcFactory = new WeightedArc.Factory<T>(this.arcFactory);
    }

    public ArcInterface<T> get(final T endPoint1, final T endPoint2) {
        return this.arcFactory.get(endPoint1, endPoint2);
    }

    public ArcDuplicate<T> getDuplicate(final ArcInterface<T> arcToDuplicate) {
        return this.arcDuplicateFactory.getDuplicate(arcToDuplicate);
    }

    public WeightedArcInterface<T> get(final T endPoint1, final T endPoint2, final Double weight) {
        return this.weightedArcFactory.get(endPoint1, endPoint2, weight);
    }

}