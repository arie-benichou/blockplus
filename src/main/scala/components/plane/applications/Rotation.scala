package components.plane.applications

import components.plane.elements.Point

object Rotation {

  // ( ([point] - [refeferential]) * [rotation π/4] ) + [referential]
  def rotate45(point: Point)(implicit referential: Point): Point =
    Point(point.y + referential.x - referential.y, -point.x + referential.x + referential.y)

  def rotate45(points: Iterable[Point])(implicit referential: Point): Iterable[Point] =
    for (point <- points) yield (rotate45(point)(referential))
}