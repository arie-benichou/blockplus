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

import static blockplus.color.Colors.Black;
import static blockplus.color.Colors.Blue;
import static blockplus.color.Colors.Green;
import static blockplus.color.Colors.Red;
import static blockplus.color.Colors.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ColorTest {

    private ColorInterface color;

    @Before
    public void setUp() throws Exception {
        this.color = new Color.Builder()
                .add(Yellow)
                .add(Red)
                .add(Green)
                .add(Blue)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        this.color = null;
    }

    @Test
    public void testToString() {
        final String expected = "Color{[Blue, Yellow, Red, Green]}";
        final String actual = this.color.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        final int expected = "Color{[Blue, Yellow, Red, Green]}".hashCode();
        final int actual = this.color.toString().hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.color.equals(null));
        assertFalse(this.color.equals(new Object()));
        assertTrue(this.color.equals(this.color));
        {
            final ColorInterface color = new Color.Builder()
                    .add(Red)
                    .add(Blue)
                    .add(Green)
                    .add(Yellow)
                    .build();
            assertTrue(this.color.equals(color));
            assertTrue(this.color.equals(this.color));
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Blue)
                    .add(Green)
                    .add(Yellow)
                    .build();
            assertFalse(this.color.equals(color));
            assertFalse(color.equals(this.color));
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Blue)
                    .add(Blue)
                    .build();
            assertFalse(this.color.equals(color));
            assertFalse(color.equals(Blue));
            assertFalse(Blue.equals(color));
        }
    }

    @Test
    public void testSize() {
        {
            final ColorInterface color = this.color;
            final int expected = 4;
            final int actual = color.size();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testContains() {
        {
            final ColorInterface color = this.color;
            assertTrue(color.contains(Yellow));
            assertTrue(color.contains(Red));
            assertTrue(color.contains(Green));
            assertFalse(color.contains(color));
        }
    }

    @Test
    public void testCount() {
        {
            final ColorInterface color = this.color;
            final int expected = 0;
            assertEquals(expected, color.count(Black));
        }
        {
            final ColorInterface color = this.color;
            final int expected = 1;
            assertEquals(expected, color.count(Blue));
            assertEquals(expected, color.count(Yellow));
            assertEquals(expected, color.count(Red));
            assertEquals(expected, color.count(Green));
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Blue)
                    .add(Yellow)
                    .add(Yellow)
                    .build();
            final int expected1 = 0;
            final int expected2 = 1;
            final int expected3 = 2;
            assertEquals(expected1, color.count(Red));
            assertEquals(expected1, color.count(Green));
            assertEquals(expected2, color.count(Blue));
            assertEquals(expected3, color.count(Yellow));
        }
        {
            final ColorInterface color = this.color;
            final ColorInterface newColor = new Color.Builder()
                    .add(Blue)
                    .add(Yellow)
                    .build();
            final int expected = 0; // TODO ? expect 1
            assertEquals(expected, color.count(newColor));
        }

    }

    @Test
    public void testIs() {
        {
            final ColorInterface color = this.color;
            assertFalse(color.is(Yellow));
            assertFalse(color.is(Red));
            assertFalse(color.is(Green));
            assertFalse(color.is(Blue));
            assertTrue(color.is(this.color));
            assertTrue(this.color.is(color));
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Yellow)
                    .add(Red)
                    .add(Green)
                    .add(Blue)
                    .build();
            assertTrue(color.is(this.color));
            assertTrue(this.color.is(color));
        }
    }

    @Test
    public void testIsEmpty() {
        {
            final ColorInterface color = this.color;
            assertFalse(color.isEmpty());
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Black)
                    .add(Black)
                    .add(Black)
                    .add(Black)
                    .build();
            assertTrue(color.isEmpty());
        }
    }

    @Test
    public void testIsPrime() {
        {
            final ColorInterface color = this.color;
            assertFalse(color.isPrime());
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Blue)
                    .add(Blue)
                    .build();
            assertFalse(color.isPrime());
        }
        {
            final ColorInterface color = new Color.Builder()
                    .add(Blue)
                    .build();
            assertTrue(color.isPrime());
        }
    }

    @Test
    public void testRemove() {
        {
            final ColorInterface color = this.color;
            final ColorInterface expected = color;
            final ColorInterface actual = color.remove(Black);
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = this.color;
            final ColorInterface expected = color; // TODO ? expect Black
            final ColorInterface actual = color.remove(color);
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = this.color;
            final ColorInterface expected = new Color.Builder()
                    .add(Yellow)
                    .add(Red)
                    .add(Green)
                    .build();
            final ColorInterface actual = color.remove(Blue);
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = this.color;
            final ColorInterface expected = new Color.Builder()
                    .add(Yellow)
                    .add(Green)
                    .build();
            ColorInterface actual = color;
            actual = actual.remove(Blue);
            actual = actual.remove(Red);
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = this.color;
            final ColorInterface expected = new Color.Builder()
                    .add(Green)
                    .build();
            ColorInterface actual = color;
            actual = actual.remove(Blue);
            actual = actual.remove(Red);
            actual = actual.remove(Yellow);
            assertSame(expected, actual);
        }
        {
            final ColorInterface color = this.color;
            final ColorInterface expected1 = new Color.Builder().build();
            final ColorInterface expected2 = Black;
            ColorInterface actual = color;
            actual = actual.remove(Blue);
            actual = actual.remove(Red);
            actual = actual.remove(Yellow);
            actual = actual.remove(Green);
            assertSame(expected1, actual);
            assertSame(expected2, actual);
        }
    }

    @Test
    public void testSet() {
        {
            final ColorInterface color = this.color;
            final Set<ColorInterface> expected = Sets.newHashSet(Blue, Yellow, Red, Green);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).build();
            final Set<ColorInterface> expected = Sets.newHashSet(Blue);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).add(Blue).build();
            final Set<ColorInterface> expected = Sets.newHashSet(Blue);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).add(Yellow).build();
            final Set<ColorInterface> expected = Sets.newHashSet(Blue, Yellow);
            final Set<ColorInterface> actual = color.set();
            assertEquals(expected, actual);
        }
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
            final ColorInterface color = this.color;
            final List<ColorInterface> expected = Lists.newArrayList(Blue, Yellow, Red, Green);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).build();
            final List<ColorInterface> expected = Lists.newArrayList(Blue);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).add(Blue).build();
            final List<ColorInterface> expected = Lists.newArrayList(Blue, Blue);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).add(Yellow).build();
            final List<ColorInterface> expected = Lists.newArrayList(Blue, Yellow);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = new Color.Builder().add(Blue).add(Yellow).add(Blue).build();
            final List<ColorInterface> expected = Lists.newArrayList(Blue, Blue, Yellow);
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
        {
            final ColorInterface color = Black;
            final List<ColorInterface> expected = Lists.newArrayList();
            final List<ColorInterface> actual = color.list();
            assertEquals(expected, actual);
        }
    }

}
