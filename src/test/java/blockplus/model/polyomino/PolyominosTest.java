
package blockplus.model.polyomino;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public final class PolyominosTest {

    private final static List<Polyomino> POLYIOMINOS = ImmutableList.copyOf(Polyomino.set());

    private static List<String> load(final String path) throws IOException {

        return Files.readLines(new File(path), Charsets.UTF_8, new LineProcessor<List<String>>() {

            private final List<String> result = Lists.newArrayList();
            private final StringBuilder stringBuilder = new StringBuilder();

            @Override
            public boolean processLine(final String line) throws IOException {
                if (line.isEmpty()) {
                    if (this.stringBuilder.length() > 0)
                        this.stringBuilder.deleteCharAt(this.stringBuilder.length() - 1);
                    this.result.add(this.stringBuilder.toString());
                    this.stringBuilder.setLength(0);
                }
                else this.stringBuilder.append(line).append("\n");
                return true;
            }

            @Override
            public List<String> getResult() {
                return this.result;
            }
        });
    }

    @Test
    public void testProperties() throws IOException {
        final List<String> expectedData = load("src/test/resources/blockplus/model/polyomino/properties");
        assertEquals(expectedData.size(), POLYIOMINOS.size());
        final Iterator<String> iterator = expectedData.iterator();
        for (final Polyomino polyomino : POLYIOMINOS) {
            final String expected = iterator.next();
            final String actual = polyomino.toString();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testInstances() throws IOException {
        final List<String> expected = load("src/test/resources/blockplus/model/polyomino/instances");
        final List<String> actual = Lists.newArrayList();
        for (final Polyomino polyomino : POLYIOMINOS) {
            for (final PolyominoInstance instance : polyomino.get())
                actual.add(instance.toString());
        }
        assertEquals(expected, actual);
    }

}