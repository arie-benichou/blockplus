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

package blockplus.piece;

import java.util.Iterator;
import java.util.List;

import blockplus.matrix.Matrix;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class PieceForNull implements PieceInterface {

    private final static Matrix MATRIX = new Matrix(0, 0);

    @Override
    public Iterator<PositionInterface> iterator() {
        return Iterators.emptyIterator();
    }

    @Override
    public PieceInterface get() {
        return this;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getNumberOfCells() {
        return 0;
    }

    @Override
    public Matrix getMatrix() {
        return MATRIX;
    }

    @Override
    public PositionInterface getReferential() {
        return Position.from(-1, -1);
    }

    @Override
    public List<PieceInterface> getRotations() {
        return ImmutableList.of();
    }

    @Override
    public int getBoxingSquareSide() {
        return 0;
    }

    @Override
    public int getInstanceOrdinal() {
        return 0; // TODO ? -1
    }

    @Override
    public List<PositionInterface> getPositions() {
        return ImmutableList.of();
    }

    @Override
    public List<PositionInterface> getPositions(final PositionInterface position) {
        return ImmutableList.of();
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "NULL\n";
    }

}