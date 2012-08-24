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

import blockplus.model.piece.PieceInstances;
import blockplus.model.piece.PieceInstancesFactory;
import blockplus.model.piece.PieceInterface;

public class PieceInstancesFactoryDemo {

    public static void main(final String[] args) {
        final PieceInstancesFactory pieceInstancesFactory = new PieceInstancesFactory();
        {

            System.out.println("----------------------------8<----------------------------");
            final PieceInstances pieceInstances = pieceInstancesFactory.get(7);
            final int n = pieceInstances.getNumberOfDistinctInstances();
            System.out.println("\n" + n + " instance(s): \n");
            for (final PieceInterface pieceInstance : pieceInstances)
                System.out.println(pieceInstance);
            System.out.println("----------------------------8<----------------------------");
        }
        {
            final PieceInstances pieceInstances = pieceInstancesFactory.get(7);
            final int n = pieceInstances.getNumberOfDistinctInstances();
            System.out.println("\n" + n + " instance(s): \n");
            for (final PieceInterface pieceInstance : pieceInstances)
                System.out.println(pieceInstance);
            System.out.println("----------------------------8<----------------------------");

        }
    }

}