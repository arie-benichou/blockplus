
package blockplus.board;

import static blockplus.board.BoardBuilder.parse;
import static blockplus.position.Position.Position;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.color.Color;
import blockplus.position.PositionInterface;

import com.google.common.collect.Lists;

public class BoardTest {

    private Board<Color> board;
    private PositionInterface referential;

    @Before
    public void setUp() throws Exception {
        final int[][] data = {
                //0  1  2  3  4  5  6 
                { 3, 3, 3, 3, 3, 3, 3 },// 0
                { 3, 2, 2, 2, 2, 2, 3 },// 1
                { 3, 2, 1, 1, 1, 2, 3 },// 2
                { 3, 2, 1, 0, 1, 2, 3 },// 3
                { 3, 2, 1, 1, 1, 2, 3 },// 4
                { 3, 2, 2, 2, 2, 2, 3 },// 5
                { 3, 3, 3, 3, 3, 3, 3 } // 6
        };
        this.board = parse(data);
        this.referential = Position(3, 3);
    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
        this.referential = null;
    }

    @Test
    public void testGetNeighboursPositions() {

        {
            final int radius = -1;
            final List<PositionInterface> expected = Lists.newArrayList();
            final List<PositionInterface> neighboursPositions = this.board.getNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 0;
            final List<PositionInterface> expected = Lists.newArrayList(this.referential); // TODO ? à revoir...
            final List<PositionInterface> neighboursPositions = this.board.getNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 1;
            final List<PositionInterface> expected = Lists.newArrayList(
                    Position(2, 2),
                    Position(2, 3),
                    Position(2, 4),
                    Position(3, 2),
                    Position(3, 4),
                    Position(4, 2),
                    Position(4, 3),
                    Position(4, 4)

                    );
            final List<PositionInterface> neighboursPositions = this.board.getNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 2;
            final List<PositionInterface> expected = Lists.newArrayList(
                    Position(1, 1),
                    Position(1, 2),
                    Position(1, 3),
                    Position(1, 4),
                    Position(1, 5),
                    Position(2, 1),
                    Position(2, 5),
                    Position(3, 1),
                    Position(3, 5),
                    Position(4, 1),
                    Position(4, 5),
                    Position(5, 1),
                    Position(5, 2),
                    Position(5, 3),
                    Position(5, 4),
                    Position(5, 5)
                    );
            final List<PositionInterface> neighboursPositions = this.board.getNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 3;
            final List<PositionInterface> expected = Lists.newArrayList(
                    Position(0, 0),
                    Position(0, 1),
                    Position(0, 2),
                    Position(0, 3),
                    Position(0, 4),
                    Position(0, 5),
                    Position(0, 6),
                    Position(1, 0),
                    Position(1, 6),
                    Position(2, 0),
                    Position(2, 6),
                    Position(3, 0),
                    Position(3, 6),
                    Position(4, 0),
                    Position(4, 6),
                    Position(5, 0),
                    Position(5, 6),
                    Position(6, 0),
                    Position(6, 1),
                    Position(6, 2),
                    Position(6, 3),
                    Position(6, 4),
                    Position(6, 5),
                    Position(6, 6)
                    );
            final List<PositionInterface> neighboursPositions = this.board.getNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }

    }

    @Test
    public void testGetAllNeighboursPositions() {
        {
            final int radius = -1;
            final List<PositionInterface> expected = Lists.newArrayList();
            final List<PositionInterface> neighboursPositions = this.board.getAllNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }
        {
            final int radius = 0;
            final List<PositionInterface> expected = Lists.newArrayList(this.referential); // TODO ? à revoir...
            final List<PositionInterface> neighboursPositions = this.board.getAllNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }
        {
            final int radius = 1;
            final List<PositionInterface> expected = Lists.newArrayList(

                    Position(2, 2),
                    Position(2, 3),
                    Position(2, 4),
                    Position(3, 2),
                    Position(3, 4),
                    Position(4, 2),
                    Position(4, 3),
                    Position(4, 4),

                    this.referential // TODO ? à revoir...

                    );
            final List<PositionInterface> neighboursPositions = this.board.getAllNeighboursPositions(this.referential, radius);
            System.out.println(neighboursPositions);
            Assert.assertEquals(expected, neighboursPositions);
        }
        {
            final int radius = 2;
            final List<PositionInterface> expected = Lists.newArrayList(

                    Position(1, 1),
                    Position(1, 2),
                    Position(1, 3),
                    Position(1, 4),
                    Position(1, 5),
                    Position(2, 1),
                    Position(2, 5),
                    Position(3, 1),
                    Position(3, 5),
                    Position(4, 1),
                    Position(4, 5),
                    Position(5, 1),
                    Position(5, 2),
                    Position(5, 3),
                    Position(5, 4),
                    Position(5, 5),

                    Position(2, 2),
                    Position(2, 3),
                    Position(2, 4),
                    Position(3, 2),
                    Position(3, 4),
                    Position(4, 2),
                    Position(4, 3),
                    Position(4, 4),

                    this.referential // TODO ? à revoir...

                    );
            final List<PositionInterface> neighboursPositions = this.board.getAllNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }
        {
            final int radius = 3;
            final List<PositionInterface> expected = Lists.newArrayList(
                    Position(0, 0),
                    Position(0, 1),
                    Position(0, 2),
                    Position(0, 3),
                    Position(0, 4),
                    Position(0, 5),
                    Position(0, 6),
                    Position(1, 0),
                    Position(1, 6),
                    Position(2, 0),
                    Position(2, 6),
                    Position(3, 0),
                    Position(3, 6),
                    Position(4, 0),
                    Position(4, 6),
                    Position(5, 0),
                    Position(5, 6),
                    Position(6, 0),
                    Position(6, 1),
                    Position(6, 2),
                    Position(6, 3),
                    Position(6, 4),
                    Position(6, 5),
                    Position(6, 6),

                    Position(1, 1),
                    Position(1, 2),
                    Position(1, 3),
                    Position(1, 4),
                    Position(1, 5),
                    Position(2, 1),
                    Position(2, 5),
                    Position(3, 1),
                    Position(3, 5),
                    Position(4, 1),
                    Position(4, 5),
                    Position(5, 1),
                    Position(5, 2),
                    Position(5, 3),
                    Position(5, 4),
                    Position(5, 5),

                    Position(2, 2),
                    Position(2, 3),
                    Position(2, 4),
                    Position(3, 2),
                    Position(3, 4),
                    Position(4, 2),
                    Position(4, 3),
                    Position(4, 4),

                    this.referential // TODO ? à revoir...

                    );
            final List<PositionInterface> neighboursPositions = this.board.getAllNeighboursPositions(this.referential, radius);
            Assert.assertEquals(expected, neighboursPositions);
        }
    }
}