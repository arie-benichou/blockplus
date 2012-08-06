
package blockplus.arbitration;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.board.BoardRenderer;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.piece.PieceTemplate;
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
        BoardRenderer.render(this.board);
        this.referee = new Referee();

    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
        this.referee = null;
    }

    @Test
    public void testGetLegalMovesWithEmptyBagOfPieces() {
        final Player player = new Player(Color.White, PiecesBag.from());
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        Assert.assertTrue(legalMoves.isEmpty());
    }

    @Test
    public void testGetLegalMovesWithBagOfPiecesHavingOnePiece() {
        final Player player = new Player(Color.White, PiecesBag.from(PieceTemplate.get(1)));
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        Assert.assertTrue(!legalMoves.isEmpty());
        // TODO à compléter
    }

}