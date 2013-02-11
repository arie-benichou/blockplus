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

package blockplus.arbitration;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import interfaces.move.MoveInterface;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import blockplus.board.Board;
import blockplus.board.BoardLayer;
import blockplus.board.State;
import blockplus.context.Context;
import blockplus.context.ContextBuilder;
import blockplus.move.Move;
import blockplus.move.Moves;
import blockplus.piece.PieceInstances;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;
import blockplus.player.Players;
import blockplus.player.Players.Builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

public class RefereeTest {

    @Test
    public void bugPiece17WhenReferentialIsOnAnotherColor() {

        // TODO ContextParser
        final ContextBuilder contextBuilder = new ContextBuilder();
        final PiecesBag blueBag = PiecesBag.from(Pieces.PIECE0, Pieces.PIECE3, Pieces.PIECE12, Pieces.PIECE17);
        final PiecesBag greenBag = PiecesBag.from(Pieces.PIECE0, Pieces.PIECE2, Pieces.PIECE16);
        final PiecesBag bagOfPieces = PiecesBag.from(Pieces.PIECE0, Pieces.PIECE1);
        final Builder playersBuilder = new Players.Builder();
        playersBuilder.add(new Player(Blue, blueBag));
        playersBuilder.add(new Player(Green, greenBag));
        playersBuilder.add(new Player(Yellow, bagOfPieces));
        playersBuilder.add(new Player(Red, bagOfPieces));
        final Players players = playersBuilder.build();

        contextBuilder.setPlayers(players);
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
        contextBuilder.setBoard(board);
        Context context = contextBuilder.build();

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(3));
            final PieceInterface piece = pieceInstances.get(1).translateTo(Position(0, 1));
            final Move move = Moves.getMove(Blue, piece);
            context = context.apply(move);
        }

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(12));
            final PieceInterface piece = pieceInstances.get(3).reflectAlongVerticalAxis().translateTo(Position(2, 4));
            final Move move = Moves.getMove(Blue, piece);
            context = context.apply(move);
        }

        context = context.forward();

        {
            final List<MoveInterface> options = context.options();
            final MoveInterface move = options.iterator().next();
            context = context.apply(move);
        }

        context = context.forward();

        {
            final List<MoveInterface> options = context.options();
            final MoveInterface move = options.iterator().next();
            context = context.apply(move);
        }

        context = context.forward();

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(2));
            final PieceInterface piece = pieceInstances.get(1).translateTo(Position(7, 0));
            final Move move = Moves.getMove(Green, piece);
            context = context.apply(move);
        }

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(new PieceInstances(16));
            final PieceInterface piece = pieceInstances.get(3).translateTo(Position(5, 2));
            final Move move = Moves.getMove(Green, piece);
            context = context.apply(move);
        }

        context = context.forward();

        final Board finalBoard = context.getBoard();
        final BoardLayer layer = finalBoard.getLayer(Blue);
        final Map<PositionInterface, State> lights = layer.getLights();
        assertTrue(!lights.isEmpty());

        final List<MoveInterface> legalMoves = context.options();
        assertEquals(1, legalMoves.size());

    }
}