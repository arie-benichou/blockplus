package blockplus.pieces

import scala.collection.immutable.Set
import com.google.common.base.Objects
import components.plane.applications._
import components.plane.elements.Point
import blockplus.pieces.PieceTemplates._
import scala.collection.mutable.Map

object PieceTemplates {

  var _instances = 0

  private val cache: Map[PieceGraph, PieceTemplates] = Map()
  def apply(pieceGraph: PieceGraph) = cache.getOrElseUpdate(pieceGraph, new PieceTemplates(pieceGraph))

  private def computeDegree(pieceGraph: PieceGraph) = pieceGraph.positions.size
  private def computeProjection(pieceGraph: PieceGraph) = pieceGraph.positions
  private def computeAnchor(pieceGraph: PieceGraph) = pieceGraph.anchor

  private lazy val sides = for { d <- -1 to 1 if d != 0; dij <- List((d, 0), (0, d)) } yield dij
  private lazy val corners = for { d <- -1 to 1 if d != 0; dij <- List((d, 1), (-d, -1)) } yield dij

  def computeSides(coordinates: Iterable[Point]) = coordinates.flatMap(p => sides.map(Translation.translate(p)(_))).toSet
  def computeCorners(coordinates: Iterable[Point]) = coordinates.flatMap(p => corners.map(Translation.translate(p)(_))).toSet

  private def computeRadius(template: PieceTemplates) = {
    if (template.isEmpty) 0
    else template.positions.map(point => Math.max(Math.abs(point.x - template.anchor.x), Math.abs(point.y - template.anchor.y))).max
  }

  private def computeDistinctRotations(pieceInstance: PieceInstance, anchor: Point) = {
    val r0 = pieceInstance
    val r1 = PieceInstance(
      Rotation.rotate45(r0.self)(anchor),
      Rotation.rotate45(r0.light)(anchor),
      Rotation.rotate45(r0.shadow)(anchor)
    )
    val r2 = PieceInstance(
      Rotation.rotate45(r1.self)(anchor),
      Rotation.rotate45(r1.light)(anchor),
      Rotation.rotate45(r1.shadow)(anchor)
    )
    val r3 = PieceInstance(
      Rotation.rotate45(r2.self)(anchor),
      Rotation.rotate45(r2.light)(anchor),
      Rotation.rotate45(r2.shadow)(anchor)
    )
    Set(r0, r1, r2, r3)
  }

  private def computeDistinctInstances(template: PieceTemplates) = {
    if (template.isEmpty) Set(PieceInstance.empty)
    else {
      val thisSide = PieceInstance(template.positions, template.light, template.shadow)
      val thatSide = PieceInstance(
        Reflection.reflectX(template.positions)(template.anchor.x),
        Reflection.reflectX(template.light)(template.anchor.x),
        Reflection.reflectX(template.shadow)(template.anchor.x)
      )
      computeDistinctRotations(thisSide, template.anchor) ++ computeDistinctRotations(thatSide, template.anchor)
    }
  }

}

final class PieceTemplates private (private val pieceGraph: PieceGraph) extends Iterable[PieceInstance] {

  _instances += 1

  lazy val degree = computeDegree(pieceGraph)
  lazy val anchor = computeAnchor(pieceGraph)
  lazy val positions = computeProjection(pieceGraph)

  lazy val radius = computeRadius(this)
  lazy val instances = computeDistinctInstances(this)

  lazy val iterator = instances.iterator

  private lazy val sides = computeSides(positions)
  private lazy val corners = computeCorners(positions)
  private lazy val light = corners -- sides
  private lazy val shadow = sides -- positions

  override def isEmpty = pieceGraph.isEmpty

  override def toString = Objects.toStringHelper(this)
    .add("degree", degree)
    .add("anchor", anchor)
    .add("radius", radius)
    .add("instances", instances.toString())
    .toString()

  override def hashCode = pieceGraph.hashCode()

  override def equals(other: Any) = {
    val that = other.asInstanceOf[PieceTemplates]
    that != null && pieceGraph == that.pieceGraph
  }

  def on(point: Point) = {
    val vector = (point.x - anchor.x, point.y - anchor.y)
    PieceTemplates.apply(PieceGraph(Translation.translate(positions)(vector))(point))
  }

}