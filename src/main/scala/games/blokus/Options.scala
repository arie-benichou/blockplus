package games.blokus

import components.Positions.Position
import games.blokus.Game.Color
import games.blokus.Game.Pieces
import games.blokus.Polyominos.Polyomino
import scala.collection.immutable.SortedSet

object Options {

  private val nullPiece = Polyominos._0
  private val nullOption = (Position(-1, -1), Polyominos._0, Set.empty[Position])

  def get(color: Color, board: Board, pieces: Pieces): Set[(Position, Polyomino, Set[Position])] = {
      def options = for {
        light <- board.lights(color)
        polyomino <- pieces.polyominos
        instance <- polyomino.instances
        connector <- instance.connectors
        translation = instance.positions.map(_ + (light - connector)); if (board.isMutable(color, translation))
      } yield (light, polyomino, translation)
    if (options.isEmpty && pieces.contains(nullPiece)) Set(nullOption) else options
  }

}