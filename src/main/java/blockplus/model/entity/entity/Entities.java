
package blockplus.model.entity.entity;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import blockplus.model.entity.Polyomino;
import blockplus.model.entity.projection.PieceInstances;
import blockplus.model.entity.projection.Plane;
import blockplus.model.entity.projection.PieceInstances.PieceInstance;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.MongoClient;
import components.cells.Positions;
import components.cells.Positions.Position;

public final class Entities {

    private final static JsonObject INDEX_QUERY = new JsonObject();
    static {
        INDEX_QUERY.add("id", new JsonPrimitive(1));
        INDEX_QUERY.add("unique", new JsonPrimitive(true));
        INDEX_QUERY.add("background", new JsonPrimitive(true));
    }

    private static Jongo jongo;

    static {
        try {
            jongo = new Jongo(new MongoClient().getDB("blockplus"));
            //jongo.getDatabase().dropDatabase();
        }
        catch (final UnknownHostException e) {
            Throwables.propagate(e);
        }
    }

    private final Positions positionFactory;

    private final MongoCollection collection = jongo.getCollection("entities");

    private final Map<String, IEntity> ramCache = Maps.newTreeMap();

    public Entities(final Positions positionFactory) {
        this.positionFactory = positionFactory;
        this.collection.ensureIndex(INDEX_QUERY.toString());
    }

    private Position getPosition(final Integer id) {
        return this.positionFactory.get(id);
    }

    private Set<Position> normalize(final Iterable<Integer> positions) {
        final Set<Position> positionIds = Sets.newTreeSet();
        for (final Integer id : positions) {
            positionIds.add(this.getPosition(id));
        }
        return positionIds;
    }

    public IEntity _get(final Iterable<Integer> positions) {
        final Set<Position> normalizedPositions = this.normalize(positions);
        return Entity.from(normalizedPositions, this.positionFactory);
    }

    public IEntity get(final Iterable<Integer> positions) {
        final Set<Position> normalizedPositions = this.normalize(positions);
        final String id = ComputedEntity.computeId(normalizedPositions);
        IEntity iEntity = this.ramCache.get(id);
        if (iEntity == null) this.ramCache.put(id, iEntity = this.get(id, normalizedPositions));
        //else System.out.println("from RAM: " + iEntity.type());// + " : " + ((ComputedEntity) iEntity).id);
        return iEntity;
    }

    private IEntity getFromDatabase(final String id) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(id));
        return this.collection.findOne(jsonObject.toString()).as(ComputedEntity.class);
    }

    private IEntity get(final String id, final Set<Position> normalizedPositions) {
        IEntity entity = this.getFromDatabase(id);
        if (entity == null) {
            entity = Entity.from(normalizedPositions, this.positionFactory);
            final ComputedEntity computedEntity = ComputedEntity.decorate(entity);
            this.collection.save(computedEntity);
        }
        return entity;
    }

    // TODO extract caching routine
    public static void main(final String[] args) {
        final int ROWS = 20;
        final int COLUMNS = 20;

        final Plane plane = Plane.from(20, 20);
        final PieceInstances pieceInstanceFactory = new PieceInstances(plane);
        final Entities entities = new Entities(plane.positions());

        int n = 0;
        for (final Polyomino polyomino : Polyomino.set()) {
            //final Polyomino polyomino = Polyomino._14;
            System.out.println(polyomino);
            try {
                Thread.sleep(100);
            }
            catch (final InterruptedException e) {}
            for (int i = 0; i < ROWS; ++i) {
                try {
                    Thread.sleep(50);
                }
                catch (final InterruptedException e) {}
                System.out.println(i);
                for (int j = 0; j < COLUMNS; ++j) {
                    try {
                        Thread.sleep(25);
                    }
                    catch (final InterruptedException e) {}
                    final Iterable<PieceInstance> instances = pieceInstanceFactory.from(polyomino.get());
                    for (final PieceInstance pieceInstance : instances) {
                        final PieceInstance on = pieceInstance.on(plane.positions().get(i, j));
                        if (on.iterator().hasNext()) {
                            ++n;
                            final Set<Integer> ids = Sets.newTreeSet();
                            for (final Position p : on) {
                                ids.add(p.id());
                            }
                            final IEntity iEntity = entities.get(ids);
                            System.out.println(Entity.render(iEntity)); // TODO render dans projection
                        }
                    }
                }
            }
        }
        System.out.println(n);
    }

}