
package services.resources;

import java.util.Set;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import services.applications.BlockplusApplicationInterface;

import blockplus.model.color.Colors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInterface;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import components.position.Position;
import components.position.PositionInterface;

public class NewPositionsSubmit extends ServerResource {
    
    
    @Get
    public Representation getRepresentation() {
        
        int id = Integer.parseInt(this.getQueryValue("id"));
        String json = this.getQueryValue("positions");
        
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        System.out.println(array);
        System.out.println(array.size());
        Set<PositionInterface> positions = Sets.newLinkedHashSet();
        for (JsonElement jsonElement : array) {
            JsonArray asJsonArray = jsonElement.getAsJsonArray();
            int row = gson.fromJson(asJsonArray.get(0), int.class);
            int column = gson.fromJson(asJsonArray.get(1), int.class);
            positions.add(Position.from(row, column));
        }
        
        PieceInterface piece = PieceComposite.from(id, positions.iterator().next(), positions);
        Move move = new Move(Colors.Green, piece);
        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame();
        // TODO ! check if move is legal
        GameContext newGameContext = game.getInitialContext().apply(move);
        application.setGame(new Game(newGameContext.next()));
        
        this.setStatus(Status.SUCCESS_OK);
        StringRepresentation representation = new StringRepresentation(json);
        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
        
    }
    
    public static void main(String[] args) {
        String json = "[[19,0],[19,1],[19,2],[19,3],[19,4]]";
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        System.out.println(array);
        System.out.println(array.size());
        Set<PositionInterface> positions = Sets.newLinkedHashSet();
        for (JsonElement jsonElement : array) {
            JsonArray asJsonArray = jsonElement.getAsJsonArray();
            int row = gson.fromJson(asJsonArray.get(0), int.class);
            int column = gson.fromJson(asJsonArray.get(1), int.class);
            positions.add(Position.from(row, column));
        }
        System.out.println(positions);
    }

}