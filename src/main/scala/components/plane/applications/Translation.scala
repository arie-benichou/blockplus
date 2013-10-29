package components.plane.applications

import components.plane.elements.Point

object Translation {

  def translate(point: Point)(vector: (Int, Int)): Point =
    Point(point.x + vector._1, point.y + +vector._2)

  def translate(points: Iterable[Point])(vector: (Int, Int)): Iterable[Point] =
    for (point <- points) yield translate(point)(vector)

}