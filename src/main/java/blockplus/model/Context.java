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

package blockplus.model;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import blockplus.model.polyomino.Polyomino;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;
import blockplus.model.polyomino.Polyominos;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import components.cells.IPosition;

public final class Context {

    public final static class Builder {

        private final static Colors SIDE = Blue;
        private final static Set<Polyomino> LEGAL_PIECES = Polyomino.set();
        private final static Pieces REMAINING_PIECES = new Pieces.Builder().addAll(LEGAL_PIECES).build();
        private final static SidesOrdering SIDES_ORDERING = new SidesOrdering.Builder().add(Blue).add(Yellow).add(Red).add(Green).build();
        private final static Sides SIDES = new Sides.Builder(SIDES_ORDERING)
                .add(Side.with(REMAINING_PIECES))
                .add(Side.with(REMAINING_PIECES))
                .add(Side.with(REMAINING_PIECES))
                .add(Side.with(REMAINING_PIECES))
                .build();
        private final static int ROWS = 20;
        private final static int COLUMNS = 20;
        private final static Board BOARD = new Board.Builder(ROWS, COLUMNS).build();

        public Context build() {
            return new Context(SIDE, SIDES, BOARD);
        }

    }

    private final Colors side;

    public Colors side() {
        return this.side;
    }

    private final Sides sides;

    public Sides sides() {
        return this.sides;
    }

    private final Board board;

    public Board board() {
        return this.board;
    }

    private volatile Options options;

    Context(final Colors side, final Sides players, final Board board) {
        this.side = side;
        this.sides = players;
        this.board = board;
    }

    private Context(final Context context) {
        this(context.next(), context.sides(), context.board());
    }

    public Context apply(final Move move) {
        final Colors color = move.color();
        Preconditions.checkState(this.side().equals(color));
        final SortedSet<IPosition> positions = move.positions();
        final Polyominos polyominos = Polyominos.getInstance();
        final PolyominoTranslatedInstance translatedInstance = polyominos.get(positions);
        return new Context(color,
                this.sides().apply(color, translatedInstance.type()),
                this.board().apply(color, translatedInstance));
    }

    public boolean isTerminal() {
        return !this.sides().hasSide();
    }

    public Options options() {
        Options value = this.options;
        if (value == null) synchronized (this) {
            if ((value = this.options) == null) this.options = value = new OptionsSupplier(this).get();
        }
        return value;
    }

    public Colors next(final Colors color) {
        final Colors nextColor = this.sides().next(color);
        final Side nextSide = this.sides().getSide(nextColor);
        return nextSide.isNull() ? this.next(nextColor) : nextColor;
    }

    public Colors next() {
        return this.next(this.side());
    }

    public Context forward() {
        if (this.isTerminal()) return this;
        Context nextContext = new Context(this);
        if (nextContext.options().isEmpty()) {
            final TreeSet<IPosition> emptySet = Sets.newTreeSet(); // TODO extract constant
            nextContext = nextContext.apply(new Move(nextContext.side(), emptySet));
            nextContext = nextContext.forward();
        }
        return nextContext;
    }

    public Side getPlayer(final Colors color) {
        return this.sides().getSide(color);
    }

    public Side getPlayer() {
        return this.getPlayer(this.side());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.board())
                .toString();
    }

}