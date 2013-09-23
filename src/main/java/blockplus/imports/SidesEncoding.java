
package blockplus.imports;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.exports.ContextRepresentation;
import blockplus.model.Colors;
import blockplus.model.Context;
import blockplus.model.Pieces;
import blockplus.model.Sides;
import blockplus.model.Sides.Builder;
import blockplus.model.Sides.Side;
import blockplus.model.SidesOrdering;
import blockplus.model.polyomino.Polyomino;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SidesEncoding {

    private final static SidesOrdering SIDES_ORDERING = new SidesOrdering.Builder().add(Blue).add(Yellow).add(Red).add(Green).build();

    public SidesEncoding() {}

    public Sides decode(final JsonObject data) {
        final Builder sidesBuilder = new Sides.Builder(SIDES_ORDERING);
        final Set<Entry<String, JsonElement>> entrySet = data.entrySet();
        final Map<Colors, Integer> tmp = Maps.newTreeMap();
        for (final Entry<String, JsonElement> entry : entrySet) {
            tmp.put(Colors.valueOf(entry.getKey()), entry.getValue().getAsInt());
        }
        //System.out.println(tmp);
        //System.out.println(entrySet);
        for (final Entry<Colors, Integer> entry : tmp.entrySet()) {
            //final String key = entry.getKey();
            //System.out.println("*" + entry.getKey());
            int code = entry.getValue();
            //int code = 3145727;
            final int digits = (int) Math.floor(1 + Math.log(code) / Math.log(2));
            final blockplus.model.Pieces.Builder piecesBuilder = new Pieces.Builder();
            piecesBuilder.add(Polyomino._0); // FIXME !!! à revoir
            for (int i = 1; i < digits; ++i, code = code >> 1)
                //if ((code & 1) == 1) piecesBuilder.add(Polyomino.valueOf("_" + (digits - i)));
                if ((code & 1) == 1) {
                    final Polyomino polyomino = Polyomino.valueOf("_" + (digits - i));
                    //System.out.println(polyomino.ordinal());
                    piecesBuilder.add(polyomino);
                }
                else {
                    final Polyomino polyomino = Polyomino.valueOf("_" + (digits - i));
                    //System.out.println(polyomino.ordinal());
                }
            sidesBuilder.add(Side.with(piecesBuilder.build()));
            //break;
        }
        return sidesBuilder.build();
    }

    // TODO extract tests
    public static void main(final String[] args) {
        final SidesEncoding encoding = new SidesEncoding();
        final Context context = new Context.Builder().build();
        final Sides expected = context.sides();
        final ContextRepresentation contextRepresentation = new ContextRepresentation(context);
        final JsonObject data = contextRepresentation.encodePieces().getAsJsonObject();
        final Sides actual = encoding.decode(data);
        //System.out.println(actual);
        //System.out.println(expected);
        System.out.println(actual.equals(expected));
    }

}