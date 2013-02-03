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

package blockplus.board;

import static blockplus.board.State.Light;
import static blockplus.board.State.None;
import static blockplus.board.State.Other;
import static blockplus.board.State.Self;
import static blockplus.board.State.Shadow;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StateTest {

    @Test
    public void testIs() {

        assertTrue(Light.is(Light));
        assertFalse(Light.is(Shadow));
        assertFalse(Light.is(Self));
        assertFalse(Light.is(Other));
        assertFalse(Light.is(None));

        assertFalse(Shadow.is(Light));
        assertTrue(Shadow.is(Shadow));
        assertFalse(Shadow.is(Self));
        assertFalse(Shadow.is(Other));
        assertFalse(Shadow.is(None));

        assertFalse(Self.is(Light));
        assertFalse(Self.is(Shadow));
        assertTrue(Self.is(Self));
        assertFalse(Self.is(Other));
        assertFalse(Self.is(None));

        assertFalse(Other.is(Light));
        assertFalse(Other.is(Shadow));
        assertFalse(Other.is(Self));
        assertTrue(Other.is(Other));
        assertFalse(Other.is(None));

        assertFalse(None.is(Light));
        assertFalse(None.is(Shadow));
        assertFalse(None.is(Self));
        assertFalse(None.is(Other));
        assertTrue(None.is(None));

    }

}