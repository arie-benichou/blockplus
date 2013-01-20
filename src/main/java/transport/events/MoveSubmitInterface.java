
package transport.events;

import com.google.gson.JsonArray;

public interface MoveSubmitInterface extends EventInterface {

    Integer getId();

    JsonArray getPositions();

}