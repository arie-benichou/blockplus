
package transport;

import java.util.List;

import transport.events.ClientInterface;
import blockplus.model.color.ColorInterface;
import blockplus.model.color.PrimeColors;
import blockplus.model.game.Game;
import blockplus.model.player.PlayerInterface;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class Room implements RoomInterface {

    // TODO à revoir
    private final ImmutableList<String> SEQUENCE = ImmutableList.of("Blue", "Yellow", "Red", "Green");

    private final int ordinal;
    private final String code;
    private final List<ClientInterface> users;
    private final Game game;

    private final ImmutableMap<PlayerInterface, ClientInterface> userByPlayer;

    public Room(final int ordinal, final String code, final List<ClientInterface> users, final Game game) {

        this.ordinal = ordinal;
        this.code = code;
        this.users = users;
        this.game = game;

        final Builder<PlayerInterface, ClientInterface> builder = new ImmutableMap.Builder<PlayerInterface, ClientInterface>();

        // TODO demander au joueur qui rejoint une room de choisir une couleur
        int n = 0;
        for (final ClientInterface user : users) {
            final ColorInterface color = PrimeColors.get(this.SEQUENCE.get(n));
            final PlayerInterface player = game.getInitialContext().getPlayers().get(color);
            builder.put(player, user);
            ++n;
        }

        this.userByPlayer = builder.build();
        System.out.println(this.userByPlayer);

    }

    @Override
    public Integer getOrdinal() {
        return this.ordinal;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public List<ClientInterface> getUsers() {
        return this.users;
    }

    @Override
    public ClientInterface getUserToPlay() {
        final ColorInterface colorToPlay = this.getGame().getInitialContext().getColor();
        final PlayerInterface player = this.getGame().getInitialContext().getPlayers().get(colorToPlay);
        return this.userByPlayer.get(player);
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ordinal", this.ordinal)
                .add("code", this.code)
                .toString();
    }

}