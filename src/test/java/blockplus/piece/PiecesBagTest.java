
package blockplus.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import blockplus.piece.PieceInterface;
import blockplus.piece.PieceTemplate;
import blockplus.piece.PiecesBag;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PiecesBagTest {

    @Test
    public void testIsEmpty() {
        final Map<PieceInterface, Integer> instancesOfPieces = Maps.newLinkedHashMap();

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertTrue(bagOfPieces.isEmpty());
        }

        instancesOfPieces.put(PieceTemplate.get(1).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertFalse(bagOfPieces.isEmpty());
        }
    }

    @Test
    public void testIterator() {

        final Map<PieceInterface, Integer> instancesOfPieces = Maps.newHashMap();

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            PieceInterface lastPiece = null;
            for (final PieceInterface piece : bagOfPieces)
                lastPiece = piece;
            assertTrue(lastPiece == null);
        }

        instancesOfPieces.put(PieceTemplate.get(1).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceInterface> expectedPieces = new ArrayList<PieceInterface>();
            expectedPieces.add(PieceTemplate.get(1).get());
            final List<PieceInterface> actualPieces = Lists.newArrayList();
            for (final PieceInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        instancesOfPieces.put(PieceTemplate.get(1).get(), 2);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceInterface> expectedPieces = new ArrayList<PieceInterface>();
            expectedPieces.add(PieceTemplate.get(1).get());
            expectedPieces.add(PieceTemplate.get(1).get());
            final List<PieceInterface> actualPieces = Lists.newArrayList();
            for (final PieceInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        instancesOfPieces.put(PieceTemplate.get(2).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceInterface> expectedPieces = new ArrayList<PieceInterface>();
            expectedPieces.add(PieceTemplate.get(1).get());
            expectedPieces.add(PieceTemplate.get(1).get());
            expectedPieces.add(PieceTemplate.get(2).get());
            final List<PieceInterface> actualPieces = Lists.newArrayList();
            for (final PieceInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        instancesOfPieces.put(PieceTemplate.get(3).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            final List<PieceInterface> expectedPieces = new ArrayList<PieceInterface>();
            expectedPieces.add(PieceTemplate.get(1).get());
            expectedPieces.add(PieceTemplate.get(1).get());
            expectedPieces.add(PieceTemplate.get(2).get());
            expectedPieces.add(PieceTemplate.get(3).get());
            final List<PieceInterface> actualPieces = Lists.newArrayList();
            for (final PieceInterface piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

    }

    @Test
    public void testRemove() {
        final Map<PieceInterface, Integer> instancesOfPieces = Maps.newLinkedHashMap();

        instancesOfPieces.put(PieceTemplate.get(1).get(), 1);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertFalse(bagOfPieces.isEmpty());
            final PiecesBag newBagOfPieces = bagOfPieces.remove(PieceTemplate.get(1).get());
            assertTrue(newBagOfPieces.isEmpty());
        }

        instancesOfPieces.put(PieceTemplate.get(1).get(), 2);

        {
            final PiecesBag bagOfPieces = new PiecesBag(instancesOfPieces);
            assertFalse(bagOfPieces.isEmpty());
            PiecesBag newBagOfPieces = bagOfPieces;
            newBagOfPieces = newBagOfPieces.remove(PieceTemplate.get(1).get());
            assertFalse(bagOfPieces.isEmpty());
            newBagOfPieces = newBagOfPieces.remove(PieceTemplate.get(1).get());
            assertTrue(newBagOfPieces.isEmpty());
        }
    }

}