package blockplus.context

import interfaces.move.MoveInterface
import scala.collection.immutable.Stack

import blockplus.Color
import blockplus.move.Move
import blockplus.move.Moves
import blockplus.piece.PieceType

final case class MoveHistory(private val data: Stack[Move]) {
  def this() = this(new Stack[Move])
  def apply(move: Move) = if (move.isNull()) this else copy(data.push(move))
  def isEmpty() = !data.isEmpty
  def contains(color: Color) = data.exists(_.getColor == color)
  def last() = data.head
  def last(color: Color) = data.dropWhile(_.getColor != color).head
  override def toString() = data.toString
}