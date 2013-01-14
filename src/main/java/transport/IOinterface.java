
package transport;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public interface IOinterface extends OnTextMessage {

    WebSocketServer getServer();

    Connection getConnection();

    void say(String message);

    void say(Object object);

    Integer getRoom();

    void setRoom(Integer ordinal);

}