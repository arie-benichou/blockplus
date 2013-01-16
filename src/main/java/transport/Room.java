
package transport;

import java.util.List;

import transport.events.UserInterface;
import blockplus.model.game.Game;

import com.google.common.base.Objects;

public class Room implements RoomInterface {

    private final int ordinal;
    private final String code;
    private final List<UserInterface> users;
    private final Game game;

    public Room(final int ordinal, final String code, final List<UserInterface> users, final Game game) {
        this.ordinal = ordinal;
        this.code = code;
        this.users = users;
        this.game = game;
    }

    @Override
    public Integer getOrdinal() {
        return this.ordinal;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public List<UserInterface> getUsers() {
        return this.users;
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ordinal", this.ordinal)
                .add("code", this.code)
                .toString();
    }

}