
package state;

import static blockplus.model.board.State.*;
import static org.junit.Assert.*;

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