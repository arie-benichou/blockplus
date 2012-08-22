
package components.board;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import components.position.PositionInterface;

public interface BoardInterface<T extends Symbol> {

    int rows();

    int columns();

    T get(PositionInterface position);

    T get(int rowIndex, int columnIndex);

    BoardInterface<T> apply(Map<PositionInterface, T> mutations);

    BoardInterface<T> copy();

    T undefinedSymbol();

    T initialSymbol();

    Map<PositionInterface, T> filter(Predicate<Entry<PositionInterface, T>> predicate);

}