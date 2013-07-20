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

import blockplus.model.interfaces.IContext;
import blockplus.model.interfaces.IMove;
import blockplus.model.interfaces.IOptionsSupplier;
import blockplus.model.polyomino.Polyomino;
import blockplus.model.polyomino.PolyominoInstances.PolyominoTranslatedInstance;
import blockplus.model.polyomino.Polyominos;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import components.cells.IPosition;

public final class Context implements IContext<Colors> {

    public final static class Builder {
        private final static Set<Polyomino> LEGAL_PIECES = Polyomino.set();
        private final static PolyominoSet REMAINING_PIECES = new PolyominoSet.Builder().addAll(LEGAL_PIECES).build();
        private final static Sides SIDES = new Sides.Builder()
                .add(Blue, Side.from(REMAINING_PIECES))
                .add(Yellow, Side.from(REMAINING_PIECES))
                .add(Red, Side.from(REMAINING_PIECES))
                .add(Green, Side.from(REMAINING_PIECES))
                .build();
        private final static Colors SIDE = Blue;
        private final static Adversity ADVERSITY = new Adversity.Builder().add(Blue).add(Yellow).add(Red).add(Green).build();
        private final static int ROWS = 20;
        private final static int COLUMNS = 20;
        private final static Board BOARD = new Board.Builder(ROWS, COLUMNS).build();
        private final static IOptionsSupplier OPTIONS_SUPPLIER = new OptionsSupplier();

        public Context build() {
            return new Context(SIDE, BOARD, SIDES, ADVERSITY, OPTIONS_SUPPLIER);
        }
    }

    // TODO inject
    private final static Predicate<Context> TERMINATION_PREDICATE = new Predicate<Context>() {

        @Override
        public boolean apply(final Context context) {
            return !context.players().hasAlivePlayer();
        }

    };

    private final Colors side;

    @Override
    public Colors side() {
        return this.side;
    }

    private final Adversity adversity;

    @Override
    public Adversity adversity() {
        return this.adversity;
    }

    private final Sides players;

    @Override
    public Sides players() {
        return this.players;
    }

    private final Board board;

    public Board board() {
        return this.board;
    }

    private final IOptionsSupplier optionsSupplier;

    @Override
    public IOptionsSupplier optionsSupplier() {
        return this.optionsSupplier;
    }

    private volatile Table<?, ?, ?> options;

    Context(final Colors side,
            final Board board,
            final Sides players,
            final Adversity adversity,
            final IOptionsSupplier optionsSupplier) {
        this.side = side;
        this.board = board;
        this.players = players;
        this.adversity = adversity;
        this.optionsSupplier = optionsSupplier;
    }

    private Context(final Context context) {
        this(
                context.nextSide(),
                context.board(),
                context.players(),
                context.adversity(),
                context.optionsSupplier());
    }

    @Override
    public Context apply(final IMove iMove) {

        final Move move = (Move) iMove;
        final Colors color = move.color();
        final SortedSet<IPosition> positions = move.positions();
        final Polyominos polyominos = Polyominos.getInstance();
        final PolyominoTranslatedInstance translatedInstance = polyominos.get(positions);

        //        if (color == Blue) System.out.println("-------8<-------");
        //        System.out.println();
        //        System.out.println(color);
        //        System.out.println(translatedInstance); // TODO render(PolyominoTranslatedInstance)
        //        System.out.println();

        Preconditions.checkState(this.side().equals(color));

        return new Context(
                color,
                this.board().apply(color, translatedInstance),
                this.players().apply(color, translatedInstance.type()),
                this.adversity(),
                this.optionsSupplier());
    }

    @Override
    public boolean isTerminal() {
        return TERMINATION_PREDICATE.apply(this);
    }

    @Override
    public Table<?, ?, ?> options() {
        Table<?, ?, ?> value = this.options;
        if (value == null) synchronized (this) {
            if ((value = this.options) == null) this.options = value = this.optionsSupplier().options(this);
        }
        return value;
    }

    public Colors getNextSide(final Colors color) {
        final Colors nextColor = this.adversity().getOpponent(color);
        final Side player = this.players().getAlivePlayer(nextColor);
        return player == null ? this.getNextSide(nextColor) : nextColor;
    }

    @Override
    public Colors nextSide() {
        return this.getNextSide(this.side());
    }

    @Override
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
        return this.players().getDeadOrAlivePlayer(color);
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

    @Override
    public int hashCode() {
        return Objects.hashCode(TERMINATION_PREDICATE, this.optionsSupplier, this.side, this.adversity, this.players, this.board);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof Context);
        final Context that = (Context) object;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public Iterable<Colors> sides() {
        return this.adversity().sides();
    }

}