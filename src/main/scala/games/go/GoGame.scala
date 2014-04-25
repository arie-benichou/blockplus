package games.go

import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet
import scala.collection.mutable.ListBuffer
import abstractions.Context
import abstractions.Side
import abstractions.Adversity
import abstractions.Sides
import abstractions.Move
import scala.collection.immutable.Stack

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
    ".........O.........",
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

  type GoContext = Context[Char, Unit, GoBoard, Position]
  type GoMove = abstractions.Move[Char, Position]

  sealed case class Move(side: Char, data: Position) extends abstractions.Move[Char, Position]

  val NullOption = Position(Integer.MIN_VALUE, Integer.MIN_VALUE)

  private def f1(value: Unit, e: Any) = value
  private def f2(value: Unit) = true
  private val side = Side()(f1, f2)

  private val sides = Sides(Adversity('O', 'X'), List(side, side))

  private def application(move: GoMove, space: GoBoard) = space.play(move.data, move.side)

  // TODO détecter les cycles
  private def isLegal(move: GoMove, context: GoContext) =
    move.data == NullOption || // TODO enlever après debug
      GoOptions(context.id, context.space).contains(move.data) // TODO optimisable

  private def isTerminal(context: GoContext) =
    (context.path.size > 2 && context.path.take(2).toSet == Set(Move('O', NullOption), Move('X', NullOption))) ||
      context.path.size > 30 // TODO enlever après debug

  val context: GoContext = Context(sides, GoBoard(Board9X9), application, isLegal, isTerminal)

}