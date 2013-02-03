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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ColorBuilderTest {

    @Test
    public void test() {
        {
            final Color.Builder builder = new Color.Builder();
            final ColorInterface expected = Black;
            final ColorInterface actual = builder.add(Black).build();
            assertSame(expected, actual);
        }
        {
            final Color.Builder builder = new Color.Builder();
            final ColorInterface expected = Blue;
            final ColorInterface actual = builder.add(Blue).build();
            assertSame(expected, actual);
        }
        {
            final Color.Builder builder = new Color.Builder();
            final ColorInterface expected = Blue;
            final ColorInterface actual = builder
                    .add(Black)
                    .add(Blue)
                    .add(Black)
                    .build();
            assertSame(expected, actual);
        }
        {
            final Color.Builder builder = new Color.Builder();
            final ColorInterface unExpected = Blue;
            final ColorInterface actual = builder
                    .add(Blue)
                    .add(Blue)
                    .build();
            assertNotSame(unExpected, actual);
        }
        {
            final ColorInterface expected = new Color.Builder()
                    .add(Blue)
                    .add(Blue)
                    .add(Blue)
                    .add(Blue)
                    .build();

            final ColorInterface color1 = new Color.Builder()
                    .add(Blue)
                    .add(Blue)
                    .build();

            final ColorInterface color2 = new Color.Builder()
                    .add(Blue)
                    .add(Blue)
                    .build();

            final ColorInterface actual = new Color.Builder().add(color1).add(color2).build();
            assertEquals(expected, actual);
        }
    }

}