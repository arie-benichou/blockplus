
package blockplus.model.color;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public final class NullColor implements ColorInterface {

    private final static ColorInterface INSTANCE = new NullColor();

    public static ColorInterface getInstance() {
        return INSTANCE;
    }

    private NullColor() {}

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int count(final ColorInterface color) {
        return 0;
    }

    @Override
    public boolean contains(final ColorInterface color) {
        return false;
    }

    @Override
    public boolean is(final ColorInterface color) {
        return color.size() == 0;
    }

    @Override
    public ColorInterface remove(final ColorInterface color) {
        return this; // TODO ? use for shadows
    }

    @Override
    public Set<ColorInterface> set() {
        return ImmutableSet.of();
    }

    @Override
    public List<ColorInterface> list() {
        return ImmutableList.of();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isPrime() {
        return false;
    }

    @Override
    public Iterator<ColorInterface> iterator() {
        return this.list().iterator();
    }

    @Override
    public String toString() {
        return "Black";
    }

}