
package transport.events.interfaces;

import com.google.gson.JsonObject;

public interface RoomReconnectionInterface extends EventInterface {

    JsonObject getLink();

}