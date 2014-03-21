package games.go

import components.Cells
import components.Positions
import components.Positions.Ordering
import components.Positions.Position
import components.Positions.Directions
import scala.collection.immutable.SortedMap
import scala.collection.immutable.TreeMap

// TODO fixe suicide des O au coup 108 à partir d'un jeu vide
// TODO fixe répétition
// TODO ? réfléchir à la mise à jour incrémentale du jeu      

object Main {

  object Evaluation {
    def apply(a: Evaluation, b: Evaluation) = new Evaluation(a.in - b.in, a.out - b.out)
  }

  sealed case class Evaluation(in: Int, out: Int)

  object EvaluationOrdering extends Ordering[Evaluation] {
    def compare(a: Evaluation, b: Evaluation): Int =
      if (a.in < b.in) -1
      else if (a.in > b.in) 1
      else if (a.out < b.out) -1
      else if (a.out > b.out) 1
      else 0
  }

  private def buildCells(data: Array[String], initial: Char): Cells[Char] = {
    val rows = data.length
    val columns = if (rows == 0) 0 else data(0).length
    val cells = Cells(rows, columns, initial, '?', Map.empty[Position, Char])
    val mutations = Map[Position, Char]() ++ (for {
      row <- 0 until rows
      column <- 0 until columns
    } yield (Position(row, column), data(row).charAt(column)))
    cells(mutations)
  }

  def spaceMap(color: Char, data: Array[String], board: components.Cells[Char]): scala.collection.mutable.Map[components.Positions.Position, Int] = {
    val space = buildCells(data, color);
    val sortedSpace = SortedMap() ++ space.data
    val spaceMap = collection.mutable.Map[Position, Int]()
    for ((position, char) <- sortedSpace) {
      if (char == ' ') {
        val sides = position * Directions.Sides
        val effectiveSides = sides.filterNot(board.get(_) == '?')
        val connexions = effectiveSides.filter(board.get(_) == color)
        spaceMap.put(position, effectiveSides.size - connexions.size)
      }
    }
    spaceMap
  }

  def paths(color: Char, board: components.Cells[Char]): scala.collection.mutable.Map[components.Positions.Position, games.go.CellData] = {
    var maxId = 0
    var currentId = maxId
    val paths = collection.mutable.Map[Position, CellData]().withDefaultValue(CellData(Set.empty[Position], 0, 0))
    val sorted = SortedMap() ++ board.data
    for ((position, char) <- sorted) {
      if (char == color) {
        val sides = position * Directions.Sides
        val effectiveSides = sides.filterNot(board.get(_) == '?')
        val connexions = effectiveSides.filter(board.get(_) == color)
        val freedom = effectiveSides.filter(board.get(_) == ' ')
        if (connexions.isEmpty) {
          maxId = maxId + 1
          currentId = maxId
        }
        else {
          val ids = connexions.map(paths(_).id)
          if (ids == Set(0)) {
            maxId = maxId + 1
            currentId = maxId
          }
          else {
            val filteredIds = ids.filter(_ > 0)
            val min = filteredIds.min
            val idsToFix = filteredIds.filterNot(_ == min)
            idsToFix.foreach { idToFix =>
              val positionsToFix = paths.filter(e => (e._2.id == idToFix)).keySet
              positionsToFix.foreach { p =>
                val data = paths(p)
                paths.put(p, CellData(data.connexions, min, data.freedom))
              }
            }
            currentId = min
          }
        }
        paths.put(position, CellData(connexions, currentId, freedom.size))
      }
    }
    paths
  }

  def pathFreedom(color: Char, board: components.Cells[Char]): Int = {
    val pathsMap = paths(color, board)
    var out = 0
    val group = pathsMap.groupBy(e => e._2.id)
    for ((k, v) <- group) {
      val freedomMax = v.values.map(_.freedom).max
      val freedomSum = v.values.foldLeft(0)((freedom, value) => freedom + value.freedom)
      out += freedomMax * freedomSum
    }
    out
  }

  sealed case class Evaluator() {
    def evaluate(color: Char, data: Array[String]): Evaluation = {
      val board = buildCells(data, ' ')
      val lockedPositions = spaceMap(color, data, board).filter(_._2 == 0)
      val in = lockedPositions.size
      val out = pathFreedom(color, board)
      Evaluation(in, out)
    }
  }

  def opponent(color: Char): Char = if (color == 'O') 'X' else if (color == 'X') 'O' else error("Unknown Color")

  sealed case class OptionsCalculator() {
    def calculate(color: Char, data: Array[String]): List[Position] = {
      val board = buildCells(data, ' ')
      val opponentColor = opponent(color)
      val suicides = spaceMap(opponentColor, data, board).filter(_._2 == 0).keySet
      val space = spaceMap(color, data, board).filterNot(e => suicides.contains(e._1))
      space.keySet.toList.sorted
    }

  }

  def play(data: Array[String], color: Char, position: Position) = {
    val clone = data.clone
    clone.update(position.row, clone(position.row).updated(position.column, color))
    val board = buildCells(clone, ' ')
    val pathsMap = paths(opponent(color), board)
    val group = pathsMap.groupBy(_._2.id)
    for ((k, v) <- group) {
      if (v.forall(_._2.freedom == 0)) {
        v.foreach { e =>
          val capturedPositions = e._2.connexions
          capturedPositions.foreach { p =>
            clone.update(p.row, clone(p.row).updated(p.column, ' '))
          }
        }
      }
    }
    clone
  }

  def main(args: Array[String]) {

    val evaluator = Evaluator()
    val optionsCalculator = OptionsCalculator()

    val color = 'X'
    //    val data = Array(
    //      "XXX ",
    //      "X X ",
    //      "XXX ",
    //      " XOX",
    //      "OO O",
    //      "O  O",
    //      "OOOO"
    //    )

    val data = Array(
      "  XO     ",
      "  OXO    ",
      "  X      ",
      "         ",
      "   O     ",
      "   OOOO  ",
      "   OXXO  ",
      "   OO O  ",
      "         "
    )

    var updatedData = data
    var colorToPlay = color

    updatedData.foreach(println)
    println

    for (i <- 1 to 9 * 20) {

      println("######################################################")

      val evaluationForPlayer1 = evaluator.evaluate(colorToPlay, updatedData)
      val evaluationForPlayer2 = evaluator.evaluate(opponent(colorToPlay), updatedData)
      val evaluation = Evaluation(evaluationForPlayer1, evaluationForPlayer2)
      val options = optionsCalculator.calculate(colorToPlay, updatedData)
      val evaluations = options.map { p =>
        val tmp = play(updatedData, colorToPlay, p)
        Evaluation(evaluator.evaluate(colorToPlay, tmp), evaluator.evaluate(opponent(colorToPlay), tmp))
      }
      if (!options.isEmpty) {
        val map = (options zip evaluations).toMap
        val group = map.groupBy(_._2).mapValues(_.keys.toSet)
        val sortedMap = TreeMap(group.toSeq: _*)(EvaluationOrdering.reverse)
        val head = sortedMap.head
        val chosenPosition = head._2.iterator.next
        updatedData = play(updatedData, colorToPlay, chosenPosition)

        println(i)
        println("Evaluation for " + colorToPlay)
        println("-------------------")
        //              println(evaluationForPlayer1)
        //              println(evaluationForPlayer2)
        println(evaluation)
        //        println
        //        println("Options for " + colorToPlay)
        //        println("-------------------")
        //        options.foreach(println)
        //      println
        //      sortedMap.foreach(println)
        //      println
        //      println(head)
        println(chosenPosition)

        println
        updatedData.foreach(println)
        println
      }

      colorToPlay = opponent(colorToPlay)

    }

  }

}