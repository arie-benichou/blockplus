
package blockplus.model.piece;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public final class PieceInstancesFactory {

    private final static Map<Integer, PieceInstances> PIECE_INSTANCES_BY_PIECE = Maps.newConcurrentMap();
    private static long cacheHits;

    public static PieceInstances get(final int piece) {
        PieceInstances instance = PIECE_INSTANCES_BY_PIECE.get(piece);
        if (instance == null) PIECE_INSTANCES_BY_PIECE.put(piece, instance = new PieceInstances(piece));
        else ++cacheHits;
        return instance;
    }

    public static long cacheHits() {
        return cacheHits;
    }

    public static int size() {
        return PIECE_INSTANCES_BY_PIECE.size();
    }

    public static String asString() {
        return Objects.toStringHelper(PieceInstancesFactory.class.getCanonicalName())
                .add("size", size())
                .add("cacheHits", cacheHits())
                .toString();
    }

    private PieceInstancesFactory() {}

}