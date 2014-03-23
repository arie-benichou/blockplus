package games.go

import components.Positions._

//object GoString {
//  val Characters = List('.', 'O', 'X')
//}

sealed case class GoString(in: Set[Position], out: Set[Position]) {
  override def toString = "Freedom: " + this.out.size + "\n" + this.in.mkString("\n") + "\n"
}