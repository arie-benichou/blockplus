package blockplus.context

import blockplus.Color
import scala.collection.JavaConversions._
import blockplus.move.Move

object Scorer {
  def apply(history: MoveHistory): Map[Color, Int] = (
    for (
      (color, moves) <- history.data.groupBy(_.getColor)
    ) yield (color -> score(moves)))
    .toMap withDefaultValue -89

  private def score(moves: Seq[Move]) =
    (moves map value sum) - 89 +
      (if (moves.size == 21) 15 else 0)

  private def value(m: Move) = m.getPiece.getSelfPositions.size
}