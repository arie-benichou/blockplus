
package blockplus.player;

import java.util.List;

public interface PlayersInterface {

    List<Player> getDeadPlayers();

    List<Player> getAlivePlayers();

    boolean hasAlivePlayers();

}