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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PiecesBag implements Iterable<PieceInterface> {

    private final static Comparator<PieceInterface> comparator = new Comparator<PieceInterface>() {

        @Override
        public int compare(final PieceInterface piece1, final PieceInterface piece2) {
            return piece1.getId() - piece2.getId();
        }

    };

    private final Map<PieceInterface, Integer> instanceOfPieces;
    private volatile List<PieceInterface> piecesAsList;

    public PiecesBag(final Map<PieceInterface, Integer> instanceOfPieces) {
        final Builder<PieceInterface, Integer> builder = new ImmutableSortedMap.Builder<PieceInterface, Integer>(PiecesBag.comparator);
        builder.putAll(instanceOfPieces);
        this.instanceOfPieces = builder.build();
    }

    private List<PieceInterface> computeList() {
        final List<PieceInterface> list = Lists.newArrayList();
        for (final Entry<PieceInterface, Integer> entry : this.instanceOfPieces.entrySet())
            for (int n = 0; n < entry.getValue(); ++n)
                list.add(entry.getKey());
        return list;
    }

    public List<PieceInterface> getList() {
        List<PieceInterface> value = this.piecesAsList;
        if (value == null)
            synchronized (this) {
                if ((value = this.piecesAsList) == null) this.piecesAsList = value = this.computeList();
            }
        return value;
    }

    public boolean isEmpty() {
        return this.getList().isEmpty();
    }

    public PiecesBag remove(final PieceInterface piece) {
        Preconditions.checkArgument(piece != null);
        final Integer instances = this.instanceOfPieces.get(piece);
        Preconditions.checkArgument(instances != null);
        Preconditions.checkArgument(instances > 0);
        final Map<PieceInterface, Integer> map = Maps.newHashMap(this.instanceOfPieces);
        map.put(piece, instances - 1);
        return new PiecesBag(map);
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.getList().iterator();
    }

}