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

import static blockplus.model.color.Colors.Blue;
import static blockplus.model.color.Colors.Green;
import static blockplus.model.color.Colors.Red;
import static blockplus.model.color.Colors.Yellow;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.model.arbitration.Referee;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.BoardParser;
import blockplus.model.board.State;
import blockplus.model.game.BlockplusGameContext;
import blockplus.model.game.BlockplusGameContextBuilder;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceInstances;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;
import blockplus.model.strategy.FirstOptionStrategy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

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

    //@Test
    public void testGetLegalMovesWithEmptyBagOfPieces() {
        final Player player = new Player(Blue, PiecesBag.from(), null); // TODO Add construction alternative 
        final Set<Move> legalMoves = this.referee.getLegalMoves(this.board, player);
        assertTrue(legalMoves.size() == 1);
        final Move move = legalMoves.iterator().next();
        assertTrue(move.isNull());
    }

    //@Test
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

    @Test
    public void bugPiece17WhenReferentialIsOnAnotherColor() {

        // TODO pouvoir reconstruire le board à partir du gameState (analyse petit carré par petit carré)

        final int rows = 8, columns = 5;

        final BoardLayer blueLayer = new BoardLayer(rows, columns).apply(Position(0, 0), State.Light);
        final BoardLayer yellowLayer = new BoardLayer(rows, columns).apply(Position(0, columns - 1), State.Light);
        final BoardLayer redLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, columns - 1), State.Light);
        final BoardLayer greenLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, 0), State.Light);

        final Board board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                .set(Blue, blueLayer)
                .set(Yellow, yellowLayer)
                .set(Red, redLayer)
                .set(Green, greenLayer)
                .build();

        final PiecesBag blueBag = PiecesBag.from(Pieces.PIECE3, Pieces.PIECE12, Pieces.PIECE17);
        final PlayerInterface bluePlayer = new Player(Blue, blueBag, Yellow);

        final PiecesBag greenBag = PiecesBag.from(Pieces.PIECE2, Pieces.PIECE16);
        final PlayerInterface greenPlayer = new Player(Green, greenBag, Blue);

        final PiecesBag bagOfPieces = PiecesBag.from(Pieces.PIECE1);

        final PlayerInterface yellowPlayer = new Player(Yellow, bagOfPieces, Red, new FirstOptionStrategy());
        final PlayerInterface redPlayer = new Player(Red, bagOfPieces, Green, new FirstOptionStrategy());

        final BlockplusGameContextBuilder builder = new BlockplusGameContextBuilder();
        builder.setPlayers(bluePlayer, yellowPlayer, redPlayer, greenPlayer);
        builder.setBoard(board);

        BlockplusGameContext context = builder.build();

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(3));
            final PieceInterface piece = pieceInstances.get(1).translateTo(Position(0, 1));
            final Move move = new Move(Blue, piece);
            context = context.apply(move);
        }

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(12));
            final PieceInterface piece = pieceInstances.get(3).reflectAlongVerticalAxis().translateTo(Position(2, 4));
            final Move move = new Move(Blue, piece);
            context = context.apply(move);
        }

        context = context.next();

        {
            final List<Move> options = context.options();
            final Move move = options.iterator().next();
            context = context.apply(move);
        }

        context = context.next();

        {
            final List<Move> options = context.options();
            final Move move = options.iterator().next();
            context = context.apply(move);
        }

        context = context.next();

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(2));
            final PieceInterface piece = pieceInstances.get(1).translateTo(Position(7, 0));
            final Move move = new Move(Green, piece);
            context = context.apply(move);
        }

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(16));
            final PieceInterface piece = pieceInstances.get(3).translateTo(Position(5, 2));
            final Move move = new Move(Green, piece);
            context = context.apply(move);
        }

        context = context.next();
        final PlayerInterface player = context.getPlayers().get(Blue);

        final Board finalBoard = context.getBoard();
        final BoardLayer layer = finalBoard.getLayer(Blue);
        final Map<PositionInterface, State> lights = layer.getLights();
        assertTrue(!lights.isEmpty());

        final Set<Move> legalMoves = context.getReferee().getLegalMoves(finalBoard, player);
        assertTrue(!legalMoves.isEmpty());

    }
}