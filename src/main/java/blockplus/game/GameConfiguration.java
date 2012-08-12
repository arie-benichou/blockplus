
package blockplus.game;

import static blockplus.player.Players.Players;

import java.util.List;
import java.util.Set;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.color.ColorInterface;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public final class GameConfiguration {

    public static class Builder {

        private final static Set<ColorInterface> DEFAULT_LEGAL_COLORS = ImmutableSet.of(
                ColorInterface.BLUE,
                ColorInterface.YELLOW,
                ColorInterface.RED,
                ColorInterface.GREEN);

        private final static ImmutableSet.Builder<PieceInterface> DEFAULT_LEGAL_PIECES_BUILDER = ImmutableSet.builder();
        static {
            for (final Pieces piece : Pieces.values()) {
                DEFAULT_LEGAL_PIECES_BUILDER.add(piece.get());
            }

        }
        private final static Set<PieceInterface> DEFAULT_LEGAL_PIECES = DEFAULT_LEGAL_PIECES_BUILDER.build();

        private final static ImmutableList.Builder<Player> DEFAULT_PLAYERS_BUILDER = ImmutableList.builder();
        static {
            for (final ColorInterface color : DEFAULT_LEGAL_COLORS) {
                DEFAULT_PLAYERS_BUILDER.add(new Player(color, PiecesBag.from(DEFAULT_LEGAL_PIECES)));
            }
        }
        private final static List<Player> DEFAULT_PLAYERS = DEFAULT_PLAYERS_BUILDER.build();

        private final static Board<ColorInterface> DEFAULT_BOARD = BoardBuilder.parse(
                new String[][] {
                        { "o..................o" },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "...................." },
                        { "o..................o" }
                });

        private final Set<PieceInterface> pieces;
        private final Set<ColorInterface> colors;

        private final List<Player> players = DEFAULT_PLAYERS;
        private Board<ColorInterface> board = DEFAULT_BOARD;

        public Builder(final Set<ColorInterface> legalColors, final Set<PieceInterface> legalPieces) {
            Preconditions.checkArgument(!legalColors.isEmpty());
            Preconditions.checkArgument(!legalPieces.isEmpty());
            //Preconditions.checkArgument(legalColors.size() <= 4);
            //Preconditions.checkArgument(legalPieces.size() <= 22);
            this.colors = ImmutableSet.copyOf(legalColors);
            this.pieces = ImmutableSet.copyOf(legalPieces);
        }

        public Builder() {
            this(DEFAULT_LEGAL_COLORS, DEFAULT_LEGAL_PIECES);
        }

        public Builder setBoard(final Board<ColorInterface> board) {
            this.board = board;
            return this;
        }

        public Builder addPlayer(final Player player) {
            final ColorInterface color = player.getColor();
            Preconditions.checkState(this.colors.contains(color), "Illegal color: " + color);
            final PiecesBag pieces = player.getAvailablePieces();
            for (final PieceInterface piece : pieces) {
                Preconditions.checkState(this.pieces.contains(piece), "Illegal piece: " + piece);
            }
            for (final Player addedPlayer : this.players) {
                Preconditions.checkState(!addedPlayer.getColor().equals(color));
            }
            this.players.add(player);
            return this;
        }

        public GameConfiguration build() {
            return new GameConfiguration(this);
        }

    }

    public final static GameConfiguration DEFAULT = new GameConfiguration.Builder().build();

    private final Players players;
    private final Board<ColorInterface> board;

    private GameConfiguration(final Builder builder) {
        this.players = Players(builder.players);
        this.board = builder.board;
    }

    public Players getPlayers() {
        return this.players;
    }

    public Board<ColorInterface> getBoard() {
        return this.board;
    }

}