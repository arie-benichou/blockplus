
package blockplus.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class PiecesBagTest {

    @Test
    public void testIsEmpty() {
        assertTrue(PiecesBag.from().isEmpty());
        assertFalse(PiecesBag.from(PieceTemplate.get(1)).isEmpty());
    }

    @Test
    public void testIterator() {

        {
            final PiecesBag bagOfPieces = PiecesBag.from();
            PieceTemplate lastPiece = null;
            for (final PieceTemplate piece : bagOfPieces)
                lastPiece = piece;
            assertTrue(lastPiece == null);
        }

        {
            final PiecesBag bagOfPieces = PiecesBag.from(PieceTemplate.get(1));
            final List<PieceTemplate> expectedPieces = new ArrayList<PieceTemplate>();
            expectedPieces.add(PieceTemplate.get(1));
            final List<PieceTemplate> actualPieces = Lists.newArrayList(bagOfPieces);
            assertEquals(expectedPieces, actualPieces);
        }

        {
            final PiecesBag bagOfPieces = PiecesBag.from(PieceTemplate.get(1), PieceTemplate.get(1));
            final List<PieceTemplate> expectedPieces = new ArrayList<PieceTemplate>();
            expectedPieces.add(PieceTemplate.get(1));
            expectedPieces.add(PieceTemplate.get(1));
            final List<PieceTemplate> actualPieces = Lists.newArrayList();
            for (final PieceTemplate piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        {
            final PiecesBag bagOfPieces = PiecesBag.from(PieceTemplate.get(1), PieceTemplate.get(1), PieceTemplate.get(2));
            final List<PieceTemplate> expectedPieces = new ArrayList<PieceTemplate>();
            expectedPieces.add(PieceTemplate.get(1));
            expectedPieces.add(PieceTemplate.get(1));
            expectedPieces.add(PieceTemplate.get(2));
            final List<PieceTemplate> actualPieces = Lists.newArrayList();
            for (final PieceTemplate piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

        {
            final PiecesBag bagOfPieces = PiecesBag.from(PieceTemplate.get(1), PieceTemplate.get(1), PieceTemplate.get(2), PieceTemplate.get(3));
            final List<PieceTemplate> expectedPieces = new ArrayList<PieceTemplate>();
            expectedPieces.add(PieceTemplate.get(1));
            expectedPieces.add(PieceTemplate.get(1));
            expectedPieces.add(PieceTemplate.get(2));
            expectedPieces.add(PieceTemplate.get(3));
            final List<PieceTemplate> actualPieces = Lists.newArrayList();
            for (final PieceTemplate piece : bagOfPieces)
                actualPieces.add(piece);
            assertEquals(expectedPieces, actualPieces);
        }

    }

    @Test
    public void testRemove() {

        {
            final PiecesBag bagOfPieces = PiecesBag.from(PieceTemplate.get(1));
            assertFalse(bagOfPieces.isEmpty());
            final PiecesBag newBagOfPieces = bagOfPieces.remove(PieceTemplate.get(1));
            assertTrue(newBagOfPieces.isEmpty());
        }

        {
            final PiecesBag bagOfPieces = PiecesBag.from(PieceTemplate.get(1), PieceTemplate.get(1));
            assertFalse(bagOfPieces.isEmpty());
            PiecesBag newBagOfPieces = bagOfPieces;
            newBagOfPieces = newBagOfPieces.remove(PieceTemplate.get(1));
            assertFalse(bagOfPieces.isEmpty());
            newBagOfPieces = newBagOfPieces.remove(PieceTemplate.get(1));
            assertTrue(newBagOfPieces.isEmpty());
        }
    }

}