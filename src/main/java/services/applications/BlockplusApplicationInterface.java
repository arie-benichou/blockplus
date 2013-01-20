
package services.applications;

import java.util.concurrent.atomic.AtomicInteger;

import blockplus.model.game.BlockplusGame;

public interface BlockplusApplicationInterface {

    //Game getGame();

    //void setGame(Game game);

    BlockplusGame getGame(String room);

    void setGame(String room, BlockplusGame game);

    AtomicInteger getCounter();

}