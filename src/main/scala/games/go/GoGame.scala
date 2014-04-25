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
  private val board9X9 = Array(
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
  val NullOption = Position(Integer.MIN_VALUE, Integer.MIN_VALUE)
  type GoMove = abstractions.Move[Char, Position]
  sealed case class Move(side: Char, data: Position) extends abstractions.Move[Char, Position]
  private def f1(value: Char, e: Any) = { println(value + " => " + e + "\n"); value }
  private def f2(values: Char) = true
  private val sides = Sides(Adversity('O', 'X'), List(Side('O')(f1, f2), Side('X')(f1, f2)))
  private def f3(move: GoMove, space: GoBoard) = space.play(move.data, move.side)
  type GoContext = Context[Char, Char, GoBoard, Position]
  private def f4(move: GoMove, context: GoContext) =
    move.data == NullOption || // TODO enlever après debug
      GoOptions(context.id, context.space).contains(move.data) // TODO optimisable
  private def f5(context: GoContext) = context.path.size > 2 && context.path.take(2).toSet == Set(Move('O', NullOption), Move('X', NullOption))
  val context: GoContext = Context(sides, GoBoard(board9X9), f3, f4, f5)
}