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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public final class PiecesBag implements Iterable<Piece> {

    private final static Multiset<Piece> EMPTY_MULTI_SET = ImmutableMultiset.of();
    private final static PiecesBag EMPTY_BAG = new PiecesBag(EMPTY_MULTI_SET);

    private static List<Piece> computeList(final Multiset<Piece> data) {
        final List<Piece> list = Lists.newArrayList();
        for (final Entry<Piece> entry : data.entrySet())
            for (int n = 0; n < entry.getCount(); ++n)
                list.add(entry.getElement());
        //Collections.sort(list); // TODO comparator
        return list;
    }

    public static PiecesBag from() {
        return EMPTY_BAG;
    }

    public static PiecesBag from(final Piece piece) {
        final Multiset<Piece> multiset = HashMultiset.create();
        multiset.add(piece);
        return new PiecesBag(multiset);
    }

    public static PiecesBag from(final Supplier<Piece> pieceSupplier) {
        return from(pieceSupplier.get());
    }

    public static PiecesBag from(final Iterable<Piece> pieces) {
        return new PiecesBag(HashMultiset.create(pieces));
    }

    public static PiecesBag from(final Piece... pieces) {
        final Multiset<Piece> multiset = HashMultiset.create();
        for (final Piece piece : pieces) {
            multiset.add(piece);
        }
        return new PiecesBag(multiset);
    }

    private final Multiset<Piece> data;

    private transient volatile List<Piece> piecesAsList;

    private PiecesBag(final Multiset<Piece> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public List<Piece> asList() {
        List<Piece> value = this.piecesAsList;
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
    public Iterator<Piece> iterator() {
        return this.asList().iterator(); // TODO ?! return this.data.iterator();
    }

    public PiecesBag remove(final Piece piece) {
        Preconditions.checkArgument(this.data.contains(piece));
        final Multiset<Piece> copy = HashMultiset.create(this.data);
        copy.remove(piece, 1);
        return from(copy);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.asList()).toString();
    }

}