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

package blockplus.demo.piece;

import static blockplus.model.color.Colors.*;
import static components.position.Position.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import blockplus.model.color.ColorInterface;
import blockplus.model.color.Colors;
import blockplus.model.piece.PieceData;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.board.Board;
import components.board.BoardInterface;
import components.board.rendering.StringRendering;
import components.position.PositionInterface;

public class PiecesDemo1 {

    public static void main(final String[] args) {

        final Map<?, Character> symbols = new ImmutableMap.Builder<Object, Character>()
                .put(Blue, 'B')
                .put(Yellow, 'Y')
                .put(Red, 'R')
                .put(Green, 'G')
                .put(White, ' ')
                .put(Black, 'Ø')
                .build();
        final StringRendering rendering = new StringRendering(symbols);

        for (final Pieces piece : Pieces.set()) {
            final PieceInterface pieceInterface = piece.getInstances().getDistinctInstance(0); // TODO !! à revoir
            final int radius = PieceData.PieceData(piece.ordinal()).radius(); // TODO !! à revoir
            final int n = 1 + 2 * (radius + 1);
            final BoardInterface<ColorInterface> board = Board.from(n, n, Colors.White, Colors.Black);
            final PositionInterface position = Position(n / 2, n / 2);
            final PieceInterface translatedPiece = pieceInterface.translateTo(position);
            final Set<BoardInterface<ColorInterface>> rotations = Sets.newHashSet();
            PieceInterface rotatedPiece = translatedPiece;
            {
                final Set<PositionInterface> selfPositions = rotatedPiece.getSelfPositions();
                final HashMap<PositionInterface, ColorInterface> mutations = Maps.newHashMap();
                for (final PositionInterface location : selfPositions) {
                    mutations.put(location, Colors.Blue);
                }
                final BoardInterface<ColorInterface> newBoard = board.apply(mutations);
                rotations.add(newBoard);
                System.out.println();
                System.out.println("=================8<=================");
                System.out.println(rendering.apply(newBoard));
            }
            for (int i = 1; i < 4; ++i)
            {
                rotatedPiece = rotatedPiece.rotate();
                System.out.println();
                final Set<PositionInterface> selfPositions = rotatedPiece.getSelfPositions();
                final HashMap<PositionInterface, ColorInterface> mutations = Maps.newHashMap();
                for (final PositionInterface location : selfPositions) {
                    mutations.put(location, Colors.Blue);
                }
                final BoardInterface<ColorInterface> newBoard = board.apply(mutations);
                rotations.add(newBoard);
                System.out.println(rendering.apply(newBoard));
            }
            System.out.println("=================8<=================");
            System.out.println("Nombre de rotations distinctes: " + rotations.size());
            System.out.println("=================8<=================");

        }
    }
}