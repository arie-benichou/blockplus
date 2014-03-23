package games.go

import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet

object GoGame {

  private def opponent(character: Char) = {
    if (character == 'O') 'X' else if (character == 'X') 'O' else error("Unknown Character")
  }

  private def play(data: Array[String], character: Char, position: Position, debug: Boolean) = {
    val clone = data.clone
    clone.update(position.row, clone(position.row).updated(position.column, character))
    val board = GoBoard(clone)
    val stringsForOpponent = board.layer(opponent(character)).strings
    val captures = stringsForOpponent.filter(_.out.isEmpty).map(_.in)
    captures.foreach { string =>
      string.foreach { position =>
        clone.update(position.row, clone(position.row).updated(position.column, '.')) // TODO parameterize
      }
    }
    if (debug) println(captures)
    clone
  }

  private def computeGlobalFreedom(board: GoBoard, character: Char): Int = {
    val s = board.layer(character).strings
    s.foldLeft(0)((sum, string) => sum + string.in.size * string.out.size)
  }

  private def evaluateOptions(options: Set[Position], character: Char, board: GoBoard) = {
    val evaluations = options.map(p => (p, computeGlobalFreedom(GoBoard(play(board.data, character, p, false)), character))).toMap
    val groupedEvaluations = evaluations.groupBy(_._2).mapValues(SortedSet() ++ _.keySet)
    TreeMap(groupedEvaluations.toSeq: _*)(math.Ordering.Int.reverse)
  }

  def main(args: Array[String]) {

    val character = 'O'

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

    var player = character
    var next = data
    var board = GoBoard(next)
    var options = GoOptions(player, board)
    var history = List.empty[Position]

    while (history.isEmpty || history.take(2) != List(Position(-1, -1), Position(-1, -1))) {

      if (!options.isEmpty) {
        val evaluatedOptions = evaluateOptions(options, player, board)

        // TODO shouldNotPlay
        val shouldPassToo =
          if (!history.isEmpty && history.head == Position(-1, -1)) {
            computeGlobalFreedom(board, player) > evaluatedOptions.firstKey
          }
          else false

        if (shouldPassToo) {
          println("=================================")
          println("Player '" + player + "'" + " has passed")
          history = Position(-1, -1) :: history
        }
        else {
          val selectedPosition =
            if (player == 'O')
              evaluatedOptions.head._2.iterator.next
            else options.toList(util.Random.nextInt(options.size))

          println("=================================")
          println("player  : " + player)
          //            println("options : " + options.size)
          //            //          options.foreach { opt =>
          //            //            println("          " + opt)
          //            //          }
          //            println(board)
          println("move    : " + selectedPosition)
          println

          history = selectedPosition :: history
          next = play(next, player, selectedPosition, false)
          board = GoBoard(next)

          println(board)
          println("score   : " + computeGlobalFreedom(board, character))
        }
      }

      else {
        println("=================================")
        println("No option left for player '" + player + "'")
        history = Position(-1, -1) :: history
      }

      player = opponent(player)
      options = GoOptions(player, board)

    }

    println("=================================")

  }

}