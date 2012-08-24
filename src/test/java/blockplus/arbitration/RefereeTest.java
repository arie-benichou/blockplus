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

package blockplus.arbitration;

import static blockplus.model.color.Colors.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.model.arbitration.Referee;
import blockplus.model.board.Board;
import blockplus.model.board.BoardParser;
import blockplus.model.move.Move;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;

// TODO ! tester avec un jeu à 0 cellules
// TODO ? tester avec un jeu à 1 cellules
// TODO ? tester avec un jeu à 2 cellules

// TODO ! tester avec un jeu à 0 pièces
// TODO ! tester avec un jeu à 1 pièce
// TODO ! tester avec un jeu à 2 pièces

// TODO ! tester avec un jeu à 0 joueur
// TODO ! tester avec un jeu à 1 joueur
// TODO ! tester avec un jeu à 2 joueur
public class RefereeTest {

    private Referee referee;
    private Board board;

    @Before
    public void setUp() throws Exception {
        final String[][] data = {
                { "b.........y" },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "g.........r" }
        };
        final BoardParser boardParser = new BoardParser();
        this.board = boardParser.parse(data);
        this.referee = new Referee();

    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
        this.referee = null;
    }

    @Test
    public void testGetLegalMovesWithEmptyBagOfPieces() {
        final Player player = new Player(Blue, PiecesBag.from(), null); // TODO Add construction alternative 
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        assertTrue(legalMoves.size() == 1);
        final Move move = legalMoves.iterator().next();
        assertTrue(move.isNull());
    }

    @Test
    public void testGetLegalMovesWithBagOfPiecesHavingOnePiece() {
        final Player player = new Player(Blue, PiecesBag.from(Pieces.get(1)), null); // TODO Add construction alternative
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        assertTrue(legalMoves.size() == 1);
        final Move actual = legalMoves.iterator().next();
        assertFalse(actual.isNull());

        /* 
         * FIXME
         * TODO !? aligner la factory de pièce sur la factory de couleurs
         *      !! passer la position du référential à l'objet Move
         *      
        final Move expected = new Move(Blue, Pieces.get(1));
        System.out.println(expected);
        System.out.println(actual);
        assertEquals(expected, actual);
        */

        // TODO ! à revoir: intoduire finalement PieceInstance(PieceId, PieceInstanceId, PieceInterface) ?
        final Move expected = new Move(Blue, Pieces.get(1).getInstances().getDistinctInstance(0));
        assertEquals(expected, actual);
    }

}