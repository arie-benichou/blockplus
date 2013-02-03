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

package blockplus.color;

import static blockplus.color.Colors.Blue;
import static blockplus.color.Colors.Green;
import static blockplus.color.Colors.Red;
import static blockplus.color.Colors.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PrimeColorsTest {

    @Test
    public void testSize() {
        {
            final ColorInterface color = Blue;
            final int expected = 1;
            final int actual = color.size();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Yellow;
            final int expected = 1;
            final int actual = color.size();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Red;
            final int expected = 1;
            final int actual = color.size();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Green;
            final int expected = 1;
            final int actual = color.size();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testContains() {
        {
            final ColorInterface color = Blue;
            assertFalse(color.contains(Yellow));
            assertFalse(color.contains(Red));
            assertFalse(color.contains(Green));
            assertTrue(color.contains(color));
        }
        {
            final ColorInterface color = Yellow;
            assertFalse(color.contains(Blue));
            assertFalse(color.contains(Red));
            assertFalse(color.contains(Green));
            assertTrue(color.contains(color));
        }
        {
            final ColorInterface color = Red;
            assertFalse(color.contains(Blue));
            assertFalse(color.contains(Yellow));
            assertFalse(color.contains(Green));
            assertTrue(color.contains(color));
        }
        {
            final ColorInterface color = Green;
            assertFalse(color.contains(Blue));
            assertFalse(color.contains(Yellow));
            assertFalse(color.contains(Red));
            assertTrue(color.contains(color));
        }
    }

    @Test
    public void testCount() {
        {
            final ColorInterface color = Blue;
            final int expected1 = 0;
            final int expected2 = 1;
            final int actual = color.count(color);
            assertEquals(expected1, color.count(Yellow));
            assertEquals(expected1, color.count(Red));
            assertEquals(expected1, color.count(Green));
            assertEquals(expected2, actual);
        }
        {
            final ColorInterface color = Yellow;
            final int expected1 = 0;
            final int expected2 = 1;
            final int actual = color.count(color);
            assertEquals(expected1, color.count(Blue));
            assertEquals(expected1, color.count(Red));
            assertEquals(expected1, color.count(Green));
            assertEquals(expected2, actual);
        }
        {
            final ColorInterface color = Red;
            final int expected1 = 0;
            final int expected2 = 1;
            final int actual = color.count(color);
            assertEquals(expected1, color.count(Blue));
            assertEquals(expected1, color.count(Yellow));
            assertEquals(expected1, color.count(Green));
            assertEquals(expected2, actual);
        }
        {
            final ColorInterface color = Green;
            final int expected1 = 0;
            final int expected2 = 1;
            final int actual = color.count(color);
            assertEquals(expected1, color.count(Blue));
            assertEquals(expected1, color.count(Yellow));
            assertEquals(expected1, color.count(Red));
            assertEquals(expected2, actual);
        }
    }

    @Test
    public void testIs() {
        {
            final ColorInterface color = Blue;
            assertFalse(color.is(Yellow));
            assertFalse(color.is(Red));
            assertFalse(color.is(Green));
            assertTrue(color.is(color));
        }
        {
            final ColorInterface color = Yellow;
            assertFalse(color.is(Blue));
            assertFalse(color.is(Red));
            assertFalse(color.is(Green));
            assertTrue(color.is(color));
        }
        {
            final ColorInterface color = Red;
            assertFalse(color.is(Blue));
            assertFalse(color.is(Yellow));
            assertFalse(color.is(Green));
            assertTrue(color.is(color));
        }
        {
            final ColorInterface color = Green;
            assertFalse(color.is(Blue));
            assertFalse(color.is(Yellow));
            assertFalse(color.is(Red));
            assertTrue(color.is(color));
        }
    }

    @Test
    public void testIsEmpty() {
        {
            final ColorInterface color = Blue;
            assertFalse(color.isEmpty());
        }
        {
            final ColorInterface color = Yellow;
            assertFalse(color.isEmpty());
        }
        {
            final ColorInterface color = Red;
            assertFalse(color.isEmpty());
        }
        {
            final ColorInterface color = Green;
            assertFalse(color.isEmpty());
        }
    }

    @Test
    public void testIsPrime() {
        {
            final ColorInterface color = Blue;
            assertTrue(color.isPrime());
        }
        {
            final ColorInterface color = Yellow;
            assertTrue(color.isPrime());
        }
        {
            final ColorInterface color = Red;
            assertTrue(color.isPrime());
        }
        {
            final ColorInterface color = Green;
            assertTrue(color.isPrime());
        }
    }

    @Test
    public void testRemove() {
        {
            final ColorInterface color = Blue;
            final ColorInterface expected1 = color;
            final ColorInterface expected2 = NullColor.getInstance();
            final ColorInterface newColor1 = color.remove(Yellow);
            final ColorInterface newColor2 = color.remove(Red);
            final ColorInterface newColor3 = color.remove(Green);
            final ColorInterface newColor4 = color.remove(Blue);
            assertEquals(expected1, newColor1);
            assertEquals(expected1, newColor2);
            assertEquals(expected1, newColor3);
            assertEquals(expected2, newColor4);
        }
        {
            final ColorInterface color = Yellow;
            final ColorInterface expected1 = color;
            final ColorInterface expected2 = NullColor.getInstance();
            final ColorInterface newColor1 = color.remove(Blue);
            final ColorInterface newColor2 = color.remove(Red);
            final ColorInterface newColor3 = color.remove(Green);
            final ColorInterface newColor4 = color.remove(Yellow);
            assertEquals(expected1, newColor1);
            assertEquals(expected1, newColor2);
            assertEquals(expected1, newColor3);
            assertEquals(expected2, newColor4);
        }
        {
            final ColorInterface color = Red;
            final ColorInterface expected1 = color;
            final ColorInterface expected2 = NullColor.getInstance();
            final ColorInterface newColor1 = color.remove(Blue);
            final ColorInterface newColor2 = color.remove(Green);
            final ColorInterface newColor3 = color.remove(Yellow);
            final ColorInterface newColor4 = color.remove(Red);
            assertEquals(expected1, newColor1);
            assertEquals(expected1, newColor2);
            assertEquals(expected1, newColor3);
            assertEquals(expected2, newColor4);
        }
        {
            final ColorInterface color = Green;
            final ColorInterface expected1 = color;
            final ColorInterface expected2 = NullColor.getInstance();
            final ColorInterface newColor1 = color.remove(Blue);
            final ColorInterface newColor2 = color.remove(Yellow);
            final ColorInterface newColor3 = color.remove(Red);
            final ColorInterface newColor4 = color.remove(Green);
            assertEquals(expected1, newColor1);
            assertEquals(expected1, newColor2);
            assertEquals(expected1, newColor3);
            assertEquals(expected2, newColor4);
        }
    }

    @Test
    public void testSet() {
        {
            final ColorInterface color = Blue;
            final Set<ColorInterface> expected = Sets.newHashSet();
            expected.add(color);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Yellow;
            final Set<ColorInterface> expected = Sets.newHashSet();
            expected.add(color);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Red;
            final Set<ColorInterface> expected = Sets.newHashSet();
            expected.add(color);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Green;
            final Set<ColorInterface> expected = Sets.newHashSet();
            expected.add(color);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testList() {
        {
            final ColorInterface color = Blue;
            final List<ColorInterface> expected = Lists.newArrayList();
            expected.add(color);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Yellow;
            final List<ColorInterface> expected = Lists.newArrayList();
            expected.add(color);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Red;
            final List<ColorInterface> expected = Lists.newArrayList();
            expected.add(color);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Green;
            final List<ColorInterface> expected = Lists.newArrayList();
            expected.add(color);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
    }

}