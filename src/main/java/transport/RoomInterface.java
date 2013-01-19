
package transport;

import java.util.List;

import transport.events.ClientInterface;
import blockplus.model.game.Game;

public interface RoomInterface {

    Integer getOrdinal();

    String getCode();

    List<ClientInterface> getUsers();

    Game getGame();

    ClientInterface getUserToPlay();

}