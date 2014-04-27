package games.go

import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeMap

import abstractions.Adversity
import abstractions.Context
import abstractions.Side
import abstractions.Sides

import components.Positions.Directions
import components.Positions.Ordering
import components.Positions.Position

object GoGame {

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

  val Board19X19 = Array(
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "..................."
  )

  type GoContext = Context[Char, Char, GoBoard, Position]
  type GoMove = abstractions.Move[Char, Position]

  sealed case class Move(side: Char, data: Position) extends abstractions.Move[Char, Position]

  val NullOption = Position(Integer.MAX_VALUE, Integer.MAX_VALUE)

  private def sideValueUpdate(value: Char, e: Any) = value
  private def isStillAlive(value: Char) = true

  private val side1 = Side('O')(sideValueUpdate, isStillAlive)
  private val side2 = Side('X')(sideValueUpdate, isStillAlive)

  private val sides = Sides(Adversity('O', 'X'), List(side1, side2))

  private def application(move: GoMove, space: GoBoard) = if (move.data == NullOption) space else space.play(move.data, move.side)

  // TODO optimisable
  // TODO détecter les cycles
  private def isLegal(move: GoMove, context: GoContext) = move.data == NullOption || context.space.layer(context.id).options.contains(move.data)

  private def isTerminal(context: GoContext) =
    (context.path.size > 2 && context.path.take(2).toSet == Set(Move('O', NullOption), Move('X', NullOption)))

  val context: GoContext = Context(sides, GoBoard(Board9X9), application, isLegal, isTerminal)

}