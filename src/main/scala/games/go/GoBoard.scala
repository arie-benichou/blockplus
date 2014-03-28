package games.go

import GoBoard._
import components.Cells
import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet

object GoBoard {

  def buildConsoleView(data: Array[String]) = {
    val stringTopBottom = " " + "+" + "-" * (data(0).length) + "+" + "\n"
    val out0 = "  " + (0 until data(0).length).map(_ % 10).mkString + "\n" + stringTopBottom
    data.foldLeft(out0)((out, in) => out + ((out.count(_ == '\n') - 2) % 10) + "|" + in + "|\n") + stringTopBottom
  }

  def cellsToArray(cells: Cells[Char]) = {
    val max = cells.data.max
    val rowMax = max._1.row
    val colMax = max._1.column
    var data = Array.fill(rowMax + 1)("?" * (colMax + 1))
    for (i <- 0 to rowMax) {
      var str = ""
      for (j <- 0 to colMax) {
        str += cells.get(Position(i, j))
      }
      data.update(i, str)
    }
    data
  }

  def buildCells(data: Array[String], initial: Char, undefined: Char): Cells[Char] = {
    val rows = data.length
    val columns = if (rows == 0) 0 else data(0).length
    val cells = Cells(rows, columns, initial, undefined, Map.empty[Position, Char])
    val mutations = Map[Position, Char]() ++ (for {
      row <- 0 until rows
      column <- 0 until columns
    } yield (Position(row, column), data(row).charAt(column)))
    cells(mutations)
  }

  // TODO privatize
  sealed case class CellData(id: Int, in: Set[Position], out: Set[Position])

  // TODO functional way
  def buildMapOfCellDataByPosition(color: Char, cells: Cells[Char]): collection.mutable.Map[Position, CellData] = {
    var maxId = 0
    var currentId = maxId
    val mapOfCellDataByPosition = collection.mutable.Map[Position, CellData]().withDefaultValue(CellData(0, Set.empty[Position], Set.empty))
    val cellsByNaturalOrder = TreeMap(cells.data.toSeq: _*)
    for ((position, char) <- cellsByNaturalOrder) {
      if (char == color) {
        val sides = position * Directions.Sides
        val effectiveSides = sides.filterNot(cells.get(_) == '!') // TODO parameterize
        val connexions = effectiveSides.filter(cells.get(_) == color)
        val freedom = effectiveSides.filter(cells.get(_) == '.') // TODO parameterize
        if (connexions.isEmpty) {
          maxId = maxId + 1
          currentId = maxId
        } else {
          val ids = connexions.map(mapOfCellDataByPosition(_).id)
          if (ids == Set(0)) {
            maxId = maxId + 1
            currentId = maxId
          } else {
            val filteredIds = ids.filter(_ > 0)
            val min = filteredIds.min
            val idsToFix = filteredIds.filterNot(_ == min)
            idsToFix.foreach { idToFix =>
              val positionsToFix = mapOfCellDataByPosition.filter(e => (e._2.id == idToFix)).keySet
              positionsToFix.foreach { p =>
                val data = mapOfCellDataByPosition(p)
                mapOfCellDataByPosition.put(p, CellData(min, data.in, data.out))
              }
            }
            currentId = min
          }
        }
        mapOfCellDataByPosition.put(position, CellData(currentId, connexions, freedom))
      }
    }
    mapOfCellDataByPosition
  }

  def apply(data: Array[String]) = new GoBoard(buildCells(data, '?', '!'))

  def main(args: Array[String]) {

    val data = Array(
      "..O..",
      "..OO.",
      ".O.OO",
      "OOO..",
      ".O.OO")

    val board = GoBoard(data)
    println(board)
1
    println("Strings for 'O'\n")
    val strings = board.layer('O').strings
    strings.foreach(println)

  }

}

sealed case class GoString(in: Set[Position], out: Set[Position]) {
  override def toString = "Freedom: " + this.out.size + "\n" + this.in.mkString("\n") + "\n"
}

sealed case class Layer(character: Char, cells: Cells[Char]) {

  lazy val strings: List[GoString] = {
    val map = buildMapOfCellDataByPosition(this.character, this.cells)
    val mapGroupedById = map.groupBy(e => e._2.id).mapValues(_.keySet)
    val rawStrings = mapGroupedById.mapValues(p => (p, p.flatMap(p => map(p).out)))
    val strings = rawStrings.map { e =>
      val value = e._2
      val in = SortedSet() ++ value._1
      val out = SortedSet() ++ value._2
      GoString(in, out)
    }
    strings.toList.sortBy(_.out.size)(math.Ordering.Int.reverse)
  }

}

sealed case class GoBoard(cells: Cells[Char]) {

  lazy val consoleView = buildConsoleView(cellsToArray(this.cells))

  override def toString = this.consoleView

  private val layers = Map(
    'O' -> Layer('O', this.cells),
    'X' -> Layer('X', this.cells),
    '.' -> Layer('.', this.cells))

  def layer(character: Char) = layers(character)

  def play(character: Char)(position: Position): GoBoard = {
    def opponent(character: Char) = if (character == 'O') 'X' else 'O'
    val updatedCells = cells.apply(Map(position -> character))
    val board = GoBoard(updatedCells)
    val stringsForOpponent = board.layer(opponent(character)).strings
    val captures = stringsForOpponent.filter(_.out.isEmpty).map(_.in)
    if (captures.isEmpty) board
    else {
      val positions = captures.head
      val map = positions.map(p => (p, '.')).toMap
      GoBoard(updatedCells.apply(map))
    }
  }

}