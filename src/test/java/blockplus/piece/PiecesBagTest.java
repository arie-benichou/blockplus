/*
 * Copyright 2012 Arie Benichou
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

package blockplus.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PiecesBagTest {

    @Test
    public void testIsEmpty() {
        assertTrue(PiecesBag.from().isEmpty());
        assertFalse(PiecesBag.from(Pieces.get(1)).isEmpty());
    }

    @Test
    public void testIterator() {

        {
            final PiecesBag bagOfPiece = PiecesBag.from();
            PieceInterface lastPiece = null;
            for (final PieceInterface piece : bagOfPiece)
                lastPiece = piece;
            assertTrue(lastPiece == null);
        }

        {
            final PiecesBag bagOfPiece = PiecesBag.from(Pieces.get(1));
            final List<PieceInterface> expectedPiece = new ArrayList<PieceInterface>();
            expectedPiece.add(Pieces.get(1));
            final List<PieceInterface> actualPiece = Lists.newArrayList(bagOfPiece);
            assertEquals(expectedPiece, actualPiece);
        }

        {
            final PiecesBag bagOfPiece = PiecesBag.from(Pieces.get(1), Pieces.get(1));
            final List<PieceInterface> expectedPiece = new ArrayList<PieceInterface>();
            expectedPiece.add(Pieces.get(1));
            expectedPiece.add(Pieces.get(1));
            final List<PieceInterface> actualPiece = Lists.newArrayList(bagOfPiece);
            assertEquals(expectedPiece, actualPiece);
        }

        {
            final PiecesBag bagOfPiece = PiecesBag.from(Pieces.get(1), Pieces.get(1), Pieces.get(2));
            final Set<PieceInterface> expectedPiece = Sets.newHashSet(Pieces.get(1), Pieces.get(1), Pieces.get(2));
            final Set<PieceInterface> actualPiece = Sets.newHashSet(bagOfPiece);
            assertEquals(expectedPiece, actualPiece);
        }

        {
            final PiecesBag bagOfPiece = PiecesBag.from(Pieces.get(1), Pieces.get(1), Pieces.get(2), Pieces.get(3));
            final Set<PieceInterface> expectedPiece = Sets.newHashSet(Pieces.get(1), Pieces.get(1), Pieces.get(2), Pieces.get(3));
            final Set<PieceInterface> actualPiece = Sets.newHashSet(bagOfPiece);
            assertEquals(expectedPiece, actualPiece);
        }

    }

    @Test
    public void testRemove() {

        {
            final PiecesBag bagOfPiece = PiecesBag.from(Pieces.get(1));
            assertFalse(bagOfPiece.isEmpty());
            final PiecesBag newBagOfPiece = bagOfPiece.remove(Pieces.get(1));
            assertTrue(newBagOfPiece.isEmpty());
        }

        {
            final PiecesBag bagOfPiece = PiecesBag.from(Pieces.get(1), Pieces.get(1));
            assertFalse(bagOfPiece.isEmpty());
            PiecesBag newBagOfPiece = bagOfPiece;
            newBagOfPiece = newBagOfPiece.remove(Pieces.get(1));
            assertFalse(bagOfPiece.isEmpty());
            newBagOfPiece = newBagOfPiece.remove(Pieces.get(1));
            assertTrue(newBagOfPiece.isEmpty());
        }
    }

}