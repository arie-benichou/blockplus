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

import static blockplus.model.context.Color.Blue;
import static blockplus.model.context.Color.Green;
import static blockplus.model.context.Color.Red;
import static blockplus.model.context.Color.Yellow;

import java.util.Set;

import blockplus.model.adversity.AdversityOf4;
import blockplus.model.adversity.IAdversity;
import blockplus.model.adversity.AdversityOf4.Builder;
import blockplus.model.board.Board;
import blockplus.model.board.LayerMutationBuilder;
import blockplus.model.entity.Polyomino;
import blockplus.model.entity.projection.PieceInstances;
import blockplus.model.entity.projection.Plane;
import blockplus.model.option.IOptionsSupplier;
import blockplus.model.option.OptionsSupplier;
import blockplus.model.player.Player;
import blockplus.model.player.Players;
import blockplus.model.player.RemainingPieces;

import com.google.common.collect.ImmutableSet;
import components.cells.Positions;

public final class ContextBuilder {

    private final static Set<Color> COLORS = ImmutableSet.of(Blue, Yellow, Red, Green); // TODO asSet();

    private final static Set<Polyomino> LEGAL_PIECES = Polyomino.set();

    private final static RemainingPieces REMAINING_PIECES = new RemainingPieces.Builder().addAll(LEGAL_PIECES).build();

    private final static blockplus.model.player.Players.Builder PLAYERS_BUILDER = new Players.Builder();
    static {
        PLAYERS_BUILDER.add(Blue, Player.from(REMAINING_PIECES));
        PLAYERS_BUILDER.add(Yellow, Player.from(REMAINING_PIECES));
        PLAYERS_BUILDER.add(Red, Player.from(REMAINING_PIECES));
        PLAYERS_BUILDER.add(Green, Player.from(REMAINING_PIECES));
    }
    private final static Players PLAYERS = PLAYERS_BUILDER.build();

    private final static Color SIDE = Blue;

    private final static Builder ADVERSITY_BUILDER = new AdversityOf4.Builder();
    static {
        ADVERSITY_BUILDER.add(Blue);
        ADVERSITY_BUILDER.add(Yellow);
        ADVERSITY_BUILDER.add(Red);
        ADVERSITY_BUILDER.add(Green);
    }

    private final static IAdversity<Color> ADVERSITY = ADVERSITY_BUILDER.build();

    private final static int ROWS = 20;
    private final static int COLUMNS = 20;

    private final static Positions CELL_POSITIONS = new Positions(ROWS, COLUMNS);

    private final static Board BOARD = new Board.Builder(COLORS, CELL_POSITIONS)
            .addLayer(Blue, new LayerMutationBuilder().setLightPositions(CELL_POSITIONS.get(0, 0)).build())
            .addLayer(Yellow, new LayerMutationBuilder().setLightPositions(CELL_POSITIONS.get(0, COLUMNS - 1)).build())
            .addLayer(Red, new LayerMutationBuilder().setLightPositions(CELL_POSITIONS.get(ROWS - 1, COLUMNS - 1)).build())
            .addLayer(Green, new LayerMutationBuilder().setLightPositions(CELL_POSITIONS.get(ROWS - 1, 0)).build())
            .build();

    private final static IOptionsSupplier OPTIONS_SUPPLIER = new OptionsSupplier(new PieceInstances(Plane.from(ROWS, COLUMNS)));

    //private final static MoveHistory MOVE_HISTORY = new MoveHistory();

    private Color side = SIDE;

    public ContextBuilder setSide(final Color side) {
        this.side = side;
        return this;
    }

    private Color getSide() {
        return this.side == null ? SIDE : this.side;
    }

    private IAdversity<Color> adversity = null;

    public ContextBuilder setAdversity(final IAdversity<Color> adversity) {
        this.adversity = adversity;
        return this;
    }

    public IAdversity<Color> getAdversity() {
        return this.adversity == null ? ADVERSITY : this.adversity;
    }

    private Players players = null;

    public ContextBuilder setPlayers(final Players players) {
        this.players = players;
        return this;
    }

    private Players getPlayers() {
        return this.players == null ? PLAYERS : this.players;
    }

    private Board board = null;

    public ContextBuilder setBoard(final Board board) {
        this.board = board;
        return this;
    }

    private Board getBoard() {
        return this.board == null ? BOARD : this.board;
    }

    /*
    private MoveHistory moveHistory = null;

    public ContextBuilder setMoveHistory(final MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
        return this;
    }

    private MoveHistory getMoveHistory() {
        return this.moveHistory == null ? MOVE_HISTORY : this.moveHistory;
    }
    */

    private IOptionsSupplier optionsSupplier = null;

    private IOptionsSupplier getOptionsSupplier() {
        return this.optionsSupplier == null ? OPTIONS_SUPPLIER : this.optionsSupplier;
    }

    public ContextBuilder setOptionsSupplier(final IOptionsSupplier optionsSupplier) {
        this.optionsSupplier = optionsSupplier;
        return this;
    }

    /* TODO add some check
    private final Set<Pieces> legalPieces;
    private final Set<Colors> legalColors;
    public ContextBuilder(final Set<Colors> legalColors, final Set<Pieces> legalPieces) {
        Preconditions.checkArgument(!legalColors.isEmpty());
        Preconditions.checkArgument(!legalPieces.isEmpty());
        this.legalColors = ImmutableSet.copyOf(legalColors);
        this.legalPieces = ImmutableSet.copyOf(legalPieces);
    }
    public ContextBuilder() {
        this(DEFAULT_LEGAL_COLORS, DEFAULT_LEGAL_PIECES);
    }
    */

    public Context build() {
        return new Context(
                this.getSide(),
                //this.getMoveHistory(),
                this.getBoard(),
                this.getPlayers(),
                this.getAdversity(),
                this.getOptionsSupplier());
    }

}