
package blockplus.color;

import static blockplus.model.color.Colors.*;
import static org.junit.Assert.*;


import org.junit.Test;

import blockplus.model.color.Color;
import blockplus.model.color.ColorInterface;

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