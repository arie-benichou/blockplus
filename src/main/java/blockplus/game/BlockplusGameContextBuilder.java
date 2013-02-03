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

package blockplus.game;

import static blockplus.board.State.Light;
import static blockplus.color.Colors.Blue;
import static blockplus.color.Colors.Green;
import static blockplus.color.Colors.Red;
import static blockplus.color.Colors.Yellow;
import static components.position.Position.Position;

import java.util.List;
import java.util.Set;

import blockplus.board.Board;
import blockplus.board.BoardLayer;
import blockplus.color.ColorInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;
import blockplus.player.PlayerInterface;
import blockplus.player.Players;
import blockplus.strategy.RandomStrategy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// TODO ! à revoir
public final class BlockplusGameContextBuilder {

    private final static Set<ColorInterface> DEFAULT_LEGAL_COLORS = ImmutableSet.of(
            Blue,
            Yellow,
            Red,
            Green);
    private final static ImmutableSet.Builder<Pieces> DEFAULT_LEGAL_PIECES_BUILDER = ImmutableSet.builder();
    static {
        for (final Pieces piece : Pieces.values()) {
            DEFAULT_LEGAL_PIECES_BUILDER.add(piece);
        }

    }
    private final static Set<Pieces> DEFAULT_LEGAL_PIECES = DEFAULT_LEGAL_PIECES_BUILDER.build();

    private final static List<PlayerInterface> DEFAULT_PLAYERS = Lists.newArrayList();
    static {
        final PiecesBag bagOfPieces = PiecesBag.from(DEFAULT_LEGAL_PIECES);
        final Player bluePlayer = new Player(Blue, bagOfPieces, Yellow, new RandomStrategy());
        final Player yellowPlayer = new Player(Yellow, bagOfPieces, Red, new RandomStrategy());
        final Player redPlayer = new Player(Red, bagOfPieces, Green, new RandomStrategy());
        final Player greenPlayer = new Player(Green, bagOfPieces, Blue, new RandomStrategy());
        DEFAULT_PLAYERS.add(bluePlayer);
        DEFAULT_PLAYERS.add(yellowPlayer);
        DEFAULT_PLAYERS.add(redPlayer);
        DEFAULT_PLAYERS.add(greenPlayer);
    }

    private final Set<Pieces> pieces;
    private final Set<ColorInterface> colors;

    private final List<PlayerInterface> players = DEFAULT_PLAYERS;
    private Board board = null;

    public BlockplusGameContextBuilder(final Set<ColorInterface> legalColors, final Set<Pieces> legalPieces) {
        Preconditions.checkArgument(!legalColors.isEmpty());
        Preconditions.checkArgument(!legalPieces.isEmpty());
        this.colors = ImmutableSet.copyOf(legalColors);
        this.pieces = ImmutableSet.copyOf(legalPieces);
    }

    public BlockplusGameContextBuilder() {
        this(DEFAULT_LEGAL_COLORS, DEFAULT_LEGAL_PIECES);
    }

    public BlockplusGameContextBuilder setBoard(final Board board) {
        this.board = board;
        return this;
    }

    public void addPlayer(final PlayerInterface player) {
        final ColorInterface color = player.getColor();
        Preconditions.checkState(this.colors.contains(color), "Illegal color: " + color);
        final PiecesBag pieces = player.getPieces();
        for (final Pieces piece : pieces) {
            Preconditions.checkState(this.pieces.contains(piece), "Illegal piece: " + piece);
        }
        for (final PlayerInterface addedPlayer : this.players) {
            Preconditions.checkState(!addedPlayer.getColor().equals(color));
        }
        this.players.add(player);
    }

    public void removePlayer(final PlayerInterface player) {
        this.players.remove(player);
    }

    public BlockplusGameContextBuilder setPlayers(final PlayerInterface... players) {
        this.players.clear();
        for (final PlayerInterface player : players) {
            this.addPlayer(player);
        }
        return this;
    }

    public BlockplusGameContextBuilder setPlayers(final List<PlayerInterface> players) {
        this.players.clear();
        for (final PlayerInterface player : players) {
            this.addPlayer(player);
        }
        return this;
    }

    public BlockplusGameContext build() {
        if (this.board == null) {
            final int rows = 20, columns = 20;
            final BoardLayer blueLayer = new BoardLayer(rows, columns).apply(Position(0, 0), Light);
            final BoardLayer yellowLayer = new BoardLayer(rows, columns).apply(Position(0, columns - 1), Light);
            final BoardLayer redLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, columns - 1), Light);
            final BoardLayer greenLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, 0), Light);
            this.board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                    .set(Blue, blueLayer)
                    .set(Yellow, yellowLayer)
                    .set(Red, redLayer)
                    .set(Green, greenLayer)
                    .build();
        }
        return new BlockplusGameContext(this.board, Players.Players(this.players), this.players.get(0).getColor());
    }

}