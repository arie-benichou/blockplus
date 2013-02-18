package components.plane.applications

import components.plane.elements.Points
import components.plane.elements.Point
import sun.org.mozilla.javascript.internal.ast.Yield
import components.plane.elements.RotatablePoints

object Translation {

  def translate(point: Point)(vector: (Int, Int)): Point =
    Point(point.x + vector._1, point.y + +vector._2)

  def translate(points: Iterable[Point])(vector: (Int, Int)): Iterable[Point] =
    Points((for (point <- points) yield translate(point)(vector)).toSeq: _*)

}