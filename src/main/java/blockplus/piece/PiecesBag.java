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

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public final class PiecesBag implements Iterable<Pieces> {

    private final static Multiset<Pieces> EMPTY_MULTI_SET = ImmutableMultiset.of();
    private final static PiecesBag EMPTY_BAG = new PiecesBag(EMPTY_MULTI_SET);

    private static List<Pieces> computeList(final Multiset<Pieces> data) {
        final List<Pieces> list = Lists.newArrayList();
        for (final Entry<Pieces> entry : data.entrySet())
            for (int n = 0; n < entry.getCount(); ++n)
                list.add(entry.getElement());
        //Collections.sort(list); // TODO comparator
        return list;
    }

    public static PiecesBag from() {
        return EMPTY_BAG;
    }

    public static PiecesBag from(final Pieces piece) {
        final Multiset<Pieces> multiset = HashMultiset.create();
        multiset.add(piece);
        return new PiecesBag(multiset);
    }

    public static PiecesBag from(final Iterable<Pieces> pieces) {
        return new PiecesBag(HashMultiset.create(pieces));
    }

    public static PiecesBag from(final Pieces... pieces) {
        final Multiset<Pieces> multiset = HashMultiset.create();
        for (final Pieces piece : pieces) {
            multiset.add(piece);
        }
        return new PiecesBag(multiset);
    }

    private final Multiset<Pieces> data;

    private transient volatile List<Pieces> piecesAsList;

    private PiecesBag(final Multiset<Pieces> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    // TODO ! immutable list
    public List<Pieces> asList() {
        List<Pieces> value = this.piecesAsList;
        if (value == null)
            synchronized (this) {
                if ((value = this.piecesAsList) == null) this.piecesAsList = value = computeList(this.data);
            }
        return value;
    }

    public int size() {
        return this.data.size();
    }

    @Override
    public Iterator<Pieces> iterator() {
        return this.asList().iterator();
    }

    public boolean contains(final Pieces piece) {
        return this.data.contains(piece);
    }

    public PiecesBag remove(final Pieces piece) {
        // TODO !!! à revoir
        if (piece.ordinal() == 0) {
            final Multiset<Pieces> copy = HashMultiset.create();
            return from(copy);
        }
        Preconditions.checkArgument(this.contains(piece), piece + " is not in " + this.data);
        final Multiset<Pieces> copy = HashMultiset.create(this.data);
        copy.remove(piece, 1);
        return from(copy);
    }

    // TODO ! à revoir
    // TODO !? Piece.getNumberOfCells()
    // TODO ? caching
    public int getWeight() {
        int sum = 0;
        for (final Pieces piece : this)
            sum += piece.getInstances().getDistinctInstance(0).getSelfPositions().size();
        return sum;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue("\n" + this.asList())
                .addValue("\n" + this.getWeight())
                .toString();
    }

}