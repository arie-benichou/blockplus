
package blockplus.color;

import static blockplus.model.color.Colors.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;


import org.junit.Test;

import blockplus.model.color.ColorInterface;
import blockplus.model.color.NullColor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class NullColorTest {

    @Test
    public void testGetInstance() {
        {
            final ColorInterface instance1 = NullColor.getInstance();
            final ColorInterface instance2 = NullColor.getInstance();
            final ColorInterface instance3 = Black;
            assertSame(instance1, instance2);
            assertSame(instance2, instance3);
        }
    }

    @Test
    public void testSize() {
        {
            final int expected = 0;
            final int actual = Black.size();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testCount() {
        {
            final int expected = 0;
            final int actual = Black.count(Blue);
            assertEquals(expected, actual);
        }
        {
            final int expected = 0;
            final int actual = Black.count(Yellow);
            assertEquals(expected, actual);
        }
        {
            final int expected = 0;
            final int actual = Black.count(Red);
            assertEquals(expected, actual);
        }
        {
            final int expected = 0;
            final int actual = Black.count(Green);
            assertEquals(expected, actual);
        }
        {
            final int expected = 0;
            final int actual = Black.count(Black);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testContains() {
        {
            final ColorInterface color = Black;
            assertFalse(color.contains(Blue));
            assertFalse(color.contains(Yellow));
            assertFalse(color.contains(Red));
            assertFalse(color.contains(Green));
            assertFalse(color.contains(color));
        }
    }

    @Test
    public void testIs() {
        {
            final ColorInterface color = Black;
            assertFalse(color.is(Blue));
            assertFalse(color.is(Yellow));
            assertFalse(color.is(Red));
            assertFalse(color.is(Green));
            assertTrue(color.is(color));
        }
    }

    @Test
    public void testIsEmpty() {
        {
            final ColorInterface color = Black;
            assertTrue(color.isEmpty());
        }
    }

    @Test
    public void testIsPrime() {
        {
            final ColorInterface color = Black;
            assertFalse(color.isPrime());
        }
    }

    @Test
    public void testRemove() {
        {
            final ColorInterface color = Black;
            final ColorInterface expected = color;
            final ColorInterface newColor1 = color.remove(Yellow);
            final ColorInterface newColor2 = color.remove(Red);
            final ColorInterface newColor3 = color.remove(Green);
            final ColorInterface newColor4 = color.remove(Blue);
            final ColorInterface newColor5 = color.remove(Black);
            assertSame(expected, newColor1);
            assertSame(expected, newColor2);
            assertSame(expected, newColor3);
            assertSame(expected, newColor4);
            assertSame(expected, newColor5);
        }
    }

    @Test
    public void testSet() {
        {
            final ColorInterface color = Black;
            final Set<ColorInterface> expected = Sets.newHashSet();
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testList() {
        {
            final ColorInterface color = Black;
            final List<ColorInterface> expected = Lists.newArrayList();
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
    }

}
