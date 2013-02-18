package components.plane.applications

import components.plane.elements.Points
import components.plane.elements.Point
import sun.org.mozilla.javascript.internal.ast.Yield
import components.plane.elements.RotatablePoints

object Reflection {

  def reflectX(point: Point)(x: Int): Point = point.copy(x = 2 * x - point.x)

  def reflectX(points: Iterable[Point])(x: Int): Iterable[Point] =
    Points((for (point <- points) yield reflectX(point)(x)).toSeq: _*)

}