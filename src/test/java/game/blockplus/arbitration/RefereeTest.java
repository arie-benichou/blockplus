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

package game.blockplus.arbitration;


// FIXME write a good set of tests
/*
public class RefereeTest {

    @Test
    public void bugPiece17WhenReferentialIsOnAnotherColor() {

        // TODO ContextParser
        final ContextBuilder contextBuilder = new ContextBuilder();
        final Pieces blueBag = new Pieces.Builder().addAll(PIECE0, PIECE3, PIECE12, PIECE17).build();
        final Pieces greenBag = new Pieces.Builder().addAll(PIECE0, PIECE2, PIECE16).build();
        final Pieces bagOfPieces = new Pieces.Builder().addAll(PIECE0, PIECE1).build();
        final Builder playersBuilder = new Players.Builder();
        playersBuilder.add(new Player(Blue, blueBag));
        playersBuilder.add(new Player(Green, greenBag));
        playersBuilder.add(new Player(Yellow, bagOfPieces));
        playersBuilder.add(new Player(Red, bagOfPieces));
        final Players players = playersBuilder.build();

        contextBuilder.setPlayers(players);
        final int rows = 8, columns = 5;
        final Board board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                .addLayer(Blue, new LayerMutationBuilder().setLightPositions(Position(0, 0)).build())
                .addLayer(Yellow, new LayerMutationBuilder().setLightPositions(Position(0, columns - 1)).build())
                .addLayer(Red, new LayerMutationBuilder().setLightPositions(Position(rows - 1, columns - 1)).build())
                .addLayer(Green, new LayerMutationBuilder().setLightPositions(Position(rows - 1, 0)).build())
                .build();
        contextBuilder.setBoard(board);
        Context context = contextBuilder.build();

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(PIECE3);
            final PieceInterface piece = pieceInstances.get(0).translateTo(Position(0, 1));
            final Move move = Moves.getMove(Blue, piece);
            context = context.apply(move);
        }

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(PIECE12);
            final PieceInterface piece = pieceInstances.get(3).reflectAlongVerticalAxis().translateTo(Position(2, 4));
            final Move move = Moves.getMove(Blue, piece);
            context = context.apply(move);
        }

        context = context.forward();

        {
            final List<MoveInterface> options = context.options();
            final MoveInterface move = options.iterator().next();
            context = context.apply(move);
        }

        context = context.forward();

        {
            final List<MoveInterface> options = context.options();
            final MoveInterface move = options.iterator().next();
            context = context.apply(move);
        }

        context = context.forward();

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(PIECE2);
            final PieceInterface piece = pieceInstances.get(1).translateTo(Position(7, 0));
            final Move move = Moves.getMove(Green, piece);
            context = context.apply(move);
        }

        {
            final List<PieceInterface> pieceInstances = Lists.newArrayList(PIECE16);
            final PieceInterface piece = pieceInstances.get(2).translateTo(Position(5, 2));
            final Move move = Moves.getMove(Green, piece);
            context = context.apply(move);
        }

        context = context.forward();

        final Board finalBoard = context.getBoard();
        final Layer layer = finalBoard.getLayer(Blue);
        final Map<CellPosition, State> lights = layer.getLights();
        assertTrue(!lights.isEmpty());

        final List<MoveInterface> legalMoves = context.options();
        assertEquals(1, legalMoves.size());

    }
}
*/