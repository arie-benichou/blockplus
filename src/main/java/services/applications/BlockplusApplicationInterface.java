
package services.applications;

import java.util.concurrent.atomic.AtomicInteger;

import blockplus.model.game.Game;

public interface BlockplusApplicationInterface {

    //Game getGame();

    //void setGame(Game game);

    Game getGame(String room);

    void setGame(String room, Game game);

    AtomicInteger getCounter();

}