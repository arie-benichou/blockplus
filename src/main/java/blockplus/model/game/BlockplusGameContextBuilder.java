
package blockplus.model.game;

import static blockplus.model.board.State.Light;
import static blockplus.model.color.Colors.Blue;
import static blockplus.model.color.Colors.Green;
import static blockplus.model.color.Colors.Red;
import static blockplus.model.color.Colors.Yellow;
import static components.position.Position.Position;

import java.util.List;
import java.util.Set;

import serialization.JSONSerializer;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.color.ColorInterface;
import blockplus.model.color.Colors;
import blockplus.model.piece.PieceInstances;
import blockplus.model.piece.PieceInterface;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import components.position.Position;
import components.position.PositionInterface;

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

    public static void main(final String[] args) {
        final int rows = 20, columns = 20;
        BoardLayer layer = new BoardLayer(rows, columns);
        final PieceInterface piece1 = new PieceInstances(1).iterator().next();
        layer = layer.apply(piece1);
        final PieceInterface piece2 = piece1.translateTo(Position.from(1, 1));
        layer = layer.apply(piece2);
        final Set<PositionInterface> positions = layer.getSelves().keySet();
        //System.out.println(positions);
        final JsonElement jsonElement = JSONSerializer.getInstance().toJsonTree(positions);
        //System.out.println(jsonElement);
        final ColorInterface color = Colors.Blue;
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add(color.toString(), jsonElement);
        System.out.println(jsonObject);
    }
}