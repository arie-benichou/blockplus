
package components.cells;

public interface IPosition extends Comparable<IPosition> {

    int row();

    int column();

    @Override
    int compareTo(IPosition that);

    boolean isNull();

}