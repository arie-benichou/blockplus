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

package blockplus.transport.messages;

import blockplus.transport.protocol.IMessage;

import com.google.gson.JsonArray;

public class Messages {

    public IMessage newClient(final String name) {
        return new Client(name);
    }

    public IMessage newGameConnection(final int ordinal) {
        return new GameConnection(ordinal);
    }

    public IMessage newMoveSubmit(final JsonArray positions) {
        return new MoveSubmit(positions);
    }

    public IMessage newPauseResumeGame(final boolean isPaused) {
        return new PauseResumeGame(isPaused);
    }

}