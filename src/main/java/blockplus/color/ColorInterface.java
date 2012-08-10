
package blockplus.color;

public interface ColorInterface {

    final static ColorInterface OPAQUE = PrimeColors.OPAQUE;
    final static ColorInterface UNKNOWN = PrimeColors.UNKNOWN;
    final static ColorInterface TRANSPARENT = PrimeColors.TRANSPARENT;

    final static ColorInterface BLUE = PrimeColors.BLUE;
    final static ColorInterface blue = PrimeColors.blue;

    final static ColorInterface YELLOW = PrimeColors.YELLOW;
    final static ColorInterface yellow = PrimeColors.yellow;

    final static ColorInterface RED = PrimeColors.RED;
    final static ColorInterface red = PrimeColors.red;

    final static ColorInterface GREEN = PrimeColors.GREEN;
    final static ColorInterface green = PrimeColors.green;

    final static ColorInterface WHITE = PrimeColors.WHITE;
    final static ColorInterface white = PrimeColors.white;

    ColorInterface potential();

    int value();

    boolean is(ColorInterface color);

    boolean contains(ColorInterface color);

    boolean hasOpacity();

    boolean hasTransparency();

    String name();

}
