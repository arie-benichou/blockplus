package games.blokus

import abstractions.Adversity
import abstractions.Context
import abstractions.Side
import abstractions.Sides
import games.blokus.Polyominos.Polyomino
import games.blokus.Polyominos.Instances._
import games.blokus.Board.Layer.State

object Game {

  sealed trait Color

  object Color {
    case object Blue extends Color
    case object Yellow extends Color
    case object Red extends Color
    case object Green extends Color
  }

  object Pieces {
    def apply(polyominos: Polyomino*): Pieces = new Pieces(Set(polyominos: _*))
  }

  sealed case class Pieces(polyominos: Set[Polyomino]) {
    lazy val weight = polyominos.foldLeft(0)((weight, p) => weight + p.order)
    def contains(polyomino: Polyomino) = polyominos.contains(polyomino)
    def remove(polyomino: Polyomino) = copy(polyominos - polyomino)
  }

  sealed case class Move(side: Color, data: Instance) extends abstractions.Move[Color, Instance]

  private val pieces = Pieces(Polyominos.values: _*)
  //private val pieces = Pieces(Polyominos.values.take(3): _*)

  private val side = Side(pieces)(
    (values: Pieces, p: Any) => values.remove(p.asInstanceOf[Instance].selfType),
    (values: Pieces) => values.contains(Polyominos._0)
  )

  private val adversity = Adversity[Color](Color.Blue, Color.Yellow, Color.Red, Color.Green)

  private val sides = Sides(adversity, List(side, side, side, side))

  type BlokusMove = abstractions.Move[Color, Instance]
  type BlokusContext = Context[Color, Pieces, Board, Instance]

  val context: BlokusContext = Context(sides, Board(20, 20))(
    (move: BlokusMove, space: Board) => space.apply(move.side, move.data.positions, move.data.shadows, move.data.lights),
    (move: BlokusMove, context: BlokusContext) => {
      val color = move.side
      val polyomino = move.data.selfType
      val positions = move.data.positions
      val pieces = context.side(color).values
      val board = context.space
      pieces.contains(polyomino) &&
        board.isMutable(color, positions) &&
        //(positions.isEmpty || positions.exists(board.layers(color).cells.get(_) == State.Metta))
        (positions.isEmpty || positions.exists(board.isLight(color, _)))
    }
  )

}