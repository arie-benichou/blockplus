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

package blockplus.model.entity.entity;


import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.model.entity.entity.Node.Type;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultiset;
import components.cells.Directions;
import components.cells.Directions.Direction;
import components.cells.ICells;
import components.cells.Positions;
import components.cells.Positions.Position;
import components.graph.GraphInterface;
import components.graph.arc.ArcInterface;
import components.graph.arc.WeightedArc;
import components.graph.features.degree.DegreeFeature;
import components.graph.features.degree.DegreeInterface;

public final class Entity implements IEntity {

    private static Position computeReferential(final Entity entity) { // TODO à mettre dans un EntityManager
        final ICells<String> cells = entity.getCells();
        if (cells.get().isEmpty()) return cells.position(-1, -1);
        final Set<Position> positions = cells.get().keySet();
        final double n = positions.size();
        int sumY = 0, sumX = 0;
        for (final Position position : positions) {
            sumY += position.row();
            sumX += position.column();
        }
        final int refY = (int) Math.round(sumY / n - 0.1);
        final int refX = (int) Math.round(sumX / n - 0.1);
        Position referential = entity.getCells().position(refY, refX);
        final String cell = entity.getCell(refY, refX);
        if (!cell.equals(AsCells.SOME)) {
            final GraphInterface<Node> graph = entity.asGraph().get();
            final DegreeInterface<Node> degree = new DegreeFeature(graph).getInterface();
            Entry<Node, Integer> max = null;
            final Set<Entry<Node, Integer>> entrySet = degree.getDegreeByNode().entrySet();
            for (final Entry<Node, Integer> entry : entrySet)
                if (max == null || entry.getValue() > max.getValue()) max = entry;
            referential = max.getKey().id();
        }
        return referential;
    }

    public String getCell(final int row, final int column) {
        return this.asCells.getCell(row, column);
    }

    public ICells<String> getCells() {
        return this.asCells.getCells();
    }

    private static int computeRadius(final Entity entity) { // TODO à mettre dans un EntityManager
        final Position referential = entity.referential();
        if (referential.isNull()) return -1;
        final int refY = referential.row(), refX = referential.column();
        int yMin = Integer.MAX_VALUE, xMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE, xMax = Integer.MIN_VALUE;
        for (final Node node : entity.asGraph().get()) {
            final int y = node.id().row(), x = node.id().column();
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
            if (x < xMin) xMin = x;
            if (x > xMax) xMax = x;
        }
        final List<Integer> deltas = Lists.newArrayList(Math.abs(refY - yMin), Math.abs(refY - yMax), Math.abs(refX - xMin), Math.abs(refX - xMax));
        return Collections.max(deltas);
    }

    private static Integer computeId(final Entity entity) { // TODO à mettre dans un EntityManager
        final TreeMultiset<String> footPrint = TreeMultiset.create();
        final GraphInterface<Node> graph = entity.asGraph().get();
        for (final Node endpoint : graph) {
            final List<ArcInterface<Node>> arcs = graph.getArcsFrom(endpoint);
            for (final ArcInterface<Node> arc : arcs) {
                final WeightedArc<Node> weightedArc = (WeightedArc<Node>) arc;
                final int weight = weightedArc.getWeight().intValue();
                final Node endPoint1 = weightedArc.getEndPoint1();
                final Node endPoint2 = weightedArc.getEndPoint2();
                footPrint.add(weight + "$ x " + "(" + endPoint1.type() + ", " + endPoint2.type() + ")");
            }
        }
        final DegreeInterface<Node> degree = new DegreeFeature(graph).getInterface();
        final Map<Node, Integer> degreeByNode = degree.getNodesHavingDegree(1);
        int relativeXProduct = 1, relativeYProduct = 1;
        final int refRow = entity.referential().row(), refColumn = entity.referential().column();
        for (final Node node : degreeByNode.keySet()) {
            relativeYProduct *= node.id().row() - refRow;
            relativeXProduct *= node.id().column() - refColumn;
        }
        footPrint.add(entity.radius() + " " + relativeXProduct * relativeYProduct);
        return Math.abs(footPrint.hashCode());
    }

    // TODO ? à mettre dans entity
    public static Iterable<Position> computeLights(final Entity entity) {
        final Set<Position> corners = Sets.newTreeSet();
        for (final Node node : entity.asGraph().get()) {
            final Position position = node.id();
            final Type nodeType = node.type();
            if (nodeType.equals(Type.DEAD_END) || nodeType.equals(Type.CORNER) || nodeType.equals(Type.ISLAND)) {
                for (final Direction direction : Directions.CORNERS) {
                    final Position p = entity.getCells().position(position.row() + direction.rowDelta(),
                            position.column() + direction.columnDelta());
                    if (!p.isNull()) corners.add(p);
                }
            }
        }
        final Set<Position> sides = Sets.newTreeSet();
        for (final Position position : entity) {
            for (final Direction direction : Directions.SIDES) {
                final Position p = entity.getCells().position(position.row() + direction.rowDelta(), position.column() + direction.columnDelta());
                if (!p.isNull()) sides.add(p);
            }
        }
        return Sets.difference(corners, sides);
    }

    // TODO ? à mettre dans entity
    public static Iterable<Position> computeShadows(final Entity entity) {
        final Set<Position> selves = Sets.newTreeSet();
        final Set<Position> sides = Sets.newTreeSet();
        for (final Position position : entity) {
            for (final Direction direction : Directions.SIDES) {
                final Position p = entity.getCells().position(position.row() + direction.rowDelta(), position.column() + direction.columnDelta());
                if (!p.isNull()) sides.add(p);
            }
            selves.add(position);
        }
        return Sets.difference(sides, selves);
    }

    // TODO normaliser l'entité avant
    public static String render(final IEntity entity) {
        throw new RuntimeException();
        /*
        if (entity == null) return "NULL"; // TODO NullEntity
        final StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append("\n###########################\n");

        int min = Integer.MAX_VALUE;
        for (final Position position : entity) {
            if (position.column() < min) {
                min = position.column();
            }
        }

        //System.out.println(min);

        int previousRow = -1;
        int previousColumn = min;

        System.out.println(entity.type());

        for (final Position position : entity) {
            //System.out.println(position.id());
            final int row = position.row();
            final int column = position.column();
            final int diffRow = row - previousRow;
            final int diffColumn = column - previousColumn;
            previousRow = row;
            previousColumn = column;
            if (diffRow != 0) {
                stringBuilder.append("\n");
                stringBuilder.append(Strings.repeat(".", Math.max(0, diffColumn)));
            }
            else {
                stringBuilder.append(Strings.repeat(".", Math.max(0, diffColumn - 1)));
            }
            stringBuilder.append("O");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
        */
    }

    public static Entity from(final String[] data) {
        return new Entity(AsCells.from(data));
    }

    public static Entity from(final Iterable<Position> positions, final Positions positionFactory) {
        return new Entity(AsCells.from(positions, positionFactory));
    }

    private final AsCells asCells;

    private volatile AsGraph asGraph;
    private volatile Integer radius;
    private volatile Position referential;
    private volatile Integer id;

    private Entity(final AsCells asCells) {
        this.asCells = asCells;
    }

    public AsGraph asGraph() {
        AsGraph asGraph = this.asGraph;
        if (asGraph == null) {
            synchronized (this) {
                if ((asGraph = this.asGraph) == null) this.asGraph = asGraph = AsGraph.from(this.asCells);
            }
        }
        return asGraph;
    }

    @Override
    public Position referential() {
        Position referential = this.referential;
        if (referential == null) {
            synchronized (this) {
                if ((referential = this.referential) == null) this.referential = referential = Entity.computeReferential(this);
            }
        }
        return referential;
    }

    @Override
    public Integer radius() {
        Integer radius = this.radius;
        if (radius == null) {
            synchronized (this) {
                if ((radius = this.radius) == null) this.radius = radius = Entity.computeRadius(this);
            }
        }
        return radius;
    }

    @Override
    public Integer type() {
        Integer id = this.id;
        if (id == null) {
            synchronized (this) {
                if ((id = this.id) == null) this.id = id = Entity.computeId(this);
            }
        }
        return this.id;
    }

    @Override
    public Iterable<Position> positions() {
        return this.getCells().get().keySet();
    }

    @Override
    public Iterable<Position> shadows() { // TODO lazy
        return computeShadows(this);
    }

    @Override
    public Iterable<Position> lights() { // TODO lazy
        return computeLights(this);
    }

    @Override
    public Iterator<Position> iterator() {
        return this.positions().iterator();
    }

    @Override
    public boolean isNull() {
        return !this.iterator().hasNext();
    }

    @Override
    public String toString() {
        return render(this);
    }

    /*
    public static void main(final String[] args) {
        final Stopwatch stopwatch = new Stopwatch().start();
        final Builder<Integer, Entity> builder = new ImmutableBiMap.Builder<Integer, Entity>();
        for (final Polyomino polyomino : Polyomino.set()) {
            final Entity entity = polyomino.get();
            final Position referential = entity.referential();
            final Integer radius = entity.radius();
            final Integer id = Entity.computeId(entity);
            System.out.println();
            System.out.println(polyomino.ordinal() + 1);
            System.out.println(referential);
            System.out.println(radius);
            System.out.println(id);
            System.out.println();
            builder.put(id, entity);
        }
        stopwatch.stop();
        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
        //final ImmutableBiMap<Integer, Entity> map = builder.build();
        //tests1(map);
        //tests2();
    }
    */

    /*
    private static void tests1(final ImmutableBiMap<Integer, Entity> map) {
        System.out.println("-------------------------------------------\n");

        {
            final String[] data = new String[] {
                    "O O",
                    "OOO"
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(map.get(id));
            System.out.println(id.hashCode());
        }

        System.out.println();

        {
            final String[] data = new String[] {
                    "OOO",
                    "O O"
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(id.hashCode());
            System.out.println(map.get(id));
        }

        System.out.println();

        {
            final String[] data = new String[] {
                    "OO",
                    "O ",
                    "OO",
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(id.hashCode());
            System.out.println(map.get(id));
        }

        System.out.println();

        {
            final String[] data = new String[] {
                    "OO",
                    " O",
                    "OO",
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(id.hashCode());
            System.out.println(map.get(id));
        }

        System.out.println("-------------------------------------------\n");

        {
            final String[] data = new String[] {
                    "O  ",
                    "OOO",
                    "O  "
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(map.get(id));
            System.out.println(id.hashCode());
        }

        System.out.println();

        {
            final String[] data = new String[] {
                    "  O",
                    "OOO",
                    "  O"
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(id.hashCode());
            System.out.println(map.get(id));
        }

        System.out.println();

        {
            final String[] data = new String[] {
                    "OOO",
                    " O ",
                    " O "
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(id.hashCode());
            System.out.println(map.get(id));
        }

        System.out.println();

        {
            final String[] data = new String[] {
                    " O ",
                    " O ",
                    "OOO"
            };
            final Entity entity = Entity.from(data);
            final Integer id = Entity.computeId(entity);
            System.out.println(id);
            System.out.println(id.hashCode());
            System.out.println(map.get(id));
        }
    }
    */

}