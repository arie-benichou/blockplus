/*
 * Copyright 2012-2013 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package game.blockplus.option;

import game.blockplus.board.Board;
import game.blockplus.context.Color;
import game.blockplus.context.Context;
import game.blockplus.player.RemainingPieces;
import game.blockplus.polyomino.Polyomino;
import game.blockplus.polyomino.Polyominos;
import game.blockplus.polyomino.projection.PieceInstances;
import game.blockplus.polyomino.projection.PieceInstances.PieceInstance;
import game.blockplus.polyomino.projection.PieceInstancesFactory;
import game.interfaces.IContext;
import game.interfaces.IOptionsSupplier;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import components.cells.Positions.Position;

public final class OptionsSupplier implements IOptionsSupplier {

    //public static Stopwatch stopwatch = new Stopwatch();

    private final static ImmutableSortedMap<Integer, ImmutableSortedSet<Polyomino>> POLYOMINOS_BY_RADIUS = Polyominos.getInstance().getByRadius();

    private final PieceInstancesFactory pieceInstancesFactory;

    private final Integer minRadius;

    private final Integer maxRadius;

    public PieceInstancesFactory getPieceInstancesFactory() {
        return this.pieceInstancesFactory;
    }

    public OptionsSupplier(final PieceInstances pieceInstances) {
        this.pieceInstancesFactory = new PieceInstancesFactory(pieceInstances);
        this.minRadius = POLYOMINOS_BY_RADIUS.firstKey();
        this.maxRadius = POLYOMINOS_BY_RADIUS.lastKey();
    }

    private Map<Position, Set<Position>> getPotentialPositionsByLight(final Board board, final Color color, final Iterable<Position> lights, final int radius) {
        final Map<Position, Set<Position>> map = Maps.newLinkedHashMap();
        for (final Position light : lights) {
            final Set<Position> potentialReferentialPositions = Sets.newTreeSet();
            for (int k = 0; k <= radius; ++k)
                for (final Position neighbour : board.neighbours(light, k))
                    if (board.get(color).isMutable(neighbour)) potentialReferentialPositions.add(neighbour);
            map.put(light, potentialReferentialPositions);
        }
        return map;
    }

    private List<Set<Position>> getLegalPositions(final Color side, final Board board, final Position potentialPosition,
            final Iterable<PieceInstance> instances) {
        final List<Set<Position>> options = Lists.newArrayList();
        for (final PieceInstance pieceInstance : instances) {
            final PieceInstance translatedPieceInstance = pieceInstance.on(potentialPosition);
            if (board.get(side).isLegal(translatedPieceInstance)) options.add((Set<Position>) translatedPieceInstance.positions());
        }
        return options;
    }

    @Override
    public Table<Position, Polyomino, List<Set<Position>>> options(final IContext<?> contextInterface) {
        //stopwatch.start();
        final Context context = (Context) contextInterface;
        final Color side = context.getSide();
        final Board board = context.getBoard();
        final Iterable<Position> lights = board.get(side).getLights().keySet();
        final RemainingPieces remainingPieces = context.getPlayer().remainingPieces();
        final Table<Position, Polyomino, List<Set<Position>>> table = TreeBasedTable.create();
        for (int radius = this.minRadius; radius <= this.maxRadius; ++radius) {
            final Map<Position, Set<Position>> potentialPositions = this.getPotentialPositionsByLight(board, side, lights, radius);
            final ImmutableSortedSet<Polyomino> polyominos = POLYOMINOS_BY_RADIUS.get(radius);
            for (final Polyomino polyomino : polyominos) {
                if (remainingPieces.contains(polyomino)) {
                    final Iterable<PieceInstance> instances = this.getPieceInstancesFactory().get(polyomino);
                    for (final Entry<Position, Set<Position>> entry : potentialPositions.entrySet()) {
                        final Position position = entry.getKey();
                        final List<Set<Position>> options = Lists.newArrayList();
                        for (final Position potentialPosition : entry.getValue())
                            options.addAll(this.getLegalPositions(side, board, potentialPosition, instances));
                        if (!options.isEmpty()) table.put(position, polyomino, options);
                    }
                }
            }
        }
        //stopwatch.stop();
        return table;
    }
}