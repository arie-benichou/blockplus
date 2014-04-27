package games.go

import components.Positions._
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeMap

object GoEvaluation {

  // TODO ? use adversity abstraction
  private def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  private def computeGlobalFreedom(board: GoBoard, character: Char): Double = {
    val s = board.layer(character).strings
    val f = s.foldLeft(0)((sum, string) => sum + string.in.size * string.out.size)
    val n = s.size
    2 * f / (n + 1)
  }

  def evaluateBoard(character: Char, board: GoBoard, nextBoard: GoBoard): Double = {
    val n0 = board.cells.filterOthers(_._2 == opponent(character)).size
    val n1 = nextBoard.cells.filterOthers(_._2 == opponent(character)).size
    val globalFreedom = computeGlobalFreedom(nextBoard, character)
    val protectedLands = nextBoard.layer(character).lands.size
    (1 + n0 - n1) * globalFreedom * 4 * (1 + protectedLands)
  }

  private def evaluateOption(character: Char, board: GoBoard, p: Position, level: Int): Double = {
    val nextBoard = board.play(p, character)
    val score = evaluateBoard(character, board, nextBoard)
    if (level == 0) score else {
      val opponentOptions = nextBoard.layer(opponent(character)).options.intersect(nextBoard.mainSpaces)
      if (opponentOptions.isEmpty) score
      else score - evaluateOptions(opponentOptions, opponent(character), nextBoard, level - 1).firstKey
    }
  }

  def evaluateOptions(options: Set[Position], character: Char, board: GoBoard, level: Int) = {
    val evaluations = options.map(p => (p, evaluateOption(character, board, p, level))).toMap
    val groupedEvaluations = evaluations.groupBy(_._2).mapValues(SortedSet() ++ _.keySet)
    TreeMap(groupedEvaluations.toSeq: _*)(math.Ordering.Double.reverse)
  }

}