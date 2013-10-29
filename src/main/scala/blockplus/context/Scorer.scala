package blockplus.context

import blockplus.Color
import scala.collection.JavaConversions._
import blockplus.move.Move
import scala.language.postfixOps

object Scorer {
  def apply(history: MoveHistory): Map[Color, Int] = (
    for (
      (color, moves) <- history.data.groupBy(_.getColor)
    ) yield (color -> score(moves)))
    .toMap withDefaultValue -89

  private def score(moves: Seq[Move]) =
    (moves map value sum) - 89 +
      (if (moves.size == 21) {
        if (moves.head.getPiece.getId == 1)
          20 // all played with monomino last
        else
          15 // all played but not monomino last
      } else 0) // no bonus

  private def value(m: Move) = m.getPiece.getSelfPositions.size
}