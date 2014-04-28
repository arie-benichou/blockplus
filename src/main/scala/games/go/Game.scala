package games.go

import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeMap
import abstractions.Adversity
import abstractions.Context
import abstractions.Side
import abstractions.Sides
import components.Positions
import components.Positions._
import components.Cells

import Board.Symbols._

object Game {

  val Board9X9 = Array(
    ".........",
    ".........",
    ".........",
    ".........",
    ".........",
    ".........",
    ".........",
    ".........",
    "........."
  )
  val Board13X13 = Cells((Positions.from(Origin).to((12, 12)))(Space), Space, Undefined)
  val Board19X19 = Cells((Positions.from(Origin).to((18, 18)))(Space), Space, Undefined)

  type GoContext = Context[Char, Char, Board, Position]
  type GoMove = abstractions.Move[Char, Position]

  sealed case class Move(side: Char, data: Position) extends abstractions.Move[Char, Position]

  val NullOption = Position(Integer.MAX_VALUE, Integer.MAX_VALUE)

  private def sideValueUpdate(value: Char, e: Any) = value
  private def isStillAlive(value: Char) = true

  private val side1 = Side('O')(sideValueUpdate, isStillAlive)
  private val side2 = Side('X')(sideValueUpdate, isStillAlive)

  private val sides = Sides(Adversity('O', 'X'), List(side1, side2))

  private def application(move: GoMove, space: Board) = if (move.data == NullOption) space else space.play(move.data, move.side)

  // TODO optimisable
  private def isLegal(move: GoMove, context: GoContext) = {
    move.data == NullOption || context.space.layer(context.id).options.contains(move.data) &&
      !context.history.exists(_.space == application(move, context.space))
  }

  // TODO cas ou <= 2
  private def isTerminal(context: GoContext) =
    (context.history.size > 2 && Set(context.lastMove, context.history.head.lastMove) == Set(Move('O', NullOption), Move('X', NullOption)))

  private def onUpdate(context: GoContext) = ()

  val context: GoContext = Context(sides, Board(Board9X9), application, isLegal, isTerminal, onUpdate)

}