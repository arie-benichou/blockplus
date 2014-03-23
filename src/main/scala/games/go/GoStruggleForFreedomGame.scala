package games.go

import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet

object GoStruggleForFreedomGame {

  private def play(data: Array[String], color: Char, position: Position) = {
    val clone = data.clone
    clone.update(position.row, clone(position.row).updated(position.column, color))
    clone
  }

  private def computeGlobalFreedom(board: GoBoard, character: Char): Int = {
    val s = board.layer(character).strings
    s.foldLeft(0)((sum, string) => sum + string.in.size * string.out.size)
  }

  private def evaluateOptions(options: Set[Position], character: Char, board: GoBoard) = {
    val evaluations = options.map(p => (p, computeGlobalFreedom(GoBoard(play(board.data, character, p)), 'O'))).toMap
    val groupedEvaluations = evaluations.groupBy(_._2).mapValues(SortedSet() ++ _.keySet)
    TreeMap(groupedEvaluations.toSeq: _*)(math.Ordering.Int.reverse)
  }

  def main(args: Array[String]) {

    val data = Array(
      ".........",
      ".........",
      ".........",
      ".........",
      ".........",
      ".........",
      ".........",
      ".........",
      "........."
    )

    var next = data
    var board = GoBoard(next)
    var options = GoOptions('O', board)
    println("=================================\n")
    println(board)

    while (!options.isEmpty) {
      val evaluatedOptions = evaluateOptions(options, 'O', board)
      next = play(next, 'O', evaluatedOptions.head._2.iterator.next)
      board = GoBoard(next)
      options = GoOptions('O', board)
      println("=================================\n")
      println(board)
      val freedom = evaluatedOptions.head._1
      println("number of options: " + options.size)
      println("freedom score: " + freedom)
      if (evaluatedOptions.size == 1) options = Set.empty
    }

  }

}