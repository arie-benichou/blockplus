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
import blockplus.piece.PieceTemplateInterface;
import blockplus.piece.Pieces;
import blockplus.position.Position;

import com.google.common.base.Supplier;

public final class PieceDemo {

    private static void renderPiece(final Supplier<PieceTemplateInterface> pieceSupplier) {
        final List<PieceTemplateInterface> rotations = pieceSupplier.get().getRotations();
        for (final PieceTemplateInterface rotation : rotations) {
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

    private static void putPiece(final BoardRenderingManager boardRenderingManager, final Board board, final Supplier<PieceTemplateInterface> pieceSupplier) {
        for (final PieceTemplateInterface rotation : pieceSupplier.get().getRotations()) {
            System.out.println(boardRenderingManager.render(board.put(Color.Blue, rotation, Position.from(3, 3))));
        }
    }

    private static void putAllPieces() {
        final BoardRenderingManager boardRenderingManager = new BoardRenderingManager();
        final String data = "" +
                "3111113" +
                "1111111" +
                "1111111" +
                "1111111" +
                "1111111" +
                "1111111" +
                "3111113";
        for (final Pieces piece : Pieces.values()) {
            System.out.println("----------------8<----------------");
            System.out.println(piece.name());
            putPiece(boardRenderingManager, Board.from(data), piece); // TODO ? BoardManager
        }
    }

    public static void main(final String[] args) {
        renderAllPieces();
        putAllPieces();
    }

}
