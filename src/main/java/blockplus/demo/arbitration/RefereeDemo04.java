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
 * this program. If not, see <http:www.gnu.org/licenses/>.
 */

package blockplus.demo.arbitration;

import static blockplus.model.color.Colors.*;
import static components.position.Position.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import serialization.JSONSerializer;

import blockplus.model.arbitration.Referee;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.color.ColorInterface;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComponent;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;
import blockplus.view.View;
import blockplus.view.console.BoardView;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;

import components.board.BoardInterface;
import components.position.PositionInterface;

/**
 * Featuring set of playable positions for a player
 */
public final class RefereeDemo04 {

    public static void main(final String[] args) {

        final int rows = 9, columns = 9;

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

        System.out.println("-----------------------------8<-----------------------------");

        final BoardView boardView = View.as(BoardView.class).show(board).up();

        System.out.println("-----------------------------8<-----------------------------");

        {
            final Referee blokusReferee = new Referee();
            final PiecesBag bagOfPieces = PiecesBag.from(Pieces.set());
            final PlayerInterface player = new Player(Red, bagOfPieces, Green);

            boardView.apply(board);

            final Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            final List<Move> legalMoves = blokusReferee.getOrderedLegalMoves(board, player);
            stopwatch.stop();

            final Set<BoardInterface<ColorInterface>> boards = Sets.newHashSet();
            final Map<Pieces, List<Move>> legalMovesByPiece = Maps.newTreeMap();

            for (final Move move : legalMoves) {
                final PieceInterface piece = move.getPiece();
                final Board newBoard = board.apply(move.getColor(), piece);
                final BoardInterface<ColorInterface> coloredBoard = newBoard.get();
                Preconditions.checkArgument(!boards.contains(coloredBoard));
                boards.add(coloredBoard);
                //View.as(blockplus.view.window.BoardView.class).show(newBoard).up();
                boardView.apply(newBoard);
                Pieces key = Pieces.get(piece.getId());
                List<Move> playablePositions = legalMovesByPiece.get(key);
                if (playablePositions == null) {
                    playablePositions = Lists.newArrayList(move);
                    legalMovesByPiece.put(key, playablePositions);
                }
                playablePositions.add(move);
            }

            System.out.println("-----------------------------8<-----------------------------");
            System.out.println("pieces      : " + bagOfPieces.size());
            System.out.println("legal moves : " + legalMoves.size());
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println(stopwatch);
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println(PieceComponent.FACTORY);
            System.out.println(PieceComposite.FACTORY);
            System.out.println("-----------------------------8<-----------------------------");
            for (Entry<Pieces, List<Move>> entry : legalMovesByPiece.entrySet()) {
                //System.out.println(entry);
            }
            /*
            Gson gson = new Gson();
            String json = gson.toJson(legalMovesByReferential);
            System.out.println(json);
            */
            
            Gson gson = JSONSerializer.getInstance();
            System.out.println(gson.toJson(legalMovesByPiece));
            System.out.println(gson.toJson(legalMovesByPiece.keySet()));
        }

    }
}