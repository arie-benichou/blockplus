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

package blockplus.model.context;

import blockplus.model.adversity.IAdversity;
import blockplus.model.board.Board;
import blockplus.model.entity.Polyomino;
import blockplus.model.entity.Polyominos;
import blockplus.model.entity.entity.Entities;
import blockplus.model.entity.entity.IEntity;
import blockplus.model.entity.entity.NullEntity;
import blockplus.model.move.IMove;
import blockplus.model.move.Move;
import blockplus.model.option.IOptionsSupplier;
import blockplus.model.player.Player;
import blockplus.model.player.Players;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Table;
import components.cells.Positions;

public final class Context implements IContext<Color> {

    // TODO !!! inject
    public final static Entities ENTITIES = new Entities(new Positions(20, 20));

    // TODO inject
    private final static Predicate<Context> TERMINATION_PREDICATE = new Predicate<Context>() {

        @Override
        public boolean apply(final Context context) {
            return !context.getPlayers().hasAlivePlayer();
        }

    };

    /*
    private final MoveHistory moveHistory;

    public MoveHistory getMoveHistory() {
        return this.moveHistory;
    }
    */

    private final Color side;

    @Override
    public Color getSide() {
        return this.side;
    }

    private final IAdversity<Color> adversity;

    @Override
    public IAdversity<Color> getAdversity() {
        return this.adversity;
    }

    private final Players players;

    @Override
    public Players getPlayers() {
        return this.players;
    }

    private final Board board;

    public Board getBoard() {
        return this.board;
    }

    private final IOptionsSupplier optionsSupplier;

    @Override
    public IOptionsSupplier getOptionsSupplier() {
        return this.optionsSupplier;
    }

    private volatile Table<?, ?, ?> options;

    Context(final Color side,
            //final MoveHistory moveHistory,
            final Board board,
            final Players players,
            final IAdversity<Color> adversity,
            final IOptionsSupplier optionsSupplier) {
        this.side = side;
        //this.moveHistory = moveHistory;
        this.board = board;
        this.players = players;
        this.adversity = adversity;
        this.optionsSupplier = optionsSupplier;
    }

    private Context(final Context context) {
        this(
                context.getNextSide(),
                //context.getMoveHistory(),
                context.getBoard(),
                context.getPlayers(),
                context.getAdversity(),
                context.getOptionsSupplier());
    }

    @Override
    public Context apply(final IMove iMove) {

        final Move move = (Move) iMove;

        final Color color = this.getSide();

        IEntity entity = move.getEntity();
        if (entity == null) entity = new NullEntity();
        final Polyomino polyomino = Polyominos.getInstance().getByTypeId(entity.type());

        return new Context(
                color,
                this.getBoard().apply(color, entity),
                this.getPlayers().apply(color, polyomino),
                this.getAdversity(),
                this.getOptionsSupplier());
    }

    @Override
    public boolean isTerminal() {
        return TERMINATION_PREDICATE.apply(this);
    }

    @Override
    public Table<?, ?, ?> options() {
        Table<?, ?, ?> value = this.options;
        if (value == null) synchronized (this) {
            if ((value = this.options) == null) this.options = value = this.getOptionsSupplier().options(this);
        }
        return value;
    }

    public Color getNextSide(final Color color) {
        final Color nextColor = this.getAdversity().getOpponent(color);
        final Player player = this.getPlayers().getAlivePlayer(nextColor);
        return player == null ? this.getNextSide(nextColor) : nextColor;
    }

    @Override
    public Color getNextSide() {
        return this.getNextSide(this.getSide());
    }

    @Override
    public Context forward() {
        if (this.isTerminal()) return this;
        Context nextContext = new Context(this);
        if (nextContext.options().isEmpty()) nextContext = nextContext.apply(new Move(this.getSide(), null)).forward();
        return nextContext;
    }

    public Player getPlayer(final Color color) {
        return this.getPlayers().getDeadOrAlivePlayer(color);
    }

    public Player getPlayer() {
        return this.getPlayer(this.getSide());
    }

    // TODO memoize
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getBoard())
                .toString();
    }

    // TODO memoize
    @Override
    public int hashCode() {
        return Objects.hashCode(TERMINATION_PREDICATE, this.optionsSupplier, this.side, this.adversity, this.players, this.board);
    }

    // TODO à revoir
    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof Context);
        final Context that = (Context) object;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public Iterable<Color> sides() {
        return this.getAdversity().sides();
    }

}