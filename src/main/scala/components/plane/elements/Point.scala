package components.plane.elements

object Point {
  implicit val origin = new Point(0, 0)
  def apply() = origin
  def apply(xy: (Int, Int)) = xy match {
    case (0, 0) => origin
    case (x, y) => new Point(x, y)
  }
  def apply(x: Int, y: Int): Point = apply((x, y))
  implicit val ordering = new Ordering[Point] {
    def compare(point1: Point, point2: Point) = {
      if (point1.x < point2.x) -1
      else if (point1.x > point2.x) 1
      else if (point1.y < point2.y) -1
      else if (point1.y > point2.y) 1
      else 0
    }
  }
  implicit def pairToPoint(xy: (Int, Int)) = Point(xy)
}

final class Point private (val x: Int, val y: Int) {
  override def toString = "(" + x + "," + y + ")"
  override def hashCode = this.toString().hashCode()
  override def equals(other: Any) = {
    val that = other.asInstanceOf[Point]
    that != null && x == that.x && y == that.y
  }
}