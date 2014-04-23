package games.go

import GoBoard._
import components.Cells
import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.SortedSet
import scala.annotation.tailrec

object GoBoard {

  // TODO inject function
  def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  private def updateFromCells1(board: GoBoard, position: Position, character: Char) = {
    val update1 = GoBoard(board.cells.apply(Map(position -> character)))
    val stringsForOpponent = update1.layer(opponent(character)).strings
    val capturedPositions = stringsForOpponent.filter(_.out.isEmpty).flatMap(s => s.in)
    GoBoard(update1.cells.apply(capturedPositions.foldLeft(Map[Position, Char]())((map, p) => map + (p -> '.'))))
  }

  private def updateFromCells2(board: GoBoard, position: Position, character: Char) = {
    val capturedPositions = board.layer(opponent(character)).strings.filter(_.out == Set(position)).flatMap(s => s.in)
    GoBoard(board.cells.apply(capturedPositions.foldLeft(Map(position -> character))((map, p) => map + (p -> '.'))))
  }

  private def updateFromStrings(board: GoBoard, position: Position, character: Char) = {
    val others = board.layer(opponent(character)).strings
    val (othersFilterYes, othersFilterNo) = others.partition(e => e.out.contains(position))
    val mutatedOutsForOthers = othersFilterYes.map(e => GoString(e.in, e.out - position))
    val (captures, mutatedOutsForOthersWithoutCaptures) = mutatedOutsForOthers.partition(_.out.isEmpty)
    val updatedOthers = othersFilterNo ++ mutatedOutsForOthersWithoutCaptures
    val capturedPositions = captures.flatMap(s => s.in)
    val selves = board.layer(character).strings
    val (connexions, notConnexions) = selves.partition(e => e.out.contains(position))
    val ins = connexions.flatMap(_.in) + position
    val outs = connexions.flatMap(_.out) - position ++ (position * Directions.Sides).filter(board.cells.get(_) == '.')
    val updatedSelves1 = Set(GoString(ins, outs)) ++ notConnexions
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
    val (concernedSpaceString, unconcernedSpaceString) = common.partition(_.out.contains(position))
    val updatedSpaceStrings =
      if (concernedSpaceString.isEmpty)
        unconcernedSpaceString.filterNot(e => e.in.contains(position)) ++ captures.map(e => GoString(e.in, if (e.in.size == 1) Set() else e.in))
      else {
        @tailrec
        def f(acc: List[Position], visited: Set[Position]): Set[Position] = {
          if (acc.isEmpty) visited
          else {
            val spaceAround = (acc.head * Directions.Sides).filter(e => updatedCells.get(e) == '.' && !visited.contains(e))
            if (spaceAround.isEmpty) f(acc.tail, visited + acc.head) else f(acc.tail ++ spaceAround, visited + acc.head)
          }
        }
        val spaceAround = (position * Directions.Sides).filter(updatedCells.get(_) == '.')
        val (ls, _) = spaceAround.foldLeft((List[Set[Position]](), Set[Position]())) { (tuple, p) =>
          val (ls, visited) = tuple
          if (visited.contains(p)) tuple
          else {
            val connected = f(List(p), Set())
            (connected :: ls, visited ++ connected)
          }
        }
        val newStrings = ls.map(e => GoString(e, if (e.size == 1) Set() else e))
        val stringsFromCaptures = captures.map(e => GoString(e.in, if (e.in.size == 1) Set() else e.in))
        val updatedSpaceStrings = unconcernedSpaceString ++ newStrings ++ stringsFromCaptures
        updatedSpaceStrings
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

  def apply(cells: Cells[Char]) = {
    val layers = Map(
      'O' -> Layer(computeStrings('O', cells)),
      'X' -> Layer(computeStrings('X', cells)),
      '.' -> Layer(computeStrings('.', cells)))
    new GoBoard(cells, layers)
  }

  def apply(data: Array[String]) = {
    val cells = buildCells(data, '?', '!')
    val layers = Map(
      'O' -> Layer(computeStrings('O', cells)),
      'X' -> Layer(computeStrings('X', cells)),
      '.' -> Layer(computeStrings('.', cells)))
    new GoBoard(cells, layers)
  }

  def reduce(board: GoBoard) = {
    val buffer = ListBuffer[Position]()
    for (row <- 0 to board.cells.rows) {
      for (column <- 0 to board.cells.columns) {
        val p = Position(row, column)
        if (board.cells.get(p) == '.') {
          val neighbours = (p * Directions.AllAround).filter(board.cells.get(_) != '!')
          val n = neighbours.count(board.cells.get(_) == '.')
          if (n != neighbours.size) buffer += p
        }
      }
    }
    buffer.toSet
  }

}

sealed case class GoString(in: Set[Position], out: Set[Position]) {
  override def toString = "\n  in:\n    " + this.in.mkString("\n    ") + "\n  out:\n    " + this.out.mkString("\n    ") + "\n"
}

sealed case class Layer(strings: Set[GoString]) {}

sealed case class GoBoard(cells: Cells[Char], layers: Map[Char, Layer]) {
  lazy val consoleView = buildConsoleView(cellsToArray(this.cells))
  def layer(character: Char) = layers(character)
  //def play(position: Position, character: Char) = updateFromCells1(this, position, character)
  def play(position: Position, character: Char) = updateFromCells2(this, position, character)
  //def play(position: Position, character: Char) = updateFromStrings(this, position, character)
  override def toString = this.consoleView
}
