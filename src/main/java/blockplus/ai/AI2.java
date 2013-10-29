
package blockplus.ai;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.model.Options;
import blockplus.model.polyomino.Polyomino;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;
import blockplus.model.polyomino.Polyominos;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import components.cells.IPosition;

public class AI2 {

    private static void evaluatePositions(final Options options, final Map<IPosition, Integer> map) {
        final Map<IPosition, Map<Polyomino, List<Set<IPosition>>>> byLight = options.byLight();
        for (final Entry<IPosition, Map<Polyomino, List<Set<IPosition>>>> entry : byLight.entrySet()) {
            for (final List<Set<IPosition>> list : entry.getValue().values()) {
                for (final Set<IPosition> set : list) {
                    for (final IPosition iPosition : set) {
                        Integer n = map.get(iPosition);
                        if (n == null) n = 0;
                        map.put(iPosition, n + 1);
                    }
                }
            }

        }
    }

    private static List<Set<IPosition>> evaluatePolyominoInstances(final Options options, final Map<IPosition, Integer> self,
            final Map<IPosition, Integer> others) {
        int max = 0;
        final List<Set<IPosition>> bestPolyominoInstances = Lists.newArrayList();
        final Set<Entry<Polyomino, Map<IPosition, List<Set<IPosition>>>>> byPolyomino = options.byPolyomino();
        for (final Entry<Polyomino, Map<IPosition, List<Set<IPosition>>>> entry : byPolyomino) {
            for (final List<Set<IPosition>> list : entry.getValue().values()) {
                for (final Set<IPosition> set : list) {
                    int score = 0;
                    for (final IPosition iPosition : set) {
                        score += self.get(iPosition);
                        final Integer integer = others.get(iPosition);
                        if (integer != null) score += 2 * integer;
                    }
                    if (score > max) {
                        max = score;
                        bestPolyominoInstances.clear();
                        bestPolyominoInstances.add(set);
                    }
                    else if (score == max) {
                        bestPolyominoInstances.add(set);
                    }
                }
            }
        }
        return bestPolyominoInstances;
    }

    public Set<IPosition> get(final Context context) {
        final Map<IPosition, Integer> self = Maps.newHashMap();
        final Map<IPosition, Integer> others = Maps.newHashMap();
        final Colors color = context.side();
        final Options options = context.options();
        Context nextContext = context;
        evaluatePositions(nextContext.options(), self);
        do {
            nextContext = nextContext.forward();
            evaluatePositions(nextContext.options(), others);
        } while (!nextContext.next().equals(color));
        final List<Set<IPosition>> bestPolyominoInstances = evaluatePolyominoInstances(options, self, others);
        final Set<IPosition> set = bestPolyominoInstances.get(0);
        final Polyominos polyominos = Polyominos.getInstance();
        final PolyominoTranslatedInstance polyominoTranslatedInstance = polyominos.get((SortedSet<IPosition>) set);
        System.out.println(polyominoTranslatedInstance);
        return set;
    }

    public static void main(final String[] args) {
        final Context context = new Context.Builder().build();
        final AI2 ai = new AI2();
        ai.get(context);
    }

}