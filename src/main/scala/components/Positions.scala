package components

import scala.collection.immutable.SortedSet

object Positions {

  type Direction = (Int, Int)

  val Origin = Position(0, 0)

  implicit def tupleOfIntToPosition(t: (Int, Int)) = Position(t._1, t._2)

  implicit object Ordering extends Ordering[Position] {
    def compare(p1: Position, p2: Position): Int =
      if (p1.row < p2.row) -1
      else if (p1.row > p2.row) 1
      else if (p1.column < p2.column) -1
      else if (p1.column > p2.column) 1
      else 0
  }

  object Directions {

    val TopLeft: Direction = (-1, -1)
    val Top: Direction = (-1, 0)
    val TopRight: Direction = (-1, 1)
    val Left: Direction = (0, -1)
    val Null: Direction = (0, 0)
    val Right: Direction = (0, 1)
    val BottomLeft: Direction = (1, -1)
    val Bottom: Direction = (1, 0)
    val BottomRight: Direction = (1, 1)

    val Sides: Set[Direction] = Set(Top, Left, Right, Bottom)
    val Corners = Set(TopLeft, TopRight, BottomLeft, BottomRight)
    val AllAround = Set(TopLeft, Top, TopRight, Left, Right, BottomLeft, Bottom, BottomRight)
  }

  sealed case class Position(row: Int, column: Int) {
    def +(vector: Direction): Position = copy(row + vector._1, column + vector._2)
    def -(p2: Position): Direction = (row - p2.row, column - p2.column)
    def *(directions: Set[Direction]): Set[Position] = directions.map(this + _)
  }

  def topLeftCorner(positions: Set[Position]): Position =
    Positions.Origin + positions.foldLeft((Int.MaxValue, Int.MaxValue))((t, p) => (Math.min(t._1, p.row), Math.min(t._2, p.column)))

  def translateBy(vector: Direction, positions: Set[Position]): Set[Position] =
    positions.map(_ + vector)

  trait Positions {
    def min: Position
    def max: Position
    def positions: Iterable[Position]
    def apply[A](e: A): Map[Position, A]
  }

  sealed case class PositionsBuilder(from: Position, to: Position) {
    def build(): Positions = ConcretePositions(from, to)
  }

  sealed case class from(f: Position) {
    def to(t: Position) = PositionsBuilder(f, t).build()
  }

  private def computePositions(positions: Positions) = {
    for {
      r <- (positions.min.row to positions.max.row)
      c <- (positions.min.column to positions.max.column)
    } yield Position(r, c)
  }

  sealed case class ConcretePositions(min: Position, max: Position) extends Positions {
    lazy val positions: Set[Position] = SortedSet() ++ computePositions(this)
    def apply[A](e: A) = this.positions.foldLeft(Map[Position, A]()) { (m, p) => m + (p -> e) }
  }

}