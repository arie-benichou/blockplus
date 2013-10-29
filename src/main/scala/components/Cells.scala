package components

import components.Positions.Position

object Cells {

  private def isInDimension(rows: Int, columns: Int)(p: Position) =
    p.row > -1 && p.column > -1 && p.row < rows && p.column < columns

  def apply[A](rows: Int, columns: Int, initialSymbol: A, undefinedSymbol: A, data: Map[Position, A]): Cells[A] = {
    val cells = (data.filterNot(_._2 == initialSymbol)).withDefaultValue(initialSymbol)
    new Cells[A](cells, rows, columns, initialSymbol, undefinedSymbol)
  }

  def apply[A](rows: Int, columns: Int, initialSymbol: A, undefinedSymbol: A): Cells[A] = {
    apply(rows, columns, initialSymbol, undefinedSymbol, Map.empty[Position, A])
  }

}

sealed case class Cells[A] private (data: Map[Position, A], rows: Int, columns: Int, initialSymbol: A, undefinedSymbol: A) {
  lazy val definedPositions: Set[Position] = data.keySet
  // TODO prendre en compte les modifications apportés dans Blokus.Board
  // TODO game of life test
  def get(position: Position): A = if (Cells.isInDimension(rows, columns)(position)) data(position) else data.getOrElse(position, undefinedSymbol)
  def filter(p: ((Position, A)) => Boolean): Set[Position] = data.filter(p).keySet
  def apply(input: Map[Position, A]): Cells[A] = copy(data ++ (input.filterNot(_._2 == initialSymbol)))
}