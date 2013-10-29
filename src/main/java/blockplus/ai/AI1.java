
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

public class AI1 {

    private static void evaluatePositions(final Options options, final Map<IPosition, Integer> map, final int treshold, final int j) {
        final Map<IPosition, Map<Polyomino, List<Set<IPosition>>>> byLight = options.byLight();
        for (final Entry<IPosition, Map<Polyomino, List<Set<IPosition>>>> entry : byLight.entrySet()) {
            for (final List<Set<IPosition>> list : entry.getValue().values()) {
                for (final Set<IPosition> set : list) {
                    for (final IPosition iPosition : set) {
                        Integer n = map.get(iPosition);
                        if (n == null) n = treshold;
                        map.put(iPosition, n + j);
                    }
                }
            }

        }
    }

    private static List<Set<IPosition>> evaluatePolyominoInstances(final Options options, final Map<IPosition, Integer> map, final int treshold) {
        int max = 0;
        final List<Set<IPosition>> bestPolyominoInstances = Lists.newArrayList();
        final Set<Entry<Polyomino, Map<IPosition, List<Set<IPosition>>>>> byPolyomino = options.byPolyomino();
        for (final Entry<Polyomino, Map<IPosition, List<Set<IPosition>>>> entry : byPolyomino) {
            for (final List<Set<IPosition>> list : entry.getValue().values()) {
                for (final Set<IPosition> set : list) {
                    int score = 0;
                    for (final IPosition iPosition : set) {
                        score += treshold / 5;
                        score += map.get(iPosition);
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

        //        System.out.println();
        //        System.out.println("best score: " + max);
        //        System.out.println("instances: " + bestPolyominoInstances.size());
        //        System.out.println();

        return bestPolyominoInstances;
    }

    public Set<IPosition> get(final Context context) {
        final Map<IPosition, Integer> map = Maps.newHashMap();
        final Colors color = context.side();
        final Options options = context.options();
        Context nextContext = context;

        System.out.println(nextContext.side());
        evaluatePositions(nextContext.options(), map, 0, 1);

        int treshold = Integer.MIN_VALUE;
        for (final Entry<IPosition, Integer> entry : map.entrySet()) {
            System.out.println(entry);
            if (entry.getValue() > treshold) treshold = entry.getValue();
        }

        System.out.println(treshold);

        do {
            nextContext = nextContext.forward();
            System.out.println(nextContext.side());
            evaluatePositions(nextContext.options(), map, treshold, 1);
        } while (!nextContext.next().equals(color));
        ////////////////////////////////////////////////////////////////////////
        //        final Set<Entry<IPosition, Integer>> entrySet = map.entrySet();
        //        for (final Entry<IPosition, Integer> entry : entrySet) {
        //            System.out.println(entry);
        //        }
        ////////////////////////////////////////////////////////////////////////
        final List<Set<IPosition>> bestPolyominoInstances = evaluatePolyominoInstances(options, map, treshold);
        System.out.println(bestPolyominoInstances);
        ////////////////////////////////////////////////////////////////////////
        final Polyominos polyominos = Polyominos.getInstance();
        for (final Set<IPosition> set : bestPolyominoInstances) {
            System.out.println(set);
            final PolyominoTranslatedInstance polyominoTranslatedInstance = polyominos.get((SortedSet<IPosition>) set);
            System.out.println(polyominoTranslatedInstance);
            System.out.println();
            break;
        }
        ////////////////////////////////////////////////////////////////////////
        return bestPolyominoInstances.get(0);
    }

    public static void main(final String[] args) {
        final Context context = new Context.Builder().build();
        final AI1 ai = new AI1();
        ai.get(context);
    }

}