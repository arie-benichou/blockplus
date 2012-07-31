
package blockplus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import blockplus.piece.PieceTemplateInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PiecesBagTest {

    @Test
    public void testIsEmpty() {
        final Map<PieceTemplateInterface, Integer> instancesOfPieces = Maps.newLinkedHashMap();

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertTrue(bagOfPieces.isEmpty());
        }

        instancesOfPieces.put(Pieces.get(1).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertFalse(bagOfPieces.isEmpty());
        }
    }

    @Test
    public void testIterator() {

        final Map<PieceTemplateInterface, Integer> instancesOfPieces = Maps.newHashMap();

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            PieceTemplateInterface lastPiece = null;
            for (final PieceTemplateInterface piece : bagOfPieces)
                lastPiece = piece;
            assertTrue(lastPiece == null);
        }

        instancesOfPieces.put(Pieces.get(1).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceTemplateInterface> expectedPieces = new ArrayList<PieceTemplateInterface>();
            expectedPieces.add(Pieces.get(1).get());
            final List<PieceTemplateInterface> actualPieces = Lists.newArrayList();
            for (final PieceTemplateInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        instancesOfPieces.put(Pieces.get(1).get(), 2);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceTemplateInterface> expectedPieces = new ArrayList<PieceTemplateInterface>();
            expectedPieces.add(Pieces.get(1).get());
            expectedPieces.add(Pieces.get(1).get());
            final List<PieceTemplateInterface> actualPieces = Lists.newArrayList();
            for (final PieceTemplateInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        instancesOfPieces.put(Pieces.get(2).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceTemplateInterface> expectedPieces = new ArrayList<PieceTemplateInterface>();
            expectedPieces.add(Pieces.get(1).get());
            expectedPieces.add(Pieces.get(1).get());
            expectedPieces.add(Pieces.get(2).get());
            final List<PieceTemplateInterface> actualPieces = Lists.newArrayList();
            for (final PieceTemplateInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        instancesOfPieces.put(Pieces.get(3).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceTemplateInterface> expectedPieces = new ArrayList<PieceTemplateInterface>();
            expectedPieces.add(Pieces.get(1).get());
            expectedPieces.add(Pieces.get(1).get());
            expectedPieces.add(Pieces.get(2).get());
            expectedPieces.add(Pieces.get(3).get());
            final List<PieceTemplateInterface> actualPieces = Lists.newArrayList();
            for (final PieceTemplateInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

    }

    @Test
    public void testRemove() {
        final Map<PieceTemplateInterface, Integer> instancesOfPieces = Maps.newLinkedHashMap();

        instancesOfPieces.put(Pieces.get(1).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertFalse(bagOfPieces.isEmpty());
            final PiecesBag newBagOfPieces = bagOfPieces.remove(Pieces.get(1).get());
            assertTrue(newBagOfPieces.isEmpty());
        }

        instancesOfPieces.put(Pieces.get(1).get(), 2);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertFalse(bagOfPieces.isEmpty());
            PiecesBag newBagOfPieces = bagOfPieces;
            newBagOfPieces = newBagOfPieces.remove(Pieces.get(1).get());
            assertFalse(bagOfPieces.isEmpty());
            newBagOfPieces = newBagOfPieces.remove(Pieces.get(1).get());
            assertTrue(newBagOfPieces.isEmpty());
        }
    }

}