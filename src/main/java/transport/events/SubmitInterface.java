
package transport.events;

import com.google.gson.JsonArray;

public interface SubmitInterface extends EventInterface {

    Integer getId();

    JsonArray getPositions();

}