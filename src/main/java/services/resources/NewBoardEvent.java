
package services.resources;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.gson.Gson;

import serialization.CellEncoding;
import serialization.JSONSerializer;
import serialization.PiecesBagEncoding;
import services.applications.BlockplusApplicationInterface;
import blockplus.model.board.Board;
import blockplus.model.color.ColorInterface;
import blockplus.model.color.Colors;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.move.Move;
import blockplus.model.piece.PieceComponent;
import blockplus.model.piece.PieceComposite;
import blockplus.model.piece.PieceInstances;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;

import components.board.BoardInterface;
import components.position.PositionInterface;

// TODO à continuer...
public class NewBoardEvent extends ServerResource {

    private final static MediaType TEXT_EVENT_STREAM = new MediaType("text/event-stream");

    public NewBoardEvent() {
        super();
    }

    public NewBoardEvent(final Context context, final Request request, final Response response) {
        this.getVariants().add(new Variant(TEXT_EVENT_STREAM));
    }

    @Get
    public Representation getRepresentation() {

        this.setStatus(Status.SUCCESS_OK);

        final BlockplusApplicationInterface application = (BlockplusApplicationInterface) this.getApplication();
        final Game game = application.getGame();

        final GameContext initialContext = game.getInitialContext();
        boolean isGameNotOver = initialContext.hasNext();

        final ColorInterface color = initialContext.getColor().iterator().next();

        String playablePositionsData = null;
        GameContext newGameContext;
        
        if (color.is(Colors.Green)) {
            newGameContext = initialContext;
            List<Move> options = initialContext.options();
            
            if(options.size() == 1) {
                Move move = new Move(color, Pieces.get(0).iterator().next());
                newGameContext = game.getInitialContext().apply(move);
                application.setGame(new Game(newGameContext.next()));
                isGameNotOver = newGameContext.hasNext();
            }
            else {
                Set<PositionInterface> potentialPositions = Sets.newHashSet();
                for (final Move move : options) potentialPositions.addAll(move.getPiece().getSelfPositions());
                Gson gson = JSONSerializer.getInstance();
                playablePositionsData = gson.toJson(potentialPositions);                
            }

        }
        else {
            newGameContext = game.start(1);
            application.setGame(new Game(newGameContext));
        }

        final Board board = newGameContext.getBoard();
        BoardInterface<ColorInterface> coloredBoard = board.colorize();

        if (!isGameNotOver) {
            final Map<PositionInterface, ColorInterface> mutations = coloredBoard.filter(null);
            coloredBoard = components.board.Board.from(coloredBoard.rows(), coloredBoard.columns(), Colors.Black, Colors.Black, mutations);
        }

        final String json = CellEncoding.encode(coloredBoard);

        /*
        // TODO !!! à revoir complètement
        //if(color.is(Colors.Blue)) {
        PiecesBag bag = newGameContext.getPlayers().get(Colors.Green).getPieces();
        ImmutableSet<Pieces> set1 = ImmutableSet.copyOf(bag.asList());
        //TODO pouvoir interroger le bag de manière à connaitre les pièces utilisées 
        ImmutableSet<Pieces> set2 = ImmutableSet.copyOf(PiecesBag.from(Pieces.values()).asList()); // TODO extract constant
        SetView<Pieces> difference = Sets.difference(set2, set1);
        System.out.println(difference);
        PiecesBag piecesBagDiff = PiecesBag.from(difference);
        String jsonBag = PiecesBagEncoding.encode(piecesBagDiff);
        //}
        */
        
        //PiecesBag bag = newGameContext.getPlayers().get(Colors.Green).getPieces();
        //String jsonBag = PiecesBagEncoding.encode(bag);

        // TODO no cache header
        Representation representation = null;

        // TODO EventStreamRepresentation
        if (isGameNotOver) {
            representation = new StringRepresentation("" +
                    "retry:1000\n" +
                    "data:" + "[[\"" + color + " has just played\"]]" + "\n\n" +
                    //"event:bag\n" +
                    //"data:" + jsonBag + "\n\n" +
                    "event:gamenotover\n" +
                    "data:" + json + "\n\n"+
                    "event:options\n" +
                    "data:" + playablePositionsData + "\n\n",                    
                    TEXT_EVENT_STREAM);
        }
        else {
            representation = new StringRepresentation("" +
                    "data:" + "[[\"Game is over\"]]" + "\n\n" +
                    //"event:bag\n" +
                    //"data:" + jsonBag + "\n\n" +
                    "event:gameover\n" +
                    "data:" + json + "\n\n",
                    TEXT_EVENT_STREAM);
        }

        representation.setCharacterSet(CharacterSet.UTF_8);
        return representation;
    }
}