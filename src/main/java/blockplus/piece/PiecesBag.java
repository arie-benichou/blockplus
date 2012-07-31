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

public class PiecesBag implements Iterable<PieceTemplateInterface> {

    private final static Comparator<PieceTemplateInterface> comparator = new Comparator<PieceTemplateInterface>() {

        @Override
        public int compare(final PieceTemplateInterface piece1, final PieceTemplateInterface piece2) {
            return piece1.getId() - piece2.getId();
        }

    };

    private final Map<PieceTemplateInterface, Integer> instanceOfPieces;
    private volatile List<PieceTemplateInterface> piecesAsList;

    public PiecesBag(final Map<PieceTemplateInterface, Integer> instanceOfPieces) {
        final Builder<PieceTemplateInterface, Integer> builder = new ImmutableSortedMap.Builder<PieceTemplateInterface, Integer>(PiecesBag.comparator);
        builder.putAll(instanceOfPieces);
        this.instanceOfPieces = builder.build();
    }

    private List<PieceTemplateInterface> computeList() {
        final List<PieceTemplateInterface> list = Lists.newArrayList();
        for (final Entry<PieceTemplateInterface, Integer> entry : this.instanceOfPieces.entrySet())
            for (int n = 0; n < entry.getValue(); ++n)
                list.add(entry.getKey());
        return list;
    }

    public List<PieceTemplateInterface> getList() {
        List<PieceTemplateInterface> value = this.piecesAsList;
        if (value == null)
            synchronized (this) {
                if ((value = this.piecesAsList) == null) this.piecesAsList = value = this.computeList();
            }
        return value;
    }

    public boolean isEmpty() {
        return this.getList().isEmpty();
    }

    public PiecesBag remove(final PieceTemplateInterface piece) {
        Preconditions.checkArgument(piece != null);
        final Integer instances = this.instanceOfPieces.get(piece);
        Preconditions.checkArgument(instances != null);
        Preconditions.checkArgument(instances > 0);
        final Map<PieceTemplateInterface, Integer> map = Maps.newHashMap(this.instanceOfPieces);
        map.put(piece, instances - 1);
        return new PiecesBag(map);
    }

    @Override
    public Iterator<PieceTemplateInterface> iterator() {
        return this.getList().iterator();
    }

}