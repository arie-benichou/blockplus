/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package components.graph.arc;

import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class ArcDuplicate<T> implements ArcInterface<T> {

    private static <T> ArcInterface<T> check(final ArcInterface<T> arcToDuplicate) {
        Preconditions.checkArgument(arcToDuplicate != null);
        Preconditions.checkArgument(!(arcToDuplicate instanceof ArcDuplicate));
        return arcToDuplicate;
    }

    private static <T> String toString(final ArcInterface<T> arcToDuplicate) {
        return Objects.toStringHelper(ArcDuplicate.class)
                .addValue(arcToDuplicate)
                .toString();
    }

    private static <T> int hashCode(final ArcInterface<T> arcToDuplicate) {
        return toString(arcToDuplicate).hashCode();
    }

    public static class Factory<T> {

        private int cacheHit = 0;

        private final ConcurrentMap<Integer, ArcDuplicate<T>> instances = Maps.newConcurrentMap();

        public ArcDuplicate<T> getDuplicate(final ArcInterface<T> arcToDuplicate) {
            final int hashCode = ArcDuplicate.hashCode(check(arcToDuplicate));
            ArcDuplicate<T> instance = this.instances.get(hashCode);
            if (instance == null) {
                instance = new ArcDuplicate<T>(arcToDuplicate);
                this.instances.put(hashCode, instance);
            }
            else {
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

    public static <T> ArcDuplicate<T> from(final ArcInterface<T> arcToDuplicate) {
        check(arcToDuplicate);
        return new ArcDuplicate<T>(arcToDuplicate);
    }

    private final ArcInterface<T> arc;

    @Override
    public T getEndPoint1() {
        return this.get().getEndPoint1();
    }

    @Override
    public T getEndPoint2() {
        return this.get().getEndPoint2();
    }

    private int hashCode;

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    private ArcDuplicate(final ArcInterface<T> arc, final int hashCode) {
        this.arc = arc;
        this.hashCode = hashCode;
    }

    private ArcDuplicate(final ArcInterface<T> arc) {
        this(arc, hashCode(arc));
    }

    @Override
    public ArcInterface<T> get() {
        return this.arc;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof ArcDuplicate)) return false;
        final Object endPoint = ((ArcDuplicate<?>) object).getEndPoint1();
        if (!endPoint.getClass().equals(this.getEndPoint1().getClass())) return false;
        final ArcDuplicate<?> that = (ArcDuplicate<?>) object;
        final boolean hasSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqal = this.get().equals(that.get());
        Preconditions.checkState(hasSameHashCode == isEqal);
        return isEqal;
    };

    @Override
    public String toString() {
        return toString(this.get());
    }

    public static void main(final String[] args) {
        final ArcDuplicate.Factory<String> virtualArcFactory = new ArcDuplicate.Factory<String>();
        System.out.println(virtualArcFactory);
        final Arc<String> arcToDuplicate1 = Arc.from("A", "B");
        final ArcDuplicate<String> arcDuplicate1 = virtualArcFactory.getDuplicate(arcToDuplicate1);
        System.out.println(arcDuplicate1);
        System.out.println(arcToDuplicate1.equals(arcDuplicate1));
        System.out.println(arcDuplicate1.equals(arcDuplicate1));
        try {
            final ArcDuplicate<String> arcDuplicate2 = virtualArcFactory.getDuplicate(arcDuplicate1);
            System.out.println(arcDuplicate2);
        }
        catch (final Exception e) {
            System.out.println("Duplicate of duplicate prevented :)");
        }
        System.out.println(virtualArcFactory);
    }

}