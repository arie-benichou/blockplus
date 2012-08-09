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

package demo.piece;

import static blockplus.piece.Piece.Piece;
import blockplus.piece.PieceInterface;

// TODO à revoir
public final class PieceDemo4 {

    public static void main(final String[] args) {
        final PieceInterface rotated0 = Piece(5);
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println(rotated0);
        for (final PieceInterface pieceInterface : rotated0)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated1 = rotated0.rotate();
        System.out.println(rotated1);
        for (final PieceInterface pieceInterface : rotated1)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated2 = rotated1.rotate();
        System.out.println(rotated2);
        for (final PieceInterface pieceInterface : rotated2)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated3 = rotated2.rotate();
        System.out.println(rotated3);
        for (final PieceInterface pieceInterface : rotated3)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println();
        final PieceInterface rotated4 = rotated3.rotate();
        System.out.println(rotated4);
        for (final PieceInterface pieceInterface : rotated4)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
    }

}