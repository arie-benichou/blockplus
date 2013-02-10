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

package blockplus.move;

import interfaces.move.MoveInterface;

import java.util.Comparator;

public final class MoveComparator implements Comparator<MoveInterface> {

    private final static MoveComparator INSTANCE = new MoveComparator();

    public static MoveComparator getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(final MoveInterface thisMoveInterface, final MoveInterface thatMoveInterface) {
        final Move thisMove = (Move) thisMoveInterface;
        final Move thatMove = (Move) thatMoveInterface;
        final int size1 = thisMove.getPiece().getSelfPositions().size();
        final int size2 = thatMove.getPiece().getSelfPositions().size();
        final int compare1 = size1 - size2;
        if (compare1 < 0) return 1;
        if (compare1 > 0) return -1;
        final int compare2 = thisMove.getPiece().getId() - thatMove.getPiece().getId();
        if (compare2 < 0) return 1;
        if (compare2 > 0) return -1;
        return thatMove.getPiece().getReferential().compareTo(thisMove.getPiece().getReferential());
    }

    private MoveComparator() {}

}