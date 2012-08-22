
package components.neighbourhood;

import static components.position.Position.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import components.position.PositionInterface;

public class NeighbourhoodTest {

    private PositionInterface referential;

    @Before
    public void setUp() throws Exception {
        this.referential = Position(3, 3);
        /*
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
        final Map<Character, State> symbolByCharacter = ImmutableMap.of('0', None, '1', Self, '2', Light, '3', Shadow, '?', Other);
        final BoardParser<State> boardParser = BoardParser.from(symbolByCharacter, State.None, State.Other);
        final BoardInterface<State> board = boardParser.parse(data);
        final Map<State, Character> characterBySymbol = ImmutableMap.of(None, '0', Self, '1', Light, '2', Shadow, '3', Other, '?');
        final BoardConsoleView boardConsoleView = new BoardConsoleView(characterBySymbol);
        boardConsoleView.render(board);
        */
    }

    @After
    public void tearDown() throws Exception {
        this.referential = null;
    }

    @Test
    public void testGetNeighboursPositions() {
        {
            final int radius = -1;
            final List<PositionInterface> expected = Lists.newArrayList();
            final List<PositionInterface> neighboursPositions = Neighbourhood.getNeighboursPositions(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 0;
            final List<PositionInterface> expected = Lists.newArrayList(this.referential); // TODO ? à revoir...
            final List<PositionInterface> neighboursPositions = Neighbourhood.getNeighboursPositions(this.referential, radius);
            assertEquals(expected, neighboursPositions);
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
            final List<PositionInterface> neighboursPositions = Neighbourhood.getNeighboursPositions(this.referential, radius);
            assertEquals(expected, neighboursPositions);
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
            final List<PositionInterface> neighboursPositions = Neighbourhood.getNeighboursPositions(this.referential, radius);
            assertEquals(expected, neighboursPositions);
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
            final List<PositionInterface> neighboursPositions = Neighbourhood.getNeighboursPositions(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }
    }

}