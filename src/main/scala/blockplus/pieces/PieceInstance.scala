package blockplus.pieces

import components.plane.elements.Point
import com.google.common.base.Objects
import blockplus.pieces.PieceInstance._
import scala.collection.mutable.Map

object PieceInstance {
  var _instances = 0
  //val empty = new PieceInstance(Set(), Set(), Set())
  private val cache: Map[Iterable[Point], PieceInstance] = Map() // TODO Set[Point]
  def apply(self: Iterable[Point], light: Iterable[Point], shadow: Iterable[Point]) = cache.getOrElseUpdate(self, new PieceInstance(self, light, shadow))
  val empty = apply(Set(), Set(), Set())
  def apply(self: Iterable[Point]) = {
    cache.getOrElseUpdate(self, {
      println("cache update")
      val sides = PieceTemplates.computeSides(self)
      val corners = PieceTemplates.computeCorners(self)
      val light = corners -- sides
      val shadow = sides -- self
      new PieceInstance(self, light, shadow)
    })
  }

  def apply(self: Point*): PieceInstance = apply(Set(self: _*))
}

// TODO add piece type hinting (footprint)
// see piece2.matching
final class PieceInstance private (
  val self: Iterable[Point],
  val light: Iterable[Point], // TODO lazy
  val shadow: Iterable[Point] // TODO lazy
  ) extends Iterable[Point] {
  _instances += 1
  override def iterator = self.iterator
  override def isEmpty() = self.isEmpty
  override def toString = Objects.toStringHelper(this).add("self", self).add("light", light).add("shadow", shadow).toString()
  override def hashCode = toString.hashCode()
  override def equals(other: Any) = {
    val that = other.asInstanceOf[PieceInstance]
    that != null && self == that.self
  }
}