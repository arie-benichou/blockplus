
package blockplus.player;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class Players implements PlayersInterface {

    private final List<Player> deadPlayers;
    private final List<Player> alivePlayers;

    @SuppressWarnings("all")
    public static Players Players(final List<Player> alivePlayers, final List<Player> deadPlayers) {
        return new Players(alivePlayers, deadPlayers);
    }

    @SuppressWarnings("all")
    public static Players Players(final List<Player> alivePlayers) {
        return new Players(alivePlayers);
    }

    private Players(final List<Player> alivePlayers, final List<Player> deadPlayers) {
        this.alivePlayers = ImmutableList.copyOf(alivePlayers);
        this.deadPlayers = ImmutableList.copyOf(deadPlayers);
    }

    private Players(final List<Player> alivePlayers) {
        this.alivePlayers = ImmutableList.copyOf(alivePlayers);
        this.deadPlayers = ImmutableList.of();
    }

    @Override
    public List<Player> getAlivePlayers() {
        return this.alivePlayers;
    }

    @Override
    public List<Player> getDeadPlayers() {
        return this.deadPlayers;
    }

    @Override
    public boolean hasAlivePlayers() {
        return !this.getAlivePlayers().isEmpty();
    }

}