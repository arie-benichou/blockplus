
package blockplus.imports;

import static components.cells.Positions.Position;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import blockplus.model.Board;
import blockplus.model.Colors;
import blockplus.model.polyomino.Polyomino;
import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.Polyominos;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BoardEncodingTest {

    @Test
    public void testDecode() {

        Board expected = Board.of(20, 20);
        final Polyominos polyominos = Polyominos.getInstance();
        final PolyominoInstance unit = Polyomino._1.get().iterator().next();

        expected = expected.apply(Colors.Blue, polyominos.get(unit.apply(Position(0, 0))));
        expected = expected.apply(Colors.Yellow, polyominos.get(unit.apply(Position(0, 19))));
        expected = expected.apply(Colors.Red, polyominos.get(unit.apply(Position(19, 19))));
        expected = expected.apply(Colors.Green, polyominos.get(unit.apply(Position(19, 0))));

        final String json = "{'dimension':{'rows':20,'columns':20},'cells':{'Blue':[0],'Yellow':[19],'Red':[399],'Green':[380]}}";
        final JsonParser jsonParser = new JsonParser();
        final JsonElement jsonElement = jsonParser.parse(json);
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final BoardEncoding boardEncoding = new BoardEncoding();
        final Board actual = boardEncoding.decode(jsonObject);

        assertEquals(expected, actual);
    }

}