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
import static blockplus.board.State.Light;
import static components.position.Position.Position;

import java.util.Set;

import blockplus.Color;
import blockplus.adversity.Adversity;
import blockplus.adversity.Adversity.Builder;
import blockplus.board.Board;
import blockplus.board.BoardLayer;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.collect.ImmutableSet;

public final class ContextBuilder {

    private final static Set<Color> COLORS = ImmutableSet.of(Blue, Yellow, Red, Green);

    private final static ImmutableSet.Builder<Pieces> LEGAL_PIECES_BUILDER = ImmutableSet.builder();
    static {
        for (final Pieces piece : Pieces.values()) {
            LEGAL_PIECES_BUILDER.add(piece);
        }
    }

    private final static Set<Pieces> LEGAL_PIECES = LEGAL_PIECES_BUILDER.build();

    private final static PiecesBag BAG_OF_PIECES = PiecesBag.from(LEGAL_PIECES);

    private final static blockplus.player.Players.Builder PLAYERS_BUILDER = new Players.Builder();
    static {
        PLAYERS_BUILDER.add(new Player(Blue, BAG_OF_PIECES));
        PLAYERS_BUILDER.add(new Player(Yellow, BAG_OF_PIECES));
        PLAYERS_BUILDER.add(new Player(Red, BAG_OF_PIECES));
        PLAYERS_BUILDER.add(new Player(Green, BAG_OF_PIECES));
    }
    private final static Players PLAYERS = PLAYERS_BUILDER.build();

    private final static Color CURRENT_SIDE = Blue;

    private final static Builder ADVERSITY_BUILDER = new Adversity.Builder();
    static {
        ADVERSITY_BUILDER.add(Blue);
        ADVERSITY_BUILDER.add(Yellow);
        ADVERSITY_BUILDER.add(Red);
        ADVERSITY_BUILDER.add(Green);
    }

    private final static Adversity ADVERSITY = ADVERSITY_BUILDER.build();

    private final static int ROWS = 20;
    private final static int COLUMNS = 20;

    private final static Board BOARD = new Board.Builder(COLORS, ROWS, COLUMNS)
            .set(Blue, new BoardLayer(ROWS, COLUMNS).apply(Position(0, 0), Light))
            .set(Yellow, new BoardLayer(ROWS, COLUMNS).apply(Position(0, COLUMNS - 1), Light))
            .set(Red, new BoardLayer(ROWS, COLUMNS).apply(Position(ROWS - 1, COLUMNS - 1), Light))
            .set(Green, new BoardLayer(ROWS, COLUMNS).apply(Position(ROWS - 1, 0), Light))
            .build();

    private Color currentSide = CURRENT_SIDE;

    public ContextBuilder setCurrentSide(final Color side) {
        this.currentSide = side;
        return this;
    }

    private Color getCurrentSide() {
        return (this.currentSide == null) ? CURRENT_SIDE : this.currentSide;
    }

    private Adversity adversity = null;

    public ContextBuilder setAdversity(final Adversity adversity) {
        this.adversity = adversity;
        return this;
    }

    public Adversity getAdversity() {
        return (this.adversity == null) ? ADVERSITY : this.adversity;
    }

    private Players players = null;

    public ContextBuilder setPlayers(final Players players) {
        this.players = players;
        return this;
    }

    private Players getPlayers() {
        return (this.players == null) ? PLAYERS : this.players;
    }

    private Board board = null;

    public ContextBuilder setBoard(final Board board) {
        this.board = board;
        return this;
    }

    private Board getBoard() {
        return (this.board == null) ? BOARD : this.board;
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
        return new Context(this.getCurrentSide(), this.getBoard(), this.getPlayers(), this.getAdversity());
    }

}