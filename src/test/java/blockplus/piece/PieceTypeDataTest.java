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

import static blockplus.piece.PieceTypeData.PieceData;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.common.collect.Sets;
import components.position.NullPosition;

public class PieceTypeDataTest {

    @Test
    public void testPieceData() {
        assertSame(PieceTypeData.ENTRY0, PieceData(0));
        assertSame(PieceTypeData.ENTRY1, PieceData(1));
        assertSame(PieceTypeData.ENTRY2, PieceData(2));
        assertSame(PieceTypeData.ENTRY3, PieceData(3));
        assertSame(PieceTypeData.ENTRY4, PieceData(4));
        assertSame(PieceTypeData.ENTRY5, PieceData(5));
        assertSame(PieceTypeData.ENTRY6, PieceData(6));
        assertSame(PieceTypeData.ENTRY7, PieceData(7));
        assertSame(PieceTypeData.ENTRY8, PieceData(8));
        assertSame(PieceTypeData.ENTRY9, PieceData(9));
        assertSame(PieceTypeData.ENTRY10, PieceData(10));
        assertSame(PieceTypeData.ENTRY11, PieceData(11));
        assertSame(PieceTypeData.ENTRY12, PieceData(12));
        assertSame(PieceTypeData.ENTRY13, PieceData(13));
        assertSame(PieceTypeData.ENTRY14, PieceData(14));
        assertSame(PieceTypeData.ENTRY15, PieceData(15));
        assertSame(PieceTypeData.ENTRY16, PieceData(16));
        assertSame(PieceTypeData.ENTRY17, PieceData(17));
        assertSame(PieceTypeData.ENTRY18, PieceData(18));
        assertSame(PieceTypeData.ENTRY19, PieceData(19));
        assertSame(PieceTypeData.ENTRY20, PieceData(20));
        assertSame(PieceTypeData.ENTRY21, PieceData(21));
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