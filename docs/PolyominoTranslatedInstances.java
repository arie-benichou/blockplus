
package blockplus.model.polyomino;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.SortedSet;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.MongoClient;
import components.cells.IPosition;

public final class PolyominoTranslatedInstances {

    private final static Gson GSON = new Gson();

    private final static JsonObject INDEX_QUERY = new JsonObject();
    static {
        INDEX_QUERY.add("positions", new JsonPrimitive(1));
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

    private final MongoCollection collection = jongo.getCollection("instances");

    private final Map<SortedSet<IPosition>, PolyominoTranslatedInstance> ramCache = Maps.newHashMap();

    public PolyominoTranslatedInstances() {
        this.collection.ensureIndex(INDEX_QUERY.toString());
    }

    public PolyominoTranslatedInstance get(final SortedSet<IPosition> positions) {
        final Polyominos polyominos = Polyominos.getInstance();
        final String rendering = PolyominoRenderer.render(positions);
        final Polyomino type = polyominos.getType(rendering);
        return polyominos.computeTranslatedInstance(positions, polyominos.getInstance(rendering), type);
    }

    /*
    public PolyominoTranslatedInstance get(final SortedSet<IPosition> positions) {
        PolyominoTranslatedInstance instance = this.ramCache.get(positions);
        if (instance == null) this.ramCache.put(positions, instance = this._get(positions));
        return instance;
    }
    */

    /*
    public PolyominoTranslatedInstance _get(final SortedSet<IPosition> positions) {
        final Polyominos polyominos = Polyominos.getInstance();
        final String rendering = PolyominoRenderer.render(positions);
        final Polyomino type = polyominos.getType(rendering);
        final PolyominoTranslatedInstance instance = polyominos.computeTranslatedInstance(positions, polyominos.getInstance(rendering), type);
        return instance;
    }
    */

    /*
    public PolyominoTranslatedInstance _get(final SortedSet<IPosition> positions) {
        final String query = "{'positions':" + GSON.toJson(positions) + "}";
        PolyominoTranslatedInstance instance = this.collection.findOne(query).as(PolyominoTranslatedInstance.class);
        if (instance == null) {
            final Polyominos polyominos = Polyominos.getInstance();
            final String rendering = PolyominoRenderer.render(positions);
            final Polyomino type = polyominos.getType(rendering);
            //final PolyominoTranslatedInstance 
            instance = polyominos.computeTranslatedInstance(positions, polyominos.getInstance(rendering), type);
            System.out.println(instance);
            this.collection.save(instance);
        }
        return instance;
    }
    */

    /*
    public static void main(final String[] args) {
        final PolyominoTranslatedInstances instances = new PolyominoTranslatedInstances();
        final Iterable<IPosition> positions = Polyomino._8.positions();
        final PolyominoTranslatedInstance instance = instances.get((SortedSet<IPosition>) positions);
        System.out.println(instance.positions());
        System.out.println(instance.shadows());
        System.out.println(instance.lights());
        System.out.println(instance.type());

        final Iterable<PolyominoInstance> iterable = Polyomino._0.get();

        System.out.println(iterable);

        for (final PolyominoInstance polyominoInstance : iterable) {
            System.out.println(polyominoInstance.getClass());
        }
    }
    */

    // TODO extract caching routine
    // TODO do not save instances out of the board
    /*
    public static void main(final String[] args) {

        final int ROWS = 20;
        final int COLUMNS = 20;

        final PolyominoTranslatedInstances entities = new PolyominoTranslatedInstances();

        int n = 0;
        for (final Polyomino polyomino : Polyomino.set()) {
            System.out.println();
            System.out.println(polyomino);
            try {
                Thread.sleep(20);
            }
            catch (final InterruptedException e) {}
            for (int i = 0; i < ROWS; ++i) {
                try {
                    Thread.sleep(10);
                }
                catch (final InterruptedException e) {}
                System.out.println(i);
                for (int j = 0; j < COLUMNS; ++j) {
                    try {
                        Thread.sleep(5);
                    }
                    catch (final InterruptedException e) {}
                    final Iterable<PolyominoInstance> instances = polyomino.get();
                    for (final PolyominoInstance pieceInstance : instances) {
                        final SortedSet<IPosition> positions = pieceInstance.apply(Positions.Position(i, j));
                        if (positions.iterator().hasNext()) {
                            boolean ok = true;
                            for (final IPosition iPosition : positions) {
                                final boolean isValid = iPosition.row() > -1
                                        && iPosition.column() > -1
                                        && iPosition.row() < ROWS
                                        && iPosition.column() < COLUMNS;
                                if (!isValid) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                ++n;
                                entities._get(positions);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(n);
    }
    */
}