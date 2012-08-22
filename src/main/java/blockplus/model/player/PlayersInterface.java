
package blockplus.model.player;

import java.util.List;

import blockplus.model.color.ColorInterface;



public interface PlayersInterface {

    List<PlayerInterface> getDeadPlayers();

    List<PlayerInterface> getAlivePlayers();

    boolean hasAlivePlayers();

    List<PlayerInterface> getAllPlayers();

    PlayerInterface get(ColorInterface color);

    PlayersInterface update(PlayerInterface newPlayer);

}