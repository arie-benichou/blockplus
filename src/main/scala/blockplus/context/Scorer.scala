package blockplus.context

import blockplus.Color
import scala.collection.JavaConversions._

object Scorer {
  def apply(history: MoveHistory): Map[Color, Int] = {
    (for (c <- Color.values) yield (c -> -89)) toMap
  }
}