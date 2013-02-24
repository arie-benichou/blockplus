
package components.graph.arc;

import com.google.common.base.Supplier;

public interface ArcInterface<T> extends Supplier<ArcInterface<T>> {

    T getEndPoint1();

    T getEndPoint2();

}