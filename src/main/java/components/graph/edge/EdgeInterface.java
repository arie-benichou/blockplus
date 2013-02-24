
package components.graph.edge;

import components.graph.arc.ArcInterface;

public interface EdgeInterface<T> extends ArcInterface<T> {

    ArcInterface<T> getArcFromEndPoint1();

    ArcInterface<T> getArcFromEndPoint2();

    @Override
    public EdgeInterface<T> get();

}