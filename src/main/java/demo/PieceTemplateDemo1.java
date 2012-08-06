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

import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceTemplate;

public class PieceTemplateDemo1 {

    public static void main(final String[] args) {

        for (final PieceTemplate pieces : PieceTemplate.values()) {
            System.out.println(pieces.name());
            System.out.println(pieces);
            System.out.println();
        }

        System.out.println(PieceComponent.FACTORY);
        System.out.println(PieceComposite.FACTORY);

    }

}