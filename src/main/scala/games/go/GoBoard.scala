package games.go

import GoBoard._
import components.Cells
import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer

object GoBoard {

  // TODO inject function
  def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  def update(board: GoBoard, position: Position, character: Char) = {
    val others = board.layer(opponent(character)).strings
    val mutatedOutsForOthers = others.filter(e => e.out.contains(position)).map(e => GoString(e.in, e.out - position))
    val captures = mutatedOutsForOthers.filter(_.out.isEmpty)
    val updatedOthers = (others.filterNot(e => e.out.contains(position)) ++ mutatedOutsForOthers.filterNot(_.out.isEmpty))
    val capturedPositions = captures.flatMap(s => s.in)
    val selves = board.layer(character).strings
    val connexions = selves.filter(e => e.out.contains(position))
    val ins = connexions.flatMap(_.in) + position
    val outs = connexions.flatMap(_.out) - position ++ (position * Directions.Sides).filter(board.cells.get(_) == '.')
    val updatedSelves1 = Set(GoString(ins, outs)) ++ selves.filterNot(e => e.out.contains(position))
    val _updatedCells = board.cells.apply(Map(position -> character))
    val ordinalBySelves = updatedSelves1.zip((1 to updatedSelves1.size)).toMap
    val selvesOutsToUpdate = collection.mutable.Map.empty[Int, Set[Position]].withDefaultValue(Set.empty[Position])
    capturedPositions.foreach { cp =>
      val tmp1 = (cp * Directions.Sides).filter(_updatedCells.get(_) == character)
      tmp1.foreach { p =>
        val tmp2 = updatedSelves1.filter(e => e.in.contains(p))
        val tmp3 = tmp2.map(s => (ordinalBySelves(s), cp)).head
        selvesOutsToUpdate.update(tmp3._1, selvesOutsToUpdate(tmp3._1) + tmp3._2)
      }
    }
    val selvesByOrdinal = collection.mutable.Map() ++ (1 to updatedSelves1.size).zip(updatedSelves1)
    selvesOutsToUpdate.foreach { e =>
      val (ordinal, out) = e
      val s = selvesByOrdinal(ordinal)
      selvesByOrdinal.update(ordinal, GoString(s.in, s.out ++ out))
    }
    val updatedSelves2 = selvesByOrdinal.values.toSet
    val updatedCells = _updatedCells.apply(capturedPositions.map(p => (p, '.')).toMap)
    val common = board.layer('.').strings
    val concernedSpaceString = common.filter(e => e.out.contains(position))
    val unconcernedSpaceString = common.filterNot(e => e.out.contains(position))
    val updatedSpaceStrings =
      if (concernedSpaceString.isEmpty)
        unconcernedSpaceString.filterNot(e => e.in.contains(position)) ++ captures.map(e => GoString(e.in, if (e.in.size == 1) Set() else e.in))
      else {
        val concernedSpaces = concernedSpaceString.head.in - position
        val (ls1, ls2) = concernedSpaces.foldLeft((List.empty[Set[Position]], List.empty[Set[Position]])) { (tuple, p) =>
          val (ls1, ls2) = tuple
          val n = ((p * Directions.Sides) - position).filter(updatedCells.get(_) == '.')
          if (n.isEmpty) (Set(p) :: ls1, ls2) else (ls1, (n + p) :: ls2)
        }
        val strings1 = ls2.foldLeft(List[Set[Position]]()) { (acc, ps) =>
          if (acc.exists(!_.intersect(ps).isEmpty)) acc.map(e => if (e.intersect(ps).isEmpty) e else e ++ ps)
          else ps :: acc
        }
        val strings2 = strings1.foldLeft(strings1) { (s, ps) =>
          if (s.exists(!_.intersect(ps).isEmpty)) {
            val (tmp1, tmp2) = s.foldLeft((List.empty[Set[Position]], Set.empty[Position])) { (acc, e) =>
              val (tmp1, tmp2) = acc
              if (e.intersect(ps).isEmpty) (e :: tmp1, tmp2) else (tmp1, e ++ ps)
            }
            tmp2 :: tmp1
          }
          else ps :: s
        }
        val islands = ls1.map(GoString(_, Set()))
        val updatedStrings = strings2.map(s => GoString(s, s))
        val stringsFromCaptures = captures.map(e => GoString(e.in, if (e.in.size == 1) Set() else e.in))
        unconcernedSpaceString ++ stringsFromCaptures ++ islands ++ updatedStrings
      }
    val updatedLayers = Map(character -> Layer(updatedSelves2), opponent(character) -> Layer(updatedOthers), '.' -> Layer(updatedSpaceStrings))
    GoBoard(updatedCells, updatedLayers)
  }

  def buildConsoleView(data: Array[String]) = {
    val stringTopBottom = " " + "+" + "-" * (data(0).length) + "+" + "\n"
    val out0 = "  " + (0 until data(0).length).map(_ % 10).mkString + "\n" + stringTopBottom
    data.foldLeft(out0)((out, in) => out + ((out.count(_ == '\n') - 2) % 10) + "|" + in + "|\n") + stringTopBottom
  }

  // TODO functional way  
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

  private sealed case class CellData(id: Int, in: Set[Position], out: Set[Position])

  // TODO functional way
  private def buildMapOfCellDataByPosition(color: Char, cells: Cells[Char]): collection.mutable.Map[Position, CellData] = {
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

  def apply(data: Array[String]) = {
    val cells = buildCells(data, '?', '!')
    val layers = Map(
      'O' -> Layer(computeStrings('O', cells)),
      'X' -> Layer(computeStrings('X', cells)),
      '.' -> Layer(computeStrings('.', cells)))
    new GoBoard(cells, layers)
  }

  def main(args: Array[String]) {
    val data = Array(
      ".....",
      ".....",
      ".....",
      ".....",
      "....."
    )
    val board = GoBoard(data)
    println(board)
    val newBoard = board.play(Position(2, 2), 'X')
    println(newBoard)
  }

}

sealed case class GoString(in: Set[Position], out: Set[Position]) {
  override def toString = "\n  in:\n    " + this.in.mkString("\n    ") + "\n  out:\n    " + this.out.mkString("\n    ") + "\n"
}

sealed case class Layer(strings: Set[GoString]) {}

sealed case class GoBoard(cells: Cells[Char], layers: Map[Char, Layer]) {
  lazy val consoleView = buildConsoleView(cellsToArray(this.cells))
  def layer(character: Char) = layers(character)
  def play(position: Position, character: Char) = update(this, position, character)
  override def toString = this.consoleView
}
