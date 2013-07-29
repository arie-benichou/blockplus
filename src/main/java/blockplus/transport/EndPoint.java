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

import java.io.IOException;

public class EndPoint implements IEndPoint {

    private final BlockplusServer server;

    private Connection connection = null;

    public EndPoint(final BlockplusServer server) {
        this.server = server;
    }

    @Override
    public boolean isOpen() {
        return this.connection != null && this.connection.isOpen();
    }

    @Override
    public void say(final String message) {
        try {
            this.connection.sendMessage(message);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public void emit(final String type, final String data) {
        this.say("{" + "\"" + "type" + "\"" + ":" + "\"" + type + "\"" + "," + "\"" + "data" + "\"" + ":" + data + "}");
    }

    @Override
    public void onClose(final int closeCode, final String message) {
        this.server.disconnect(this);
    }

    @Override
    public void onMessage(final String data) {
        this.server.onMessage(this, data);
    }

}