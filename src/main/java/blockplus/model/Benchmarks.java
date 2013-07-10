
package blockplus.model;


import java.text.NumberFormat;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import blockplus.model.context.Context;
import blockplus.model.context.ContextBuilder;
import blockplus.model.entity.Polyomino;
import blockplus.model.entity.entity.IEntity;
import blockplus.model.entity.projection.PieceInstances;
import blockplus.model.entity.projection.Plane;
import blockplus.model.move.Move;
import blockplus.model.option.IOptionsSupplier;
import blockplus.model.option.OptionsSupplier;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import components.cells.Positions.Position;

public class Benchmarks {

    private static int iteration = 0;

    public static void main(final String[] args) {
        run(new OptionsSupplier(new PieceInstances(Plane.from(20, 20))));
    }

    private static void run(final IOptionsSupplier optionsSupplier) {
        Context context = new ContextBuilder().setOptionsSupplier(optionsSupplier).build();
        final Context initialContext = context;
        final Random random = new Random();
        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (int i = 0; i < 42; ++i) {
            while (!context.isTerminal()) {
                final Table<Position, Polyomino, List<Set<Position>>> options = (Table<Position, Polyomino, List<Set<Position>>>) context.options();
                final Set<Cell<Position, Polyomino, List<Set<Position>>>> cellSet = options.cellSet();
                final Cell<Position, Polyomino, List<Set<Position>>> last = Iterators.getLast(cellSet.iterator());
                ////////////////////////////////////////////////////////////////////////////
                //                int n = 0;
                //                for (final Cell<Position, Polyomino, List<Set<Position>>> object : options.cellSet()) {
                //                    final List<Set<Position>> value = object.getValue();
                //                    for (final Iterable<Position> positions : value) {
                //                        ++n;
                //                    }
                //                }
                //System.out.println(n + " options");
                ////////////////////////////////////////////////////////////////////////////                
                final List<Set<Position>> list = last.getValue();
                //                final int size = list.size();
                //                final int k = random.nextInt(size);
                final Set<Integer> positions = Sets.newTreeSet();
                for (final Position position : list.get(0))
                    positions.add(position.id());
                final IEntity iEntity = context.ENTITIES.get(positions);
                final Move move = new Move(context.getSide(), iEntity);
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