package games.go

import components.Cells
import components.Positions._
import collection.immutable.TreeMap
import collection.mutable.Map
import org.specs2.internal.scalaz.syntax.std.ToTupleOps

// TODO IHM humain-ordi, fixe répétition, réfléchir à la mise à jour incrémentale du jeu      

object Main {

  object Evaluation {
    def apply(a: Evaluation, b: Evaluation) = new Evaluation(a.in - b.in, a.out - b.out)
  }

  sealed case class Evaluation(in: Int, out: Int)

  sealed case class CellData(connexions: Set[Position], id: Int, freedomSet: Set[Position])

  object EvaluationOrdering extends Ordering[Evaluation] {
    def compare(a: Evaluation, b: Evaluation): Int =
      if (a.in < b.in) -1
      else if (a.in > b.in) 1
      else if (a.out < b.out) -1
      else if (a.out > b.out) 1
      else 0
  }

  def buildCells(data: Array[String], initial: Char): Cells[Char] = {
    val rows = data.length
    val columns = if (rows == 0) 0 else data(0).length
    val cells = Cells(rows, columns, initial, '?', collection.immutable.Map.empty[Position, Char])
    val mutations = collection.immutable.Map[Position, Char]() ++ (for {
      row <- 0 until rows
      column <- 0 until columns
    } yield (Position(row, column), data(row).charAt(column)))
    cells(mutations)
  }

  def spaceMap(color: Char, data: Array[String], board: components.Cells[Char]): Map[components.Positions.Position, Int] = {
    val space = buildCells(data, color);
    val sortedSpace = TreeMap(space.data.toSeq: _*)

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

  def paths(color: Char, board: components.Cells[Char]): Map[components.Positions.Position, CellData] = {
    var maxId = 0
    var currentId = maxId
    val paths = Map[Position, CellData]().withDefaultValue(CellData(Set.empty[Position], 0, Set.empty))
    val sorted = TreeMap(board.data.toSeq: _*)
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
                paths.put(p, CellData(data.connexions, min, data.freedomSet))
              }
            }
            currentId = min
          }
        }
        paths.put(position, CellData(connexions, currentId, freedom))
      }
    }
    paths
  }

  @deprecated // See options calculator
  def pathFreedom(color: Char, board: components.Cells[Char]): Int = {
    val pathsMap = paths(color, board)
    var out = 0
    val group = pathsMap.groupBy(e => e._2.id)
    for ((k, v) <- group) {
      val freedomMax = v.values.map(_.freedomSet.size).max
      val freedomSum = v.values.foldLeft(0)((freedom, value) => freedom + value.freedomSet.size)
      val incr = freedomMax * freedomSum
      out += incr
    }
    out
  }

  sealed case class Evaluator() {
    def evaluate(color: Char, data: Array[String]): Evaluation = {
      val board = buildCells(data, ' ')
      val space = spaceMap(color, data, board)
      val lockedPositions = space.filter(_._2 == 0)
      val almostLockedPositions = space.filter(_._2 == 1)
      val in = 2 * lockedPositions.size + almostLockedPositions.size
      val out = pathFreedom(color, board)
      Evaluation(in, out)
    }
  }

  def opponent(color: Char): Char = if (color == 'O') 'X' else if (color == 'X') 'O' else error("Unknown Color")

  sealed case class OptionsCalculator() {
    def calculate(color: Char, data: Array[String]): List[Position] = {
      val board = buildCells(data, ' ')
      val space = spaceMap(color, data, board)
      val ambiguousPositions1 = space.filter(_._2 < 2).keySet
      val opponentColor = opponent(color)
      val oSpace = spaceMap(opponentColor, data, board)
      val ambiguousPositions2 = oSpace.filter(_._2 < 2).keySet
      val ambiguousPositions = ambiguousPositions1 ++ ambiguousPositions2
      val pathsMap = paths(color, board)
      val pathsById = pathsMap.groupBy(e => e._2.id).mapValues(_.keySet)
      val pathFreedom = pathsById.mapValues(p => (p, p.flatMap(p => pathsMap(p).freedomSet)))
      val pathsInDanger = pathFreedom.filter(_._2._2.size < 2)
      val illegalPositions1 = for {
        p <- ambiguousPositions
        sides <- List((p * Directions.Sides).filter(board.get(_) == color))
        if sides.exists(p => pathsInDanger.exists(_._2._1.contains(p)))
      } yield p
      val illegalPositions2 = oSpace.filter(_._2 == 0).keySet
      val illegalPositions = illegalPositions1 ++ illegalPositions2
      val filteredSpace = space.filterNot(e => illegalPositions.contains(e._1))
      filteredSpace.keySet.toList.sorted
    }
  }

  def play(data: Array[String], color: Char, position: Position) = {
    val clone = data.clone
    clone.update(position.row, clone(position.row).updated(position.column, color))
    val board = buildCells(clone, ' ')
    val pathsMap = paths(opponent(color), board)
    val group = pathsMap.groupBy(_._2.id)
    for ((k, v) <- group) {
      if (v.forall(_._2.freedomSet.isEmpty)) {
        v.foreach { e =>
          val capturedPositions = e._2.connexions
          capturedPositions.foreach(p => clone.update(p.row, clone(p.row).updated(p.column, ' '))
          )
        }
        v.keySet.foreach(p => clone.update(p.row, clone(p.row).updated(p.column, ' ')))
      }
    }
    clone
  }

  def main(args: Array[String]) {

    val letters = "ABCDEFGHJ"
    val columns = letters.zipWithIndex.toMap
    val numbers = "987654321"
    val rows = numbers.zipWithIndex.toMap
    def inputToPosition(line: String) = Position(rows(line(1)), columns(line(0).toUpper))
    def positionToInput(p: Position) = "" + letters(p.column) + numbers(p.row)
    //println(positionToInput(inputToPosition("a9")))

    val color = 'O'

    val data = Array(
      "         ",
      "         ",
      "         ",
      "         ",
      "         ",
      "         ",
      "         ",
      "         ",
      "         "
    )

    val evaluator = Evaluator()

    val optionsCalculator = OptionsCalculator()

    var updatedData = data
    var colorToPlay = color

    updatedData.foreach(println)

    var j = 0
    var i = 0

    do {

      val options = optionsCalculator.calculate(colorToPlay, updatedData)

      if (!options.isEmpty) {

        val evaluations = options.map { p =>
          val tmp = play(updatedData, colorToPlay, p)
          Evaluation(evaluator.evaluate(colorToPlay, tmp), evaluator.evaluate(opponent(colorToPlay), tmp))
        }

        val map = (options zip evaluations).toMap
        val group = map.groupBy(_._2).mapValues(_.keys.toSet)
        val sortedEvaluatedOptions = TreeMap(group.toSeq: _*)(EvaluationOrdering.reverse)

        val chosenPosition =
          if (colorToPlay == 'O') {
            val head = sortedEvaluatedOptions.head
            head._2.iterator.next
          }
          else {
            System.err.println("Enter coordinates for X: ");
            val line = scala.Console.readLine
            inputToPosition(line)
            //val ls = sortedEvaluatedOptions.keySet.toList
            //sortedEvaluatedOptions(ls(util.Random.nextInt(sortedEvaluatedOptions.size))).iterator.next
          }

        updatedData = play(updatedData, colorToPlay, chosenPosition)

        val evaluationForPlayer1 = evaluator.evaluate(colorToPlay, updatedData)
        val evaluationForPlayer2 = evaluator.evaluate(opponent(colorToPlay), updatedData)
        val evaluation = Evaluation(evaluationForPlayer1, evaluationForPlayer2)

        j += 1

        println("#" + j + " " + chosenPosition + " " + colorToPlay)
        updatedData.foreach(println)
        println(evaluation)
        println(positionToInput(chosenPosition))
        println

      }

      colorToPlay = opponent(colorToPlay)
      i += 1

    } while (i < 2 * j)

  }

}