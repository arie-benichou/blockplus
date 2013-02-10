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

import static blockplus.piece.PieceData.PieceData;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.common.collect.Sets;
import components.position.NullPosition;

public class PieceDataTest {

    @Test
    public void testPieceData() {
        assertSame(PieceData.ENTRY0, PieceData(0));
        assertSame(PieceData.ENTRY1, PieceData(1));
        assertSame(PieceData.ENTRY2, PieceData(2));
        assertSame(PieceData.ENTRY3, PieceData(3));
        assertSame(PieceData.ENTRY4, PieceData(4));
        assertSame(PieceData.ENTRY5, PieceData(5));
        assertSame(PieceData.ENTRY6, PieceData(6));
        assertSame(PieceData.ENTRY7, PieceData(7));
        assertSame(PieceData.ENTRY8, PieceData(8));
        assertSame(PieceData.ENTRY9, PieceData(9));
        assertSame(PieceData.ENTRY10, PieceData(10));
        assertSame(PieceData.ENTRY11, PieceData(11));
        assertSame(PieceData.ENTRY12, PieceData(12));
        assertSame(PieceData.ENTRY13, PieceData(13));
        assertSame(PieceData.ENTRY14, PieceData(14));
        assertSame(PieceData.ENTRY15, PieceData(15));
        assertSame(PieceData.ENTRY16, PieceData(16));
        assertSame(PieceData.ENTRY17, PieceData(17));
        assertSame(PieceData.ENTRY18, PieceData(18));
        assertSame(PieceData.ENTRY19, PieceData(19));
        assertSame(PieceData.ENTRY20, PieceData(20));
        assertSame(PieceData.ENTRY21, PieceData(21));
    }

    @Test
    public void testId() {
        assertSame(PieceData.ENTRY0.ordinal(), PieceData(0).id());
        assertSame(PieceData.ENTRY1.ordinal(), PieceData(1).id());
        assertSame(PieceData.ENTRY2.ordinal(), PieceData(2).id());
        assertSame(PieceData.ENTRY3.ordinal(), PieceData(3).id());
        assertSame(PieceData.ENTRY4.ordinal(), PieceData(4).id());
        assertSame(PieceData.ENTRY5.ordinal(), PieceData(5).id());
        assertSame(PieceData.ENTRY6.ordinal(), PieceData(6).id());
        assertSame(PieceData.ENTRY7.ordinal(), PieceData(7).id());
        assertSame(PieceData.ENTRY8.ordinal(), PieceData(8).id());
        assertSame(PieceData.ENTRY9.ordinal(), PieceData(9).id());
        assertSame(PieceData.ENTRY10.ordinal(), PieceData(10).id());
        assertSame(PieceData.ENTRY11.ordinal(), PieceData(11).id());
        assertSame(PieceData.ENTRY12.ordinal(), PieceData(12).id());
        assertSame(PieceData.ENTRY13.ordinal(), PieceData(13).id());
        assertSame(PieceData.ENTRY14.ordinal(), PieceData(14).id());
        assertSame(PieceData.ENTRY15.ordinal(), PieceData(15).id());
        assertSame(PieceData.ENTRY16.ordinal(), PieceData(16).id());
        assertSame(PieceData.ENTRY17.ordinal(), PieceData(17).id());
        assertSame(PieceData.ENTRY18.ordinal(), PieceData(18).id());
        assertSame(PieceData.ENTRY19.ordinal(), PieceData(19).id());
        assertSame(PieceData.ENTRY20.ordinal(), PieceData(20).id());
        assertSame(PieceData.ENTRY21.ordinal(), PieceData(21).id());
    }

    @Test
    public void testSize() {
        assertEquals(0, PieceData(0).size());
        assertEquals(1, PieceData(1).size());
        assertEquals(2, PieceData(2).size());
        assertEquals(3, PieceData(3).size());
        assertEquals(3, PieceData(4).size());
        assertEquals(4, PieceData(5).size());
        assertEquals(4, PieceData(6).size());
        assertEquals(4, PieceData(7).size());
        assertEquals(4, PieceData(8).size());
        assertEquals(4, PieceData(9).size());
        assertEquals(5, PieceData(10).size());
        assertEquals(5, PieceData(11).size());
        assertEquals(5, PieceData(12).size());
        assertEquals(5, PieceData(13).size());
        assertEquals(5, PieceData(14).size());
        assertEquals(5, PieceData(15).size());
        assertEquals(5, PieceData(16).size());
        assertEquals(5, PieceData(17).size());
        assertEquals(5, PieceData(18).size());
        assertEquals(5, PieceData(19).size());
        assertEquals(5, PieceData(20).size());
        assertEquals(5, PieceData(21).size());
    }

    @Test
    public void testReferential() {
        assertEquals(NullPosition.getInstance(), PieceData(0).referential());
        assertEquals(Position(0, 0), PieceData(1).referential());
        assertEquals(Position(0, 0), PieceData(2).referential());
        assertEquals(Position(1, 0), PieceData(3).referential());
        assertEquals(Position(0, 0), PieceData(4).referential());
        assertEquals(Position(1, 0), PieceData(5).referential());
        assertEquals(Position(1, 0), PieceData(6).referential());
        assertEquals(Position(1, 0), PieceData(7).referential());
        assertEquals(Position(0, 0), PieceData(8).referential());
        assertEquals(Position(1, 1), PieceData(9).referential());
        assertEquals(Position(1, 0), PieceData(10).referential());
        assertEquals(Position(2, 0), PieceData(11).referential());
        assertEquals(Position(1, 1), PieceData(12).referential());
        assertEquals(Position(1, 1), PieceData(13).referential());
        assertEquals(Position(1, 1), PieceData(14).referential());
        assertEquals(Position(1, 0), PieceData(15).referential());
        assertEquals(Position(1, 1), PieceData(16).referential());
        assertEquals(Position(0, 0), PieceData(17).referential());
        assertEquals(Position(1, 1), PieceData(18).referential());
        assertEquals(Position(1, 1), PieceData(19).referential());
        assertEquals(Position(1, 1), PieceData(20).referential());
        assertEquals(Position(1, 1), PieceData(21).referential());
    }

    @Test
    public void testPositions() {

        assertEquals(Sets.newHashSet(), PieceData(0).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0)),
                PieceData(1).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0)),
                PieceData(2).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(2, 0)),
                PieceData(3).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 0)),
                PieceData(4).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)),
                PieceData(5).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 0), Position(2, 0)),
                PieceData(6).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 0)),
                PieceData(7).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1)),
                PieceData(8).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2)),
                PieceData(9).positions());

        assertEquals(Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 0), Position(2, 0), Position(3, 0)),
                PieceData(10).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0), Position(4, 0)),
                PieceData(11).positions());

        assertEquals(Sets.newHashSet(
                Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2), Position(1, 3)),
                PieceData(12).positions());

        assertEquals(Sets.newHashSet(
                Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 1), Position(1, 2)),
                PieceData(13).positions());

        assertEquals(Sets.newHashSet(
                Position(0, 0), Position(0, 1), Position(1, 1), Position(2, 0), Position(2, 1)),
                PieceData(14).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(1, 1), Position(2, 0), Position(3, 0)),
                PieceData(15).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(1, 1), Position(1, 2), Position(2, 0)),
                PieceData(16).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(0, 1), Position(0, 2), Position(1, 0), Position(2, 0)),
                PieceData(17).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2), Position(2, 2)),
                PieceData(18).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(1, 1), Position(1, 2), Position(2, 2)),
                PieceData(19).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 0), Position(1, 0), Position(1, 1), Position(1, 2), Position(2, 1)),
                PieceData(20).positions());

        assertEquals(
                Sets.newHashSet(Position(0, 1), Position(1, 0), Position(1, 1), Position(1, 2), Position(2, 1)),
                PieceData(21).positions());
    }

    @Test
    public void testRadius() {

        assertEquals(-1, PieceData(0).radius());

        assertEquals(0, PieceData(1).radius());

        assertEquals(1, PieceData(2).radius());
        assertEquals(1, PieceData(3).radius());
        assertEquals(1, PieceData(4).radius());

        assertEquals(2, PieceData(5).radius());

        assertEquals(1, PieceData(6).radius());
        assertEquals(1, PieceData(7).radius());
        assertEquals(1, PieceData(8).radius());
        assertEquals(1, PieceData(9).radius());

        assertEquals(2, PieceData(10).radius());
        assertEquals(2, PieceData(11).radius());
        assertEquals(2, PieceData(12).radius());

        assertEquals(1, PieceData(13).radius());
        assertEquals(1, PieceData(14).radius());

        assertEquals(2, PieceData(15).radius());

        assertEquals(1, PieceData(16).radius());

        assertEquals(2, PieceData(17).radius());

        assertEquals(1, PieceData(18).radius());
        assertEquals(1, PieceData(19).radius());
        assertEquals(1, PieceData(20).radius());
        assertEquals(1, PieceData(21).radius());
    }

}