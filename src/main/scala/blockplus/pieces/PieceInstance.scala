package blockplus.pieces

import components.plane.elements.Point
import com.google.common.base.Objects

import blockplus.pieces.PieceInstance._

object PieceInstance {
  var _instances = 0
  def apply(self: Iterable[Point], light: Iterable[Point], shadow: Iterable[Point]) = new PieceInstance(self, light, shadow)
  /*
  def apply(self: Iterable[Point]) = {
    val sides = PieceTemplates.computeSides(self)
    val corners = PieceTemplates.computeCorners(self)
    val light = corners -- sides
    val shadow = sides -- self
    new PieceInstance(self, light, shadow)
  }
  
  def apply(self: Point*): PieceInstance = apply(Set(self: _*))
  */
}

final class PieceInstance(val self: Iterable[Point], val light: Iterable[Point], val shadow: Iterable[Point]) extends Iterable[Point] {
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