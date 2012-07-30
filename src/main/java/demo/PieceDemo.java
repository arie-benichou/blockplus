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

package demo;

import java.util.List;

import blockplus.Color;
import blockplus.board.Board;
import blockplus.board.BoardRenderingManager;
import blockplus.board.position.Position;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;

import com.google.common.base.Supplier;

public final class PieceDemo {

    private static void renderPiece(final Supplier<PieceInterface> pieceSupplier) {
        final List<PieceInterface> rotations = pieceSupplier.get().getRotations();
        for (final PieceInterface rotation : rotations) {
            System.out.println(rotation);
        }
    }

    private static void renderAllPieces() {
        for (final Pieces piece : Pieces.values()) {
            System.out.println("----------------8<----------------");
            System.out.println(piece.name());
            renderPiece(piece);
        }
    }

    private static void putPiece(final BoardRenderingManager boardRenderingManager, final Board board, final Supplier<PieceInterface> pieceSupplier) {
        for (final PieceInterface rotation : pieceSupplier.get().getRotations()) {
            System.out.println(boardRenderingManager.render(board.put(Color.Blue, rotation, Position.from(5, 10))));
        }
    }

    private static void putAllPieces() {
        final BoardRenderingManager boardRenderingManager = new BoardRenderingManager();
        final String data = "" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111" +
                "11111111111111111111";
        for (final Pieces piece : Pieces.values()) {
            System.out.println("----------------8<----------------");
            System.out.println(piece);
            putPiece(boardRenderingManager, Board.from(data), piece); // TODO ? BoardManager
        }
    }

    public static void main(final String[] args) {
        renderAllPieces();
        putAllPieces();
    }

}