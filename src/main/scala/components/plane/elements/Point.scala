package components.plane.elements

import scala.language.implicitConversions

object Point {
  implicit val origin = new Point(0, 0)
  implicit val ordering = new Ordering[Point] {
    def compare(point1: Point, point2: Point) = {
      if (point1.x < point2.x) -1
      else if (point1.x > point2.x) 1
      else if (point1.y < point2.y) -1
      else if (point1.y > point2.y) 1
      else 0
    }
  }
  implicit def pairToPoint(xy: (Int, Int)) = xy match {
    case (0, 0) => origin
    case (x, y) => new Point(x, y)
  }
}

final case class Point(val x: Int, val y: Int) {
  override def toString = s"($x,$y)"
}
