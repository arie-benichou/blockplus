package games.blokus

import abstractions.Adversity
import abstractions.Context
import abstractions.Side
import abstractions.Sides
import components.Positions
import components.Positions.Position
import games.blokus.Polyominos.Instances.Instance
import games.blokus.Polyominos.Polyomino
import games.blokus.Polyominos._

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

  // TODO define unique side1, side2, side3, side4
  private val sides = Sides(adversity, List(side, side, side, side))

  type BlokusMove = abstractions.Move[Color, Instance]
  type BlokusContext = Context[Color, Pieces, Board, Instance]

  def isTerminalFunction(context: BlokusContext) = context.sides.count == 0

  def onUpdate(context: BlokusContext) = ()

  val context: BlokusContext = Context(
    sides,
    Board(20, 20),
    (move: BlokusMove, space: Board) => space.apply(move.side, move.data.positions, move.data.shadows, move.data.lights),
    (move: BlokusMove, context: BlokusContext) => {
      val color = move.side
      val polyomino = move.data.selfType
      val positions = move.data.positions
      val pieces = context.side(color).values
      val board = context.space
      pieces.contains(polyomino) && board.isMutable(color, positions) && (positions.isEmpty || positions.exists(board.isLight(color, _))
      )
    }, isTerminalFunction, onUpdate)

  private def isNullMove(move: BlokusMove) = move.data.selfType == Polyominos._0

  private def lastMove(context: BlokusContext, side: Color): BlokusMove = {
    if (context.lastMove.side == side) context.lastMove
    else {
      val path = context.history.dropWhile(_.lastMove.side != side)
      if (isNullMove(path.head.lastMove))
        path.tail.dropWhile(_.lastMove.side != side).head.lastMove
      else path.head.lastMove
    }
  }

  private def isSpecialMove(move: BlokusMove) = move.data.selfType == Polyominos._1

  def score(context: BlokusContext, id: Color) = {
    val side = context.side(id)
    val pieces = side.values;
    val weight = -pieces.weight
    if (weight != 0) weight
    else if (isSpecialMove(lastMove(context, id))) 20
    else 15
  }

  // TODO use Option
  def positionsToPolyomino(positions: Set[Position]) = {
    val topLeftCorner = Positions.topLeftCorner(positions)
    val normalizedPositions = positions.map(_ + (Positions.Origin - topLeftCorner))
    val polyomino = Polyominos.values.find(p => p.order == positions.size && p.instances.exists(_.positions == normalizedPositions)).get
    val normalizedInstance = polyomino.instances.find(_.positions == normalizedPositions).get
    normalizedInstance.translateBy(topLeftCorner - Positions.Origin)
  }

  def forceNullMove(context: BlokusContext): BlokusContext = {
    if (context.isTerminal) context
    else {
      val options = Options.get(context.id, context.space, context.side(context.id).values)
      if (options == Options.Null) {
        val move = Move(context.id, Polyominos._0.instances.head.translateBy((0, 0)))
        forceNullMove(context.apply(move))
      }
      else context
    }
  }

}