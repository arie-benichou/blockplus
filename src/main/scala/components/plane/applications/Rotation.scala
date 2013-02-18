package components.plane.applications

import components.plane.elements.Points
import components.plane.elements.Point
import sun.org.mozilla.javascript.internal.ast.Yield
import components.plane.elements.RotatablePoints

object Rotation {

  // ( ([point] - [refeferential]) * [rotation π/4] ) + [referential]
  def rotate45(point: Point)(implicit referential: Point): Point =
    Point(point.y + referential.x - referential.y, -point.x + referential.x + referential.y)

  def rotate45(points: Iterable[Point])(implicit referential: Point): Iterable[Point] =
    Points((for (point <- points) yield rotate45(point)(referential)).toSeq: _*)

}