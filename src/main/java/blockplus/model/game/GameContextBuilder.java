
package blockplus.model.game;

import static blockplus.model.board.State.*;
import static blockplus.model.color.Colors.*;
import static components.position.Position.*;

import java.util.List;
import java.util.Set;

import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.color.ColorInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;
import blockplus.model.player.Players;
import blockplus.model.strategy.RandomStrategy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// TODO ! à revoir
public final class GameContextBuilder {

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

    public GameContextBuilder(final Set<ColorInterface> legalColors, final Set<Pieces> legalPieces) {
        Preconditions.checkArgument(!legalColors.isEmpty());
        Preconditions.checkArgument(!legalPieces.isEmpty());
        this.colors = ImmutableSet.copyOf(legalColors);
        this.pieces = ImmutableSet.copyOf(legalPieces);
    }

    public GameContextBuilder() {
        this(DEFAULT_LEGAL_COLORS, DEFAULT_LEGAL_PIECES);
    }

    public GameContextBuilder setBoard(final Board board) {
        this.board = board;
        return this;
    }

    private void addPlayer(final PlayerInterface player) {
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

    public GameContextBuilder setPlayers(final PlayerInterface... players) {
        this.players.clear();
        for (final PlayerInterface player : players) {
            this.addPlayer(player);
        }
        return this;
    }

    public GameContextBuilder setPlayers(final List<PlayerInterface> players) {
        this.players.clear();
        for (final PlayerInterface player : players) {
            this.addPlayer(player);
        }
        return this;
    }

    public GameContext build() {
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
        return new GameContext(this.board, Players.Players(this.players), this.players.get(0).getColor());
    }

}