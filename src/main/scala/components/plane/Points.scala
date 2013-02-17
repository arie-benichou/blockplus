package components.plane

import scala.collection.immutable.TreeSet

object Points {
  val empty = new Points(Set.empty)
  def apply(points: Point*) = points match {
    case Nil => empty
    case _   => new Points(TreeSet(points: _*));
  }
}

final class Points private (private val data: Set[Point]) {
  def isEmpty() = data.isEmpty
  override def toString = data.mkString("{", ",", "}")
  override def hashCode = this.data.hashCode()
  override def equals(other: Any) = {
    val that = other.asInstanceOf[Points]
    that != null && data == that.data
  }
}