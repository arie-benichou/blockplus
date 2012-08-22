
package blockplus.model.color;

import java.util.List;
import java.util.Set;

import components.board.Symbol;

public interface ColorInterface extends Symbol, Iterable<ColorInterface> {

    int size();

    int count(ColorInterface color);

    boolean contains(ColorInterface color);

    boolean is(ColorInterface color);

    boolean isEmpty();

    ColorInterface remove(ColorInterface color);

    Set<ColorInterface> set();

    List<ColorInterface> list();

    boolean isPrime();

}