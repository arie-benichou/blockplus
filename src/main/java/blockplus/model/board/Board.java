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

package blockplus.model.board;

import static blockplus.model.color.Colors.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.model.color.Color;
import blockplus.model.color.ColorInterface;
import blockplus.model.piece.PieceInterface;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import components.board.BoardInterface;
import components.board.Symbol;
import components.position.PositionInterface;

public final class Board implements Supplier<BoardInterface<? extends Symbol>> {

    public final static class Builder {

        private static Set<ColorInterface> check(final Set<ColorInterface> colors) {
            Preconditions.checkArgument(colors != null);
            Preconditions.checkArgument(!colors.isEmpty());
            return colors;
        }

        private static int check(final int naturalInteger) {
            Preconditions.checkArgument(naturalInteger > 0);
            return naturalInteger;
        }

        private final Set<ColorInterface> colors;

        public Set<ColorInterface> getColors() {
            return this.colors;
        }

        public int getRows() {
            return this.rows;
        }

        public int getColumns() {
            return this.columns;
        }

        private final int rows;
        private final int columns;

        private final Map<ColorInterface, BoardLayer> layerByColor = Maps.newHashMap();

        public Builder(final Set<ColorInterface> colors, final int rows, final int columns) {
            this.colors = check(colors);
            this.rows = check(rows);
            this.columns = check(columns);
        }

        public Builder set(final ColorInterface color, final BoardLayer layer) {
            Preconditions.checkArgument(this.colors.contains(color));
            Preconditions.checkArgument(this.rows == layer.rows());
            Preconditions.checkArgument(this.columns == layer.columns());
            this.layerByColor.put(color, layer);
            return this;
        }

        public Board build() {
            if (this.layerByColor.isEmpty())
                for (final ColorInterface color : this.colors) {
                    this.layerByColor.put(color, new BoardLayer(this.rows, this.columns));
                }
            Preconditions.checkState(this.colors.size() == this.layerByColor.size());
            return new Board(this);
        }

    }

    public static Builder builder(final Set<ColorInterface> colors, final int rows, final int columns) {
        return new Board.Builder(colors, rows, columns);
    }

    // TODO sorted map
    private final Map<ColorInterface, BoardLayer> layerByColor;

    public Set<ColorInterface> getColors() {
        return this.layerByColor.keySet();
    }

    private final int rows;

    public int rows() {
        return this.rows;
    }

    private final int columns;

    public int columns() {
        return this.columns;
    }

    private Board(final Map<ColorInterface, BoardLayer> stateBoardByColor, final int rows, final int columns) {
        this.layerByColor = ImmutableMap.copyOf(stateBoardByColor);
        this.rows = rows;
        this.columns = columns;
    }

    private Board(final Builder builder) {
        this(builder.layerByColor, builder.rows, builder.columns);
    }

    public BoardLayer getLayer(final ColorInterface color) {
        return this.layerByColor.get(color);
    }

    public boolean isLegal(final ColorInterface color, final PieceInterface piece) {
        return this.getLayer(color).isLegal(piece.getSelfPositions());
    }

    public Board apply(final ColorInterface color, final PieceInterface piece) {
        final Map<ColorInterface, BoardLayer> newLayers = Maps.newHashMap();
        final Map<PositionInterface, State> mutation = new MutationBuilder()
                .setOtherPositions(piece.getSelfPositions())
                .build();
        for (final ColorInterface anotherColor : this.getColors()) {
            if (!anotherColor.is(color)) newLayers.put(anotherColor, this.getLayer(anotherColor).apply(mutation));
            else newLayers.put(color, this.getLayer(color).apply(piece));
        }
        return new Board(newLayers, this.rows(), this.columns());
    }

    ///////////////////////////////////////////////////////////////////////////////

    public boolean isSelf(final ColorInterface color, final PositionInterface position) {
        return this.getLayer(color).isSelf(position);
    }

    public boolean isShadow(final ColorInterface color, final PositionInterface position) {
        return this.getLayer(color).isShadow(position);
    }

    public boolean isPotential(final ColorInterface color, final PositionInterface position) {
        return this.getLayer(color).isLight(position);
    }

    public boolean isNone(final ColorInterface color, final PositionInterface position) {
        return this.getLayer(color).isNone(position);
    }

    public boolean isOther(final ColorInterface color, final PositionInterface position) {
        return this.getLayer(color).isOther(position);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public boolean isSelf(final PositionInterface position) {
        for (final ColorInterface color : this.getColors())
            if (!this.getLayer(color).isSelf(position)) return false;
        return true;
    }

    public boolean isShadow(final PositionInterface position) {
        for (final ColorInterface color : this.getColors())
            if (!this.getLayer(color).isShadow(position)) return false;
        return true;
    }

    public boolean isPotential(final PositionInterface position) {
        for (final ColorInterface color : this.getColors())
            if (!this.getLayer(color).isLight(position)) return false;
        return true;
    }

    public boolean isNone(final PositionInterface position) {
        for (final ColorInterface color : this.getColors())
            if (!this.getLayer(color).isNone(position)) return false;
        return true;
    }

    public boolean isOther(final PositionInterface position) {
        for (final ColorInterface color : this.getColors())
            if (!this.getLayer(color).isOther(position)) return false;
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////

    public boolean hasPotential(final PositionInterface position) {
        int potentials = 0;
        for (final ColorInterface color : this.getColors()) {
            final BoardLayer layer = this.getLayer(color);
            if (layer.isLight(position)) ++potentials;
            else if (!layer.isMutable(position)) return false;
        }
        return potentials != 0;
    }

    public boolean hasNone(final PositionInterface position) {
        int nones = 0;
        for (final ColorInterface color : this.getColors()) {
            final BoardLayer layer = this.getLayer(color);
            if (layer.isSelfOrOther(position)) return false;
            if (layer.isNone(position)) ++nones;
        }
        return nones != 0;
    }

    public boolean hasShadow(final PositionInterface position) {
        int shadows = 0;
        for (final ColorInterface color : this.getColors()) {
            final BoardLayer layer = this.getLayer(color);
            if (layer.isSelfOrOther(position)) return false;
            if (layer.isShadow(position)) ++shadows;
        }
        return shadows != 0;
    }

    public boolean isSelfOrOther(final PositionInterface position) {
        for (final ColorInterface color : this.getColors())
            if (!this.getLayer(color).isSelfOrOther(position)) return false;
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////

    public final BoardInterface<ColorInterface> colorize() {
        final Map<PositionInterface, ColorInterface> mutations = Maps.newHashMap();
        for (final ColorInterface color : this.getColors())
            for (final PositionInterface position : this.getLayer(color).getSelves().keySet())
                mutations.put(position, color);
        return components.board.Board.from(this.rows(), this.columns(), White, Black, mutations);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public final BoardInterface<Concurrency> getSelfLightConcurrency(final ColorInterface givenColor) {

        final Map<PositionInterface, ColorInterface> mutations = Maps.newHashMap();

        {
            final Map<PositionInterface, State> lights = this.getLayer(givenColor).getLights();
            for (final PositionInterface position : lights.keySet())
                mutations.put(position, givenColor);
        }

        for (final ColorInterface color : this.getColors()) {
            if (!color.is(givenColor)) {
                final Map<PositionInterface, State> lights = this.getLayer(color).getLights();
                for (final PositionInterface position : lights.keySet()) {
                    final ColorInterface otherColor = mutations.get(position);
                    if (otherColor == null) mutations.put(position, color);
                    else {
                        if (otherColor.contains(givenColor)) {
                            final ColorInterface newColor = new Color.Builder().add(otherColor).add(color).build();
                            mutations.put(position, newColor);
                        }
                    }
                }
            }
        }

        for (final ColorInterface color : this.getColors()) {
            if (!color.is(givenColor)) {
                final Map<PositionInterface, State> selves = this.getLayer(color).getSelves();
                for (final PositionInterface position : selves.keySet())
                    mutations.put(position, Black);
            }
        }

        final Map<PositionInterface, State> shadows = this.getLayer(givenColor).getShadows();
        for (final PositionInterface position : shadows.keySet())
            mutations.put(position, Black);

        final Map<PositionInterface, State> selves = this.getLayer(givenColor).getSelves();
        for (final PositionInterface position : selves.keySet())
            mutations.put(position, Black);

        final Map<PositionInterface, Concurrency> lightsConcurrency = Maps.newHashMap();
        final Set<Entry<PositionInterface, ColorInterface>> entrySet = mutations.entrySet();
        for (final Entry<PositionInterface, ColorInterface> entry : entrySet) {
            final ColorInterface color = entry.getValue();
            if (color.contains(givenColor)) {
                final PositionInterface position = entry.getKey();
                final int size = color.set().size();
                final Concurrency concurrency = Concurrency.get(size);
                lightsConcurrency.put(position, concurrency);
            }
        }

        return components.board.Board.from(this.rows(), this.columns(), Concurrency.NONE, Concurrency.NONE, lightsConcurrency);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public final BoardInterface<Remaining> getRemainingSpace(final ColorInterface givenColor) {

        final Map<PositionInterface, Remaining> remainingSpace = Maps.newHashMap();

        // on enlève toutes les pièces posées
        for (final ColorInterface color : this.getColors()) {
            final Map<PositionInterface, State> selves = this.getLayer(color).getSelves();
            for (final PositionInterface position : selves.keySet()) {
                remainingSpace.put(position, Remaining.NONE);
            }
        }

        // on enlève toutes les ombres de la couleur donnée
        final Map<PositionInterface, State> shadows = this.getLayer(givenColor).getShadows();
        for (final PositionInterface position : shadows.keySet())
            remainingSpace.put(position, Remaining.NONE);

        return components.board.Board.from(this.rows(), this.columns(), Remaining.ALL, Remaining.NONE, remainingSpace);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public final BoardInterface<Capacity> getCapacity(final ColorInterface givenColor) {

        final Map<PositionInterface, Capacity> capacity = Maps.newHashMap();

        // on enlève toutes les pièces posées
        for (final ColorInterface color : this.getColors()) {
            final Map<PositionInterface, State> selves = this.getLayer(color).getSelves();
            for (final PositionInterface position : selves.keySet()) {
                capacity.put(position, Capacity.NONE);
            }
        }

        // on enlève toutes les ombres de la couleur donnée
        final Map<PositionInterface, State> shadows = this.getLayer(givenColor).getShadows();
        for (final PositionInterface position : shadows.keySet())
            capacity.put(position, Capacity.NONE);

        final Map<PositionInterface, ColorInterface> mutations = Maps.newHashMap();

        for (final ColorInterface color : this.getColors()) {
            if (!color.is(givenColor)) {
                final Map<PositionInterface, State> otherShadows = this.getLayer(color).getShadows();
                for (final PositionInterface position : otherShadows.keySet()) {
                    final ColorInterface otherColor = mutations.get(position);
                    if (otherColor == null) mutations.put(position, color);
                    else if (!otherColor.contains(givenColor)) {
                        final ColorInterface newColor = new Color.Builder().add(otherColor).add(color).build();
                        mutations.put(position, newColor);
                    }
                }
            }
        }

        final Set<Entry<PositionInterface, ColorInterface>> entrySet = mutations.entrySet();
        for (final Entry<PositionInterface, ColorInterface> entry : entrySet) {
            final ColorInterface color = entry.getValue();
            final PositionInterface position = entry.getKey();
            final int size = color.set().size();
            if (capacity.get(position) == null)
                capacity.put(position, Capacity.get(this.getColors().size() - size));
        }

        // TODO initialiser à Capacity.NONE et créer Capacity.UNKNOWN / OTHER 
        return components.board.Board.from(this.rows(), this.columns(), Capacity.FOUR, Capacity.NONE, capacity);
    }

    ///////////////////////////////////////////////////////////////////////////////

    public final BoardInterface<Concurrency> getLightConcurrency(final ColorInterface givenColor) {

        final Map<PositionInterface, Concurrency> concurrency = Maps.newHashMap();

        for (final ColorInterface color : this.getColors()) {
            final Map<PositionInterface, State> selves = this.getLayer(color).getSelves();
            for (final PositionInterface position : selves.keySet()) {
                concurrency.put(position, Concurrency.NONE);
            }
        }

        final Map<PositionInterface, State> shadows = this.getLayer(givenColor).getShadows();
        for (final PositionInterface position : shadows.keySet())
            concurrency.put(position, Concurrency.NONE);

        final Map<PositionInterface, ColorInterface> mutations = Maps.newHashMap();

        for (final ColorInterface color : this.getColors()) {
            final Map<PositionInterface, State> lights = this.getLayer(color).getLights();
            for (final PositionInterface position : lights.keySet()) {
                final ColorInterface otherColor = mutations.get(position);
                if (otherColor == null) mutations.put(position, color);
                else {
                    final ColorInterface newColor = new Color.Builder().add(otherColor).add(color).build();
                    mutations.put(position, newColor);
                }
            }
        }

        final Set<Entry<PositionInterface, ColorInterface>> entrySet = mutations.entrySet();
        for (final Entry<PositionInterface, ColorInterface> entry : entrySet) {
            final ColorInterface color = entry.getValue();
            final PositionInterface position = entry.getKey();
            final int size = color.set().size();
            if (concurrency.get(position) == null)
                concurrency.put(position, Concurrency.get(size));
        }

        // TODO créer Concurrency.UNKNOWN/OTHER
        return components.board.Board.from(this.rows(), this.columns(), Concurrency.NONE, Concurrency.NONE, concurrency);
    }

    ///////////////////////////////////////////////////////////////////////////////

    // TODO ? calculer combien de fois une cellule est Light pour la même couleur

    @Override
    public BoardInterface<ColorInterface> get() {
        return this.colorize();
    }

}