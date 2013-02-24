
package components.graph.arc;

import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * abstract class sucks :p
 */
public final class Arc<T> implements ArcInterface<T> {

    private static <T> void check(final T endPoint1, final T endPoint2) {
        Preconditions.checkArgument(endPoint1 != null);
        Preconditions.checkArgument(endPoint2 != null);
        //Preconditions.checkArgument(!endPoint1.equals(endPoint2));
    }

    private static <T> String toString(final T endPoint1, final T endPoint2) {
        return Objects.toStringHelper(Arc.class)
                .addValue(endPoint1)
                .addValue(endPoint2)
                .toString();
    }

    private static <T> int hashCode(final T endPoint1, final T endPoint2) {
        return toString(endPoint1, endPoint2).hashCode();
    }

    private static <T> Arc<T> from(final T endPoint1, final T endPoint2, final int hashCode) {
        return new Arc<T>(endPoint1, endPoint2, hashCode);
    }

    public static class Factory<T> {

        private int cacheHit = 0;

        private final ConcurrentMap<Integer, Arc<T>> instances = Maps.newConcurrentMap();

        public Arc<T> get(final T endPoint1, final T endPoint2) {
            check(endPoint1, endPoint2);
            //if (true) return Arc.from(endPoint1, endPoint2); // TODO ! pouvoir activer ou désactiver le cache de la factory
            final int hashCode = Arc.hashCode(endPoint1, endPoint2);
            Arc<T> instance = this.instances.get(hashCode);
            if (instance == null) {
                instance = Arc.from(endPoint1, endPoint2, hashCode);
                this.instances.put(hashCode, instance);
            }
            else {
                Preconditions.checkState(instance.getEndPoint1().equals(endPoint1),
                        "Hash Code Error !" + "Expected Arc: " + Arc.from(endPoint1, endPoint2) + " but factory returned: " + instance);
                Preconditions.checkState(instance.getEndPoint2().equals(endPoint2),
                        "Hash Code Error !" + "Expected Arc: " + Arc.from(endPoint1, endPoint2) + " but factory returned: " + instance);

                ++this.cacheHit;
            }
            return instance;
        }

        public int getCacheHit() {
            return this.cacheHit;
        }

        public int size() {
            return this.instances.size();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("cacheHit", this.getCacheHit())
                    .add("size", this.size())
                    .toString();
        }

    }

    public static <T> Arc<T> from(final T endPoint1, final T endPoint2) {
        check(endPoint1, endPoint2);
        return new Arc<T>(endPoint1, endPoint2);
    }

    private final T endPoint1;

    @Override
    public T getEndPoint1() {
        return this.endPoint1;
    }

    private final T endPoint2;

    @Override
    public T getEndPoint2() {
        return this.endPoint2;
    }

    private final int hashCode;

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    private Arc(final T endPoint1, final T endPoint2, final int hashCode) {
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
        this.hashCode = hashCode;
    }

    private Arc(final T endPoint1, final T endPoint2) {
        this(endPoint1, endPoint2, hashCode(endPoint1, endPoint2));
    }

    @Override
    public Arc<T> get() {
        return this;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Arc)) return false;
        final Object endPoint = ((Arc<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final Arc<?> that = (Arc<?>) object;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqal = this.getEndPoint1().equals(that.getEndPoint1()) && this.getEndPoint2().equals(that.getEndPoint2());
        Preconditions.checkState(hasSameHashCode == isEqal);
        return isEqal;
    };

    @Override
    public String toString() {
        return toString(this.getEndPoint1(), this.getEndPoint2());
    }

}