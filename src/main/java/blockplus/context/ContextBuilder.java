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

package blockplus.context;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static components.position.Position.Position;
import interfaces.adversity.AdversityInterface;

import java.util.Set;

import blockplus.Color;
import blockplus.adversity.AdversityOf4;
import blockplus.adversity.AdversityOf4.Builder;
import blockplus.board.Board;
import blockplus.board.LayerMutationBuilder;
import blockplus.piece.PieceType;
import blockplus.piece.Pieces;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.collect.ImmutableSet;

public final class ContextBuilder {

    private final static Set<Color> COLORS = ImmutableSet.of(Blue, Yellow, Red, Green); // TODO asSet();

    private final static Set<PieceType> LEGAL_PIECES = PieceType.asSet();

    private final static Pieces BAG_OF_PIECES = new Pieces.Builder().addAll(LEGAL_PIECES).build();

    private final static blockplus.player.Players.Builder PLAYERS_BUILDER = new Players.Builder();
    static {
        PLAYERS_BUILDER.add(new Player(Blue, BAG_OF_PIECES));
        PLAYERS_BUILDER.add(new Player(Yellow, BAG_OF_PIECES));
        PLAYERS_BUILDER.add(new Player(Red, BAG_OF_PIECES));
        PLAYERS_BUILDER.add(new Player(Green, BAG_OF_PIECES));
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

    private final static AdversityInterface<Color> ADVERSITY = ADVERSITY_BUILDER.build();

    private final static int ROWS = 20;
    private final static int COLUMNS = 20;

    private final static Board BOARD = new Board.Builder(COLORS, ROWS, COLUMNS)
            .addLayer(Blue, new LayerMutationBuilder().setLightPositions(Position(0, 0)).build())
            .addLayer(Yellow, new LayerMutationBuilder().setLightPositions(Position(0, COLUMNS - 1)).build())
            .addLayer(Red, new LayerMutationBuilder().setLightPositions(Position(ROWS - 1, COLUMNS - 1)).build())
            .addLayer(Green, new LayerMutationBuilder().setLightPositions(Position(ROWS - 1, 0)).build())
            .build();

    //private final static MoveHistory MOVE_HISTORY = new MoveHistory();

    private Color side = SIDE;

    public ContextBuilder setSide(final Color side) {
        this.side = side;
        return this;
    }

    private Color getSide() {
        return this.side == null ? SIDE : this.side;
    }

    private AdversityInterface<Color> adversity = null;

    public ContextBuilder setAdversity(final AdversityInterface<Color> adversity) {
        this.adversity = adversity;
        return this;
    }

    public AdversityInterface<Color> getAdversity() {
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
                this.getAdversity());
    }

}