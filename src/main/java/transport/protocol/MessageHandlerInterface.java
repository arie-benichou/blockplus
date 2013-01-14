
package transport.protocol;

import transport.IOinterface;

public interface MessageHandlerInterface {

    Object handle(IOinterface IO, MessageInterface message);

}