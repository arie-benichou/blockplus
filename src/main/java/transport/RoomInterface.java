
package transport;

import java.util.List;

import transport.events.UserInterface;
import blockplus.model.game.Game;

public interface RoomInterface {

    Integer getOrdinal();

    String getCode();

    List<UserInterface> getUsers();

    Game getGame();

}