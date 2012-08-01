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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import blockplus.Color;
import blockplus.Move;
import blockplus.board.Board;
import blockplus.board.BoardRepresentation;
import blockplus.piece.PieceTemplateInterface;
import blockplus.piece.Pieces;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

public final class PieceDemo {

    // TODO précomputer les positions d'ancrage d'un template de pièce et s'en servir pour le score
    // et pour la recherche de positions légales potentielles

    private static double computeScore(final PieceTemplateInterface pieceTemplate) {

        if (pieceTemplate.isNull()) return Double.POSITIVE_INFINITY;

        final int boxingSquareSide = pieceTemplate.getBoxingSquareSide();
        final int numberOfCells = pieceTemplate.getNumberOfCells();
        final int numberOfRotations = pieceTemplate.getRotations().size();

        double score = 1;
        score *= 5.0 / numberOfCells;
        score *= 1.0 * numberOfCells / (boxingSquareSide * boxingSquareSide);
        score *= numberOfRotations;
        score -= 5 * numberOfCells;
        //score += 1;
        return score;

    }

    private final static Comparator<PieceTemplateInterface> COMPARATOR = new Comparator<PieceTemplateInterface>() {

        @Override
        public int compare(final PieceTemplateInterface pt1, final PieceTemplateInterface pt2) {
            return Double.compare(computeScore(pt1), computeScore(pt2));
        }

    };

    private static void renderAllPiecesByScore() {

        final List<Pieces> pieces = Lists.newArrayList(Pieces.values());

        final List<PieceTemplateInterface> templates = Lists.transform(pieces, new Function<Pieces, PieceTemplateInterface>() {

            @Override
            public PieceTemplateInterface apply(@Nullable final Pieces input) {
                return input.get();
            }

        });

        final List<PieceTemplateInterface> list = Lists.newArrayList(templates);

        Collections.sort(list, COMPARATOR);

        for (final PieceTemplateInterface piece : list) {
            System.out.println("----------------8<----------------");
            System.out.println(piece);
            System.out.println(computeScore(piece));
        }

    }

    private static void renderPiece(final Supplier<PieceTemplateInterface> pieceSupplier) {
        final PieceTemplateInterface pieceTemplate = pieceSupplier.get();
        final List<PieceTemplateInterface> rotations = pieceTemplate.getRotations();
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

    private static void putPiece(final Board<Color> board, final Supplier<PieceTemplateInterface> pieceSupplier) {
        final PositionInterface position = Position.from(3, 3);
        for (final PieceTemplateInterface rotation : pieceSupplier.get().getRotations()) {
            final Move move = new Move(Color.Blue, position, rotation, board); // TODO ? MoveManager.emit(move)
            System.out.println(move.getOutputBoard());
        }
    }

    private static void putAllPieces() {

        final int[][] data = {
                { 3, 1, 1, 1, 1, 1, 5 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1 },
                { 7, 1, 1, 1, 1, 1, 1 }
        };

        final Board<Color> board = BoardRepresentation.parse(data);
        for (final Pieces piece : Pieces.values()) {
            System.out.println("----------------8<----------------");
            System.out.println(piece.name());
            putPiece(board, piece);
        }
    }

    public static void main(final String[] args) {
        renderAllPieces();
        putAllPieces();
        renderAllPiecesByScore();
    }
}
