package components.plane.elements

import com.google.common.base.Objects

object RotatablePoints {
  def apply(points: Points)(implicit referential: Point) = new RotatablePoints(points)(referential)
  def apply(points: Point*)(implicit referential: Point): RotatablePoints = apply(Points(points: _*))(referential)
  def apply(): RotatablePoints = apply(Points.empty)
}

final class RotatablePoints private (private val points: Points)(implicit val referential: Point) extends Iterable[Point] {
  override def iterator = points.iterator
  override def isEmpty() = points.isEmpty
  override def toString = Objects.toStringHelper(this).add("referential", referential).add("points", points).toString()
  override def hashCode = toString.hashCode()
  override def equals(other: Any) = {
    val that = other.asInstanceOf[RotatablePoints]
    that != null && referential == that.referential && points == that.points
  }
}