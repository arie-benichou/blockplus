
package blockplus.color;

public class Color implements ColorInterface {

    private final String name;
    private final int value;

    public Color(final ColorInterface color1, final ColorInterface color2) {
        this.name = color1.name() + " * " + color2.name();
        this.value = -Math.abs(color1.value() * color2.value());
    }

    public Color(final int value) {
        this.name = "" + value;
        this.value = -value;
    }

    @Override
    public int value() {
        return this.value;
    }

    @Override
    public boolean is(final ColorInterface color) {
        return this.value() == color.value();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ColorInterface potential() {
        throw new RuntimeException(); // TODO à revoir
    }

    @Override
    public boolean contains(final ColorInterface color) {
        return Math.abs(this.value()) % Math.abs(color.value()) == 0;
    }

    @Override
    public boolean hasOpacity() {
        return false;
    }

    @Override
    public boolean hasTransparency() {
        return true;
    }

    @Override
    public String toString() {
        return this.name();
    }

}