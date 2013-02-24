
package components.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import components.graph.arc.ArcDuplicate;
import components.graph.arc.ArcInterface;
import components.graph.arc.Arcs;

public final class GraphBuilder<T> {

    private static <T> void check(final int order, final Arcs<T> arcsFactory) {
        Preconditions.checkArgument(order >= 2, order);
        Preconditions.checkArgument(arcsFactory != null);
    }

    private final int order;

    public int getOrder() {
        return this.order;
    }

    private final Arcs<T> arcsFactory;

    public Arcs<T> getArcFactory() {
        return this.arcsFactory;
    }

    private final Map<T, Integer> idByEndpoint;

    public Map<T, Integer> getIdByEndpoint() {
        return this.idByEndpoint;
    }

    private final Map<T, List<T>> connectedEndPointsByEndPoint;

    public Map<T, List<T>> getConnectedEndPointsByEndPoint() {
        return this.connectedEndPointsByEndPoint;
    }

    private final Set<ArcInterface<T>> arcs;

    public Set<ArcInterface<T>> getArcs() {
        return this.arcs;
    }

    private final ImmutableSortedMap.Builder<Integer, ArcInterface<T>> arcByHashCodeBuilder;

    public ImmutableSortedMap.Builder<Integer, ArcInterface<T>> getArcByHashCodeBuilder() {
        return this.arcByHashCodeBuilder;
    }

    private int id;

    public GraphBuilder(final int order, final Arcs<T> arcsFactory, final boolean isInUserMode) {
        check(order, arcsFactory); // TODO
        this.order = order;
        this.arcsFactory = arcsFactory;
        this.idByEndpoint = Maps.newHashMap();
        this.arcs = Sets.newHashSet();
        this.connectedEndPointsByEndPoint = Maps.newHashMap();
        this.arcByHashCodeBuilder = new ImmutableSortedMap.Builder<Integer, ArcInterface<T>>(Ordering.natural());
        this.id = 0;
    }

    // TODO injecter une factory de type Arcs 
    public GraphBuilder(final int order, final Arcs<T> arcFactory) {
        this(order, arcFactory, GraphInterface.USER_MODE);
    }

    public GraphBuilder(final int order) {
        this(order, new Arcs<T>());
    }

    public boolean contains(final ArcInterface<T> arc) {
        return this.getArcs().contains(arc); // TODO !!! utiliser arcByHashCodeBuilder
    }

    private Integer getId(final T endPoint) {
        return this.getIdByEndpoint().get(endPoint);
    }

    private T checkEndPoint(final T endPoint) {
        if (this.getId(endPoint) == null) {
            Preconditions.checkState(this.id != this.order, "Maximal number of vertices (" + this.order + ") reached.");
            this.getIdByEndpoint().put(endPoint, ++this.id);
            this.getConnectedEndPointsByEndPoint().put(endPoint, new ArrayList<T>());
        }
        return endPoint;
    }

    private int hashCode(final ArcInterface<T> arc, final boolean isDuplicate) {
        return (isDuplicate ? -1 : 1) * (this.getOrder() * this.getId(arc.getEndPoint1()) + this.getId(arc.getEndPoint2()));
    }

    public GraphBuilder<T> addArc(final ArcInterface<T> arc) {
        Preconditions.checkArgument(arc != null);
        Preconditions.checkState(!this.contains(arc), "Arc " + arc + " is already defined.");
        final T endPoint1 = this.checkEndPoint(arc.getEndPoint1());
        final T endPoint2 = this.checkEndPoint(arc.getEndPoint2());
        this.getArcs().add(arc);
        this.getConnectedEndPointsByEndPoint().get(endPoint1).add(endPoint2);
        this.getArcByHashCodeBuilder().put(this.hashCode(arc, false), arc);
        return this;
    }

    // TODO ! vérifier que l'arc n'est pas déjà dupliqué
    public ArcDuplicate<T> addDuplicate(final ArcInterface<T> arc) {
        Preconditions.checkArgument(arc != null);
        Preconditions.checkState(this.contains(arc), "Arc " + arc + " is not defined.");
        final ArcDuplicate<T> arcDuplicate = ArcDuplicate.from(arc); // TODO
        this.getArcs().add(arcDuplicate);
        this.getConnectedEndPointsByEndPoint().get(arcDuplicate.getEndPoint1()).add(arcDuplicate.getEndPoint2());
        this.getArcByHashCodeBuilder().put(this.hashCode(arcDuplicate, true), arcDuplicate);
        return arcDuplicate;
    }

    public GraphBuilder<T> addArc(final T endPoint1, final T endPoint2, final double weight) {
        return this.addArc(this.getArcFactory().get(endPoint1, endPoint2, weight));
    }

    public GraphBuilder<T> addArc(final T endPoint1, final T endPoint2) {
        return this.addArc(endPoint1, endPoint2, 1);
    }

    public GraphBuilder<T> addEdge(final T endPoint1, final T endPoint2, final double weight) {
        this.addArc(this.getArcFactory().get(endPoint1, endPoint2, weight));
        this.addArc(this.getArcFactory().get(endPoint2, endPoint1, weight));
        return this;
    }

    public GraphBuilder<T> addEdge(final T endPoint1, final T endPoint2) { // TODO
        return this.addEdge(endPoint1, endPoint2, 1.0);
    }

    public Graph<T> build(final boolean isInUserMode) {
        final int order = this.getConnectedEndPointsByEndPoint().size();
        Preconditions
                .checkState(order == this.order, "Declared number of vertices: " + this.order + " does not match number of defined vertices: " + order);
        return Graph.from(this, isInUserMode);
    }

    public Graph<T> build() {
        return this.build(GraphInterface.USER_MODE);
    }

}