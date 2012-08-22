
package blockplus.model.player;

import java.util.List;
import java.util.Map;



import blockplus.model.color.ColorInterface;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Players implements PlayersInterface {

    private final List<PlayerInterface> deadPlayers;
    private final List<PlayerInterface> alivePlayers;
    private final Map<ColorInterface, PlayerInterface> playerByColor = Maps.newHashMap();

    @SuppressWarnings("all")
    public static Players Players(final List<PlayerInterface> alivePlayers, final List<PlayerInterface> deadPlayers) {
        return new Players(alivePlayers, deadPlayers);
    }

    @SuppressWarnings("all")
    public static Players Players(final List<PlayerInterface> alivePlayers) {
        return new Players(alivePlayers);
    }

    private Players(final List<PlayerInterface> alivePlayers, final List<PlayerInterface> deadPlayers) {
        this.alivePlayers = ImmutableList.copyOf(alivePlayers);
        for (final PlayerInterface player : alivePlayers) {
            this.playerByColor.put(player.getColor(), player);
        }
        this.deadPlayers = ImmutableList.copyOf(deadPlayers);
        for (final PlayerInterface player : deadPlayers) {
            this.playerByColor.put(player.getColor(), player);
        }
    }

    private Players(final List<PlayerInterface> alivePlayers) {
        this.alivePlayers = ImmutableList.copyOf(alivePlayers);
        for (final PlayerInterface player : alivePlayers) {
            this.playerByColor.put(player.getColor(), player);
        }
        this.deadPlayers = ImmutableList.of();
    }

    @Override
    public List<PlayerInterface> getAlivePlayers() {
        return this.alivePlayers;
    }

    @Override
    public List<PlayerInterface> getDeadPlayers() {
        return this.deadPlayers;
    }

    @Override
    public List<PlayerInterface> getAllPlayers() {
        return new ImmutableList.Builder<PlayerInterface>().addAll(this.deadPlayers).addAll(this.alivePlayers).build(); // TODO
    }

    @Override
    public PlayerInterface get(final ColorInterface color) {
        return this.playerByColor.get(color);
    }

    @Override
    public boolean hasAlivePlayers() {
        return !this.getAlivePlayers().isEmpty();
    }

    @Override
    public String toString() {
        final ToStringHelper toStringHelper = Objects.toStringHelper(this);
        for (final PlayerInterface alivePlayer : this.getAlivePlayers()) {
            toStringHelper.addValue("\n" + alivePlayer + "\n");
        }
        return toStringHelper.toString();
    }

    @Override
    public PlayersInterface update(final PlayerInterface newPlayer) {
        final ColorInterface color = newPlayer.getColor();
        final PlayerInterface oldPlayer = this.get(color);
        PlayersInterface newPlayers = null;
        if (newPlayer.isDead()) {
            if (oldPlayer.isAlive()) {
                final List<PlayerInterface> deadPlayers = Lists.newArrayList(this.getDeadPlayers());
                deadPlayers.add(newPlayer);
                final List<PlayerInterface> alivePlayers = Lists.newArrayList();
                for (final PlayerInterface player : this.getAlivePlayers())
                    if (!player.getColor().is(newPlayer.getColor())) alivePlayers.add(player);
                newPlayers = Players.Players(alivePlayers, deadPlayers);
            }
            else newPlayers = this;
        }
        else {
            final List<PlayerInterface> alivePlayers = Lists.newArrayList(this.getAlivePlayers());
            alivePlayers.remove(oldPlayer);
            alivePlayers.add(newPlayer);
            newPlayers = Players.Players(alivePlayers, this.getDeadPlayers());
        }
        return newPlayers;
    }

}