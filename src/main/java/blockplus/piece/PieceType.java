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

package blockplus.piece;

import static blockplus.piece.PieceData.PieceData;

import java.util.Iterator;
import java.util.Set;

import blockplus.piece.matrix.Matrix;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import components.position.PositionInterface;

public enum PieceType implements Iterable<PieceInterface> {

    // null piece
    PIECE0,

    // monominoes
    PIECE1,

    // dominoes
    PIECE2,

    // triominoes
    PIECE3, PIECE4,

    // tetrominoes
    PIECE5, PIECE6, PIECE7, PIECE8, PIECE9,

    // pentominoes
    PIECE10, PIECE11, PIECE12, PIECE13, PIECE14, PIECE15, PIECE16,
    PIECE17, PIECE18, PIECE19, PIECE20, PIECE21;

    private final static Set<PieceType> SET = new ImmutableSortedSet.Builder<PieceType>(Ordering.natural()).add(PieceType.values()).build();

    private final static String PIECE_NAME_PATTERN = "PIECE";

    public final static PieceType get(final int ordinal) {
        return PieceType.valueOf(PIECE_NAME_PATTERN + ordinal);
    }

    public static Set<PieceType> asSet() {
        return SET;
    }

    private final PieceData data;

    private PieceData getData() {
        return this.data;
    }

    public PositionInterface referential() {
        return this.getData().referential();
    }

    public Set<PositionInterface> positions() {
        return this.getData().positions();
    }

    public Matrix matrix() {
        return this.getData().matrix();
    }

    public int radius() {
        return this.getData().radius();
    }

    public int size() {
        return this.getData().size();
    }

    public int id() {
        return this.ordinal();
    }

    private volatile PieceInstances instances;

    private PieceInstances getInstances() {
        PieceInstances instances = this.instances;
        if (instances == null) {
            synchronized (this) {
                if ((instances = this.instances) == null) this.instances = instances = new PieceInstances(this);
            }
        }
        return instances;
    }

    private PieceType() {
        this.data = PieceData(this.ordinal());
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.getInstances().iterator();
    }

}