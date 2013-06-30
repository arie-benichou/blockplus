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

package transport;

import java.io.IOException;

import transport.protocol.MessageInterface;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;

public class IO implements IOinterface {

    private final BlockplusServer server;

    @Override
    public BlockplusServer getServer() {
        return this.server;
    }

    private Connection connection = null;

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    private void setConnection(final Connection connection) {
        this.connection = connection;
    }

    private Integer game = 0;

    @Override
    public Integer getGame() {
        return this.game;
    }

    @Override
    public void setRoom(final Integer ordinal) {
        this.game = ordinal;
    }

    public IO(final BlockplusServer server) {
        this.server = server;
    }

    private void say(final String message) {
        try {
            this.getConnection().sendMessage(message);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void emit(final String type, final String data) {
        //this.say("{" + "\"" + "type" + "\"" + ":" + "\"" + type + "\"" + "," + "\"" + "data" + "\"" + ":" + "\"" + data + "\"" + "}"); // TODO sprintf
        this.say("{" + "\"" + "type" + "\"" + ":" + "\"" + type + "\"" + "," + "\"" + "data" + "\"" + ":" + data + "}"); // TODO sprintf
    }

    @Override
    public void onOpen(final Connection connection) {
        this.setConnection(connection);
        //this.emit("handshake", "\"" + "Nice to meet you," + "\""); // TODO client side event
    }

    @Override
    public void onClose(final int closeCode, final String message) {
        this.getServer().disconnect(this);
    }

    @Override
    public void onMessage(final String data) {
        MessageInterface message = null;
        try {
            message = this.getServer().decode(data);
        }
        catch (final Exception e) { // TODO MessageConstructionException
            this.say("Message could not be created from " + data + " : " + Throwables.getRootCause(e));
        }
        if (message != null) {
            Object object = null;
            try {
                object = this.getServer().handle(this, message);
            }
            catch (final Exception e) { // TODO EventConstructionException
                this.say("Event could not be created from " + message + " : " + Throwables.getRootCause(e));
            }
            if (object != null) {
                try {
                    this.getServer().getEventBus().post(object);
                    //System.out.println(object);
                }
                catch (final Exception e) { // TODO EventDispatchingException
                    this.say("Event could not be dispatched from " + object + " : " + Throwables.getRootCause(e));
                }
            }
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("server", this.getServer())
                .add("game", this.getGame())
                .add("connection", this.getConnection())
                .toString();
    }
}