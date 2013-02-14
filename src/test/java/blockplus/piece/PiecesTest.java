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

package blockplus.piece;

import static blockplus.piece.PieceType.PIECE1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

// FIXME add tests for toString, hashCode, equals
public class PiecesTest {

    private final static Pieces PIECESBAG_OF_1 = new Pieces.Builder().add(PIECE1).build();

    @Test
    public void testIsEmpty() {
        assertTrue(Pieces.EMPTY.isEmpty());
        assertFalse(PIECESBAG_OF_1.isEmpty());
    }

    @Test
    public void testIterator() {
        {
            final Pieces bagOfPiece = Pieces.EMPTY;
            assertFalse(bagOfPiece.iterator().hasNext());
        }
        {
            final Map<PieceType, Integer> expected = Maps.newHashMap();
            expected.put(PIECE1, 1);
            final Builder<PieceType, Integer> builder = new ImmutableMap.Builder<PieceType, Integer>();
            for (final Entry<PieceType, Integer> entry : PIECESBAG_OF_1)
                builder.put(entry);
            assertEquals(expected, builder.build());
        }
    }

    @Test
    public void testWithdraw() {
        final Pieces bagOfPiece = PIECESBAG_OF_1;
        assertFalse(bagOfPiece.isEmpty());
        final Pieces newBagOfPiece = bagOfPiece.withdraw(PIECE1);
        assertTrue(newBagOfPiece.isEmpty());
    }

    @Ignore
    public void testToString() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testHashCode() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

}