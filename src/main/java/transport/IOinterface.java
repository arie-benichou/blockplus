
package transport;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public interface IOinterface extends OnTextMessage {

    BlockplusServer getServer();

    Connection getConnection();

    //void say(String message);

    //void say(Object object);

    Integer getRoom();

    void setRoom(Integer ordinal);

    void emit(String type, String data);

}