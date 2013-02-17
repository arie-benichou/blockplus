package components.plane

import scala.collection.immutable.TreeSet
import com.google.common.base.Objects

object RotatablePoints {
  def apply(points: Points)(implicit referential: Point) = new RotatablePoints(points)(referential)
  def apply(points: Point*)(implicit referential: Point): RotatablePoints = apply(Points(points: _*))(referential)
  def apply(): RotatablePoints = apply(Points.empty)
}

final class RotatablePoints private (private val points: Points)(implicit private val referential: Point) {
  def isEmpty() = points.isEmpty
  override def toString = Objects.toStringHelper(this).add("referential", referential).add("points", points).toString()
  override def hashCode = toString.hashCode()
  override def equals(other: Any) = {
    val that = other.asInstanceOf[RotatablePoints]
    that != null && referential == that.referential && points == that.points
  }
}