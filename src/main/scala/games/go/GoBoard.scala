package games.go

import GoBoard._
import components.Cells
import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet
import scala.annotation.tailrec

// TODO extract Parser object
// TODO extract Utils object
// TODO extract new Layer and Layers objects
// TODO extract Strings object
object GoBoard {

  private object GoOptions {
    /**
     *  1) As long as there is at least one degree of freedom remaining once played
     *  2) Except if the opponent string will loose its last degree of freedom
     */
    def apply(character: Char, board: GoBoard) = {
      val stringsForSpace = board.layer('.').strings // TODO parameterize
      val islands = stringsForSpace.filter(_.out.size < 1).flatMap(_.in)
      val stringsForPlayer = board.layer(character).strings.map(_.out)
      val tmp1 = stringsForPlayer.filter(_.size < 2).flatten
      val tmp2 = tmp1.filter(e => islands.contains(e))
      val suicides = tmp2.filter(e => !stringsForPlayer.exists(s => s.contains(e) && s.size > 1))
      val stringsForOpponent = board.layer(opponent(character)).strings
      val captures = stringsForOpponent.filter(_.out.size == 1).flatMap(_.out)
      val effectiveIslands = islands.diff(captures).filterNot(p => stringsForPlayer.exists(_.contains(p)))
      Set() ++ board.spaces -- effectiveIslands -- suicides ++ captures
    }
  }

  private object GoLands {
    def apply(character: Char, board: GoBoard) = {
      val stringsForSpace = board.layer('.').strings // TODO parameterize
      val islands = stringsForSpace.filter(_.out.isEmpty).flatMap(_.in)
      val stringsForPlayer = board.layer(character).strings
      val stringsForOpponent = board.layer(opponent(character)).strings
      val capturables = stringsForPlayer.filter(_.out.size == 1).flatMap(_.out)
      val effectiveIslands = islands.diff(capturables).filter { p =>
        stringsForPlayer.exists(_.out.contains(p)) && !stringsForOpponent.exists(_.out.contains(p))
      }
      effectiveIslands
    }
  }

  private def reduce(board: GoBoard) = {
    val positions = board.spaces.foldLeft(Set[Position]()) { (s, p) =>
      val neighbours = (p * Directions.AllAround).filter(board.cells.get(_) != '?') // TODO
      if (neighbours.count(board.cells.get(_) == '.') != neighbours.size) s + p else s // TODO
    }
    if (positions.isEmpty) board.spaces else positions
  }

  private def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  private def update(board: GoBoard, position: Position, character: Char) = {
    val capturedPositions = board.layer(opponent(character)).strings.filter(_.out == Set(position)).flatMap(s => s.in)
    GoBoard(board.cells.apply(capturedPositions.foldLeft(Map(position -> character))((map, p) => map + (p -> '.'))))
  }

  private def buildConsoleView(data: Array[String]) = {
    val stringTopBottom = " " + "+" + "-" * (data(0).length) + "+" + "\n"
    val out0 = "  " + (0 until data(0).length).map(_ % 10).mkString + "\n" + stringTopBottom
    data.foldLeft(out0)((out, in) => out + ((out.count(_ == '\n') - 2) % 10) + "|" + in + "|\n") + stringTopBottom
  }

  private def cellsToArray(cells: Cells[Char]) = {
    val (rowMax, colMax) = (cells.positions.max.row, cells.positions.max.column)
    val data = Array.fill(rowMax + 1)("?" * (colMax + 1))
    for (i <- 0 to rowMax) data.update(i, (0 to colMax).foldLeft("") { (str, j) => str + cells.get(Position(i, j)) })
    data
  }

  private def buildCells(input: Array[String], initial: Char, undefined: Char): Cells[Char] = {
    val rows = input.length
    val columns = if (rows == 0) 0 else input(0).length
    val data = Map[Position, Char]() ++ (for {
      row <- 0 until rows
      column <- 0 until columns
    } yield (Position(row, column), input(row).charAt(column)))
    Cells(data, initial, undefined)
  }

  private sealed case class CellData(id: Int, in: Set[Position], out: Set[Position])

  // TODO functional way
  private def buildMapOfCellDataByPosition(color: Char, cells: Cells[Char]): collection.mutable.Map[Position, CellData] = {
    var maxId = 0
    var currentId = maxId
    val mapOfCellDataByPosition = collection.mutable.Map[Position, CellData]().withDefaultValue(CellData(0, Set.empty[Position], Set.empty))
    cells.positions.foreach { position =>
      val char = cells.get(position)
      if (char == color) {
        val sides = position * Directions.Sides
        val effectiveSides = sides.filterNot(cells.get(_) == '?') // TODO parameterize
        val connexions = effectiveSides.filter(cells.get(_) == color)
        val freedom = effectiveSides.filter(cells.get(_) == '.') // TODO parameterize
        if (connexions.isEmpty) {
          maxId = maxId + 1
          currentId = maxId
        }
        else {
          val ids = connexions.map(mapOfCellDataByPosition(_).id)
          if (ids == Set(0)) {
            maxId = maxId + 1
            currentId = maxId
          }
          else {
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

  sealed case class GoString(in: Set[Position], out: Set[Position]) {
    override def toString = "\n  in:\n    " + this.in.mkString("\n    ") + "\n  out:\n    " + this.out.mkString("\n    ") + "\n"
  }

  private def computeStrings(character: Char, cells: Cells[Char]) = {
    val map = buildMapOfCellDataByPosition(character, cells)
    val mapGroupedById = map.groupBy(e => e._2.id).mapValues(_.keySet)
    val rawStrings = mapGroupedById.mapValues(p => (p, p.flatMap(p => map(p).out)))
    val strings = rawStrings.map { e =>
      val value = e._2
      GoString(value._1.toSet, value._2.toSet)
    }
    strings.toSet
  }

  sealed case class Layer(strings: Set[GoString]) {}

  def apply(cells: Cells[Char]): GoBoard = {
    val layers = Map(
      'O' -> Layer(computeStrings('O', cells)),
      'X' -> Layer(computeStrings('X', cells)),
      '.' -> Layer(computeStrings('.', cells)))
    new GoBoard(cells, layers)
  }

  def apply(data: Array[String]): GoBoard = GoBoard(buildCells(data, '.', '?'))

}

sealed case class GoBoard(cells: Cells[Char], layers: Map[Char, Layer]) {
  lazy val consoleView = buildConsoleView(cellsToArray(this.cells))
  lazy val spaces = this.cells.filterDefaults()
  lazy val mainSpaces = reduce(this)

  // TODO extract Strings Object and create new Layer
  private val landsByCharacter = collection.mutable.Map[Char, Set[Position]]()
  private val optionsByCharacter = collection.mutable.Map[Char, Set[Position]]()
  def layer(character: Char) = this.layers(character)
  def lands(character: Char) = this.landsByCharacter.getOrElseUpdate(character, GoLands(character, this))
  def options(character: Char) = this.optionsByCharacter.getOrElseUpdate(character, GoOptions(character, this))

  def play(position: Position, character: Char) = update(this, position, character)
  override def toString = this.consoleView
}
