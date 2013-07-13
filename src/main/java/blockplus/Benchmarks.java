
package blockplus;

import java.text.NumberFormat;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import blockplus.model.Context;
import blockplus.model.Move;
import blockplus.model.polyomino.Polyomino;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import components.cells.IPosition;

public class Benchmarks {

    private static int iteration = 0;

    public static void main(final String[] args) {
        Context context = new Context.Builder().build();
        final Context initialContext = context;
        final Random random = new Random();
        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (int i = 0; i < 100; ++i) {
            while (!context.isTerminal()) {
                final Table<IPosition, Polyomino, List<Set<IPosition>>> options = (Table<IPosition, Polyomino, List<Set<IPosition>>>) context.options();
                final Set<Cell<IPosition, Polyomino, List<Set<IPosition>>>> cellSet = options.cellSet();
                final Cell<IPosition, Polyomino, List<Set<IPosition>>> last = Iterators.getLast(cellSet.iterator());
                ////////////////////////////////////////////////////////////////////////////
                //                int n = 0;
                //                for (final Cell<IPosition, Polyomino, List<Set<IPosition>>> object : options.cellSet()) {
                //                    final List<Set<IPosition>> value = object.getValue();
                //                    for (final Iterable<IPosition> positions : value) {
                //                        ++n;
                //                    }
                //                }
                //System.out.println(n + " options");
                ////////////////////////////////////////////////////////////////////////////                
                final List<Set<IPosition>> list = last.getValue();
                final SortedSet<IPosition> positions = (SortedSet<IPosition>) list.get(0);
                //                final int size = list.size();
                //                final int k = random.nextInt(size);
                //System.out.println(positions);
                final Move move = new Move(context.side(), positions);
                context = context.apply(move).forward();
                ++iteration;
            }
            context = initialContext;
        }
        stopwatch.stop();
        final long time = stopwatch.elapsedTime(TimeUnit.NANOSECONDS);
        final double t = time / Math.pow(10, 9);
        final double m = time / Math.pow(10, 6) / iteration;
        final NumberFormat instance = NumberFormat.getInstance();
        System.out.println(iteration + " moves");
        System.out.println(instance.format(t) + " " + TimeUnit.SECONDS);
        System.out.println(instance.format(m) + " " + TimeUnit.MILLISECONDS);
    }
}