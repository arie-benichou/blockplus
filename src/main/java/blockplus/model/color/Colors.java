
package blockplus.model.color;

public final class Colors {

    public final static ColorInterface Black = NullColor.getInstance();

    public final static ColorInterface Blue = PrimeColors.Blue;
    public final static ColorInterface Yellow = PrimeColors.Yellow;
    public final static ColorInterface Red = PrimeColors.Red;
    public final static ColorInterface Green = PrimeColors.Green;

    public final static ColorInterface White =
                                               new Color.Builder()
                                                       .add(Blue)
                                                       .add(Yellow)
                                                       .add(Red)
                                                       .add(Green)
                                                       .build();

    private Colors() {}

}