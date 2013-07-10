
package components.cells;

public interface IPosition extends Comparable<IPosition> {

    Integer id();

    int row();

    int column();

    boolean isNull();

}