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

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

// TODO à compléter
public class RefereeTest {

    private Referee referee;
    private Board<Color> board;

    @Before
    public void setUp() throws Exception {

        final String[][] data = {
                { "Ø.........Ø" },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { ".....?....." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "Ø.........o" }
        };

        this.board = BoardBuilder.parse(data);
        this.referee = new Referee();

    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
        this.referee = null;
    }

    @Test
    public void testGetLegalMovesWithEmptyBagOfPieces() {
        final Player player = new Player(Color.WHITE, PiecesBag.from());
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        Assert.assertTrue(legalMoves.isEmpty());
    }

    @Test
    public void testGetLegalMovesWithBagOfPiecesHavingOnePiece() {
        final Player player = new Player(Color.WHITE, PiecesBag.from(Pieces.get(1)));
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        Assert.assertTrue(!legalMoves.isEmpty());
        // TODO à compléter
    }

}