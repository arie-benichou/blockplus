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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;

public final class PiecesBag implements Iterable<PieceTemplate> {

    private final static Multiset<PieceTemplate> EMPTY_MULTI_SET = ImmutableMultiset.of();
    private final static PiecesBag EMPTY_BAG = new PiecesBag(EMPTY_MULTI_SET);

    private static List<PieceTemplate> computeList(final Multiset<PieceTemplate> data) {
        final List<PieceTemplate> list = Lists.newArrayList();
        for (final Entry<PieceTemplate> entry : data.entrySet())
            for (int n = 0; n < entry.getCount(); ++n)
                list.add(entry.getElement());
        Collections.sort(list);
        return list;
    }

    public static PiecesBag from() {
        return EMPTY_BAG;
    }

    public static PiecesBag from(final PieceTemplate piece) {
        final Multiset<PieceTemplate> multiset = HashMultiset.create();
        multiset.add(piece);
        return new PiecesBag(multiset);
    }

    public static PiecesBag from(final Iterable<PieceTemplate> pieces) {
        return new PiecesBag(HashMultiset.create(pieces));
    }

    public static PiecesBag from(final PieceTemplate... pieces) {
        final Multiset<PieceTemplate> multiset = HashMultiset.create();
        for (final PieceTemplate piece : pieces) {
            multiset.add(piece);
        }
        return new PiecesBag(multiset);
    }

    private final Multiset<PieceTemplate> data;

    private transient volatile List<PieceTemplate> piecesAsList;

    private PiecesBag(final Multiset<PieceTemplate> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public List<PieceTemplate> asList() {
        List<PieceTemplate> value = this.piecesAsList;
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
    public Iterator<PieceTemplate> iterator() {
        return this.asList().iterator(); // TODO ?! return this.data.iterator();
    }

    public PiecesBag remove(final PieceTemplate piece) {
        Preconditions.checkArgument(this.data.contains(piece));
        final Multiset<PieceTemplate> copy = HashMultiset.create(this.data);
        copy.remove(piece, 1);
        return from(copy);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.asList()).toString();
    }

}