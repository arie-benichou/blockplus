package blockplus.pieces

import com.google.common.base.Objects
import components.plane.elements.Point
import components.plane.applications.Translation
import scala.collection.mutable.Map

// TODO define a type of piece from a graph and compute automaticly its anchor point
// see piece2.matching
object PieceGraph {
  def apply(coordinates: Iterable[Point])(implicit anchorPoint: Point) = new PieceGraph(coordinates.toSet)(anchorPoint)
  def apply(coordinates: Point*)(implicit anchorPoint: Point): PieceGraph = apply(coordinates)(anchorPoint)
}

final class PieceGraph private (val positions: Iterable[Point])(implicit val anchor: Point) extends Iterable[Point] {
  override def iterator = positions.iterator
  override def isEmpty = positions.isEmpty
}