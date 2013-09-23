
package blockplus.imports;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import blockplus.exports.ContextRepresentation;
import blockplus.model.Context;
import blockplus.model.Options;

import com.google.gson.JsonObject;

public class OptionsEncodingTest {

    @Test
    public void testDecode() {

        final Context context = new Context.Builder().build();
        final Options expected = context.options();

        final ContextRepresentation contextRepresentation = new ContextRepresentation(context);
        final JsonObject data = contextRepresentation.encodeOptions().getAsJsonObject();
        final OptionsEncoding optionsEncoding = new OptionsEncoding();
        final Options actual = optionsEncoding.decode(data);

        assertEquals(expected, actual);
    }

}