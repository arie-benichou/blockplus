
package transport;

import java.io.IOException;

import transport.protocol.MessageInterface;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;

public final class IO implements IOinterface {

    private final WebSocketServer server;

    @Override
    public WebSocketServer getServer() {
        return this.server;
    }

    private transient Connection connection = null;

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    private void setConnection(final Connection connection) {
        this.connection = connection;
    }

    private transient Integer room = 0;

    @Override
    public Integer getRoom() {
        return this.room;
    }

    @Override
    public void setRoom(final Integer ordinal) {
        this.room = ordinal;
    }

    public IO(final WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void say(final String message) {
        try {
            this.getConnection().sendMessage(message);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void say(final Object object) {
        this.say(object.toString());
    }

    @Override
    public void onOpen(final Connection connection) {
        this.setConnection(connection);
        this.say("Who is there ?"); // TODO client side event
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
                .add("room", this.getRoom())
                .add("connection", this.getConnection())
                .toString();
    }
}