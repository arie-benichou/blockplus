
package transport;

import transport.events.interfaces.ClientInterface;

import com.google.common.collect.ImmutableList;

public interface RoomInterface<T> {

    Integer getOrdinal();

    String getCode();

    long getTimeStamp();

    ImmutableList<ClientInterface> getClients();

    T getApplication();

    boolean isFull();

    boolean isEmpty();

    int getCapacity();

    RoomInterface<T> connect(ClientInterface client);

    RoomInterface<T> disconnect(ClientInterface client);

    String toJson();

    //ClientInterface getUserToPlay(); TODO

}