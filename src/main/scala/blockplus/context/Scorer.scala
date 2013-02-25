package blockplus.context

import blockplus.Color
import scala.collection.JavaConversions._

object Scorer {
  def apply(history: MoveHistory): Map[Color, Int] = (
    for (
      (color, moves) <- history.data.groupBy(_.getColor)
    ) yield (color -> ((moves map (_.getPiece.getSelfPositions.size) sum) - 89)))
    .toMap withDefaultValue -89

}