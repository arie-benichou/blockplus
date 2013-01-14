
package transport.protocol;

import com.google.gson.JsonObject;

public interface MessageInterface {

    String getType();

    JsonObject getData();

}
