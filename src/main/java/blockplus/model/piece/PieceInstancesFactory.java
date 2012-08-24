
package blockplus.model.piece;

import java.util.Map;

import com.google.common.collect.Maps;

public class PieceInstancesFactory {

    private final static Map<Integer, PieceInstances> PIECE_INSTANCES_BY_PIECE = Maps.newConcurrentMap();

    public PieceInstances get(final int piece) {
        PieceInstances instance = PIECE_INSTANCES_BY_PIECE.get(piece);
        if (instance == null) PIECE_INSTANCES_BY_PIECE.put(piece, instance = new PieceInstances(piece));
        else System.out.println("\ncache hit !\n");
        return instance;
    }

}