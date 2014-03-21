package components

object Positions {

  type Direction = (Int, Int)

  val Origin = Position(0, 0)

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

}