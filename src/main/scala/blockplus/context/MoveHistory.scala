package blockplus.context

import interfaces.move.MoveInterface
import scala.collection.immutable.Stack

import blockplus.Color
import blockplus.move.Move
import blockplus.move.Moves
import blockplus.piece.PieceType

final case class MoveHistory(val data: Stack[Move] = Stack.empty) {
  def this() = this(Stack.empty)
  def play(move: Move) = if (move.isNull) this else copy(data.push(move))
  def isEmpty = !data.isEmpty
  def contains(color: Color) = data.exists(_.getColor == color)
  def last = data.head
  def last(color: Color) = data.dropWhile(_.getColor != color).head
  override def toString = data.toString
}