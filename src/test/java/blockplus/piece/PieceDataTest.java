
package blockplus.piece;

import static blockplus.piece.PieceData.PieceData;
import static blockplus.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import blockplus.position.NullPosition;

import com.google.common.collect.Sets;

public class PieceDataTest {

    @Test
    public void testGet() {
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
        assertEquals(Position(1, 1), PieceData(17).referential());
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

}