
package blockplus;

import java.text.NumberFormat;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import blockplus.model.Context;
import blockplus.model.Move;
import blockplus.model.Options;

import com.google.common.base.Stopwatch;
import components.cells.IPosition;

public class Benchmarks {

    private static int iteration = 0;

    public static void main(final String[] args) {
        Context context = new Context.Builder().build();
        final Context initialContext = context;
        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (int i = 0; i < 100; ++i) {
            while (!context.isTerminal()) {
                final Options options = context.options();
                final List<Set<IPosition>> list = options.toList();
                final SortedSet<IPosition> positions = (SortedSet<IPosition>) list.get(list.size() - 1);
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