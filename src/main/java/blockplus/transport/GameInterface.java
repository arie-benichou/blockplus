/*
 * Copyright 2012-2013 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.transport;


import blockplus.transport.events.interfaces.ClientInterface;

import com.google.common.collect.ImmutableList;

// TODO revoir l'interface
public interface GameInterface<T> {

    Integer getOrdinal();

    String getCode();

    long getTimeStamp();

    ImmutableList<ClientInterface> getClients();

    T getContext();

    boolean isFull();

    boolean isEmpty();

    int getCapacity();

    GameInterface<T> connect(ClientInterface client);

    GameInterface<T> disconnect(ClientInterface client);

    String toJson();

}