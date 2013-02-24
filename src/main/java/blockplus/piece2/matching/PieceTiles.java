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

package blockplus.piece2.matching;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;

// TODO use bimap
public final class PieceTiles {

    public enum PieceTile {

        SOME('O'),
        NONE(' ');

        private final char character;

        private PieceTile(final char character) {
            this.character = character;
        }

        public char toCharacter() {
            return this.character;
        }

        public boolean is(final PieceTile tile) {
            return this.equals(tile);
        }

    }

    private final static PieceTile[] TILES = PieceTile.values();

    private final static Builder<Character, PieceTile> TILE_BY_CHARACTER_MAP_BUILDER = new ImmutableSortedMap.Builder<Character, PieceTile>(Ordering.natural());
    static {
        for (final PieceTile tile : TILES)
            TILE_BY_CHARACTER_MAP_BUILDER.put(tile.toCharacter(), tile);
    }

    private final static ImmutableSortedMap<Character, PieceTile> TILE_BY_CHARACTER_MAP = TILE_BY_CHARACTER_MAP_BUILDER.build();

    public static PieceTile from(final Character character) {
        Preconditions.checkArgument(character != null);
        final PieceTile tile = TILE_BY_CHARACTER_MAP.get(character);
        Preconditions.checkState(tile != null, "Unknown tile from character: " + character);
        return tile;
    }

    private PieceTiles() {}

}