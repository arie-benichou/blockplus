package components.plane.applications

import components.plane.elements.Point

object Reflection {

  def reflectX(point: Point)(x: Int): Point = point.copy(x = 2 * x - point.x)

  def reflectX(points: Iterable[Point])(x: Int): Iterable[Point] =
    for (point <- points) yield reflectX(point)(x)

}