package games.go

import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet

// TODO compute protected land
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

  private def computeGlobalFreedom(board: GoBoard, character: Char): Double = {
    val s = board.layer(character).strings
    val f = s.foldLeft(0)((sum, string) => sum + string.in.size * string.out.size)
    val n = s.size
    2 * f / (n + 1)
  }

  private def evaluateBoard(character: Char, board: GoBoard, nextBoard: GoBoard): Double = {

    val n0 = board.cells.filter(_._2 == opponent(character)).size
    val n1 = nextBoard.cells.filter(_._2 == opponent(character)).size

    val diff = (n0 - n1) + 1

    val globalFreedom = computeGlobalFreedom(nextBoard, character)
    val protectedLands = GoLands(character, nextBoard).size + 1 // ? avoid playing in protected lands

    diff * globalFreedom * 4 * protectedLands
  }

  private def _evaluateOption(character: Char, board0: GoBoard, p: Position): Double = {
    val nextBoard = GoBoard(play(board0.data, character, p, false))
    evaluateBoard(character, board0, nextBoard)
  }

  private def evaluateOption(character: Char, board0: GoBoard, p: Position, level: Int): Double = {
    val nextBoard = GoBoard(play(board0.data, character, p, false))
    val score = evaluateBoard(character, board0, nextBoard)
    if (level == 0) {
      score
    }
    else {
      val opponentOptions = GoOptions(opponent(character), nextBoard)
      if (opponentOptions.isEmpty) {
        score
      }
      else {
        score - evaluateOptions(opponentOptions, opponent(character), nextBoard, level - 1).firstKey
      }
    }
  }

  private def evaluateOptions(options: Set[Position], character: Char, board: GoBoard, level: Int) = {
    val evaluations = options.map(p => (p, evaluateOption(character, board, p, level))).toMap
    val groupedEvaluations = evaluations.groupBy(_._2).mapValues(SortedSet() ++ _.keySet)
    TreeMap(groupedEvaluations.toSeq: _*)(math.Ordering.Double.reverse)
  }

  def main(args: Array[String]) {

    val character = 'O'

    val _data = Array(
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

    val __data = Array(
      ".......",
      ".......",
      ".......",
      ".......",
      ".......",
      ".......",
      "......."
    )

    val data = Array(
      ".....",
      ".....",
      ".....",
      ".....",
      "....."
    )

    val letters = "ABCDEFGHJ".take(data(0).length())
    val columns = letters.zipWithIndex.toMap
    val numbers = (1 to data.length).toList.reverse.mkString("")
    val rows = numbers.zipWithIndex.toMap

    def inputToPosition(line: String) = Position(rows(line(1)), columns(line(0).toUpper))
    def positionToInput(p: Position) = "" + letters(p.column) + numbers(p.row)

    var player = character
    var next = data
    var board = GoBoard(next)
    var options = GoOptions(player, board)
    var history = List.empty[Position]

    val scores = collection.mutable.Map[Char, Double]().withDefaultValue(0)

    while (history.isEmpty || history.take(2) != List(Position(-1, -1), Position(-1, -1))) {
      //for (i <- 1 to 125) {

      if (!options.isEmpty) {

        val selectedPosition =
          if (player == 'O' /*|| player == 'X'*/ ) {
            val evaluatedOptions = evaluateOptions(options, player, board, 3)
            // TODO shouldNotPlay
            val shouldPassToo = {
              if (!history.isEmpty && history.head == Position(-1, -1))
                evaluatedOptions.head._1 < scores(player)
              else false
            }
            if (shouldPassToo) {
              println("=================================")
              println("Player '" + player + "'" + " has passed")
              history = Position(-1, -1) :: history
              Position(-1, -1)
            }
            else {
              scores.update(player, evaluatedOptions.head._1)
              val bestOptions = evaluatedOptions.head._2
              bestOptions.toList(util.Random.nextInt(bestOptions.size))
            }
          }
          else {
            var selectedPosition = Position(-1, -1)
            do {
              System.err.println("Enter coordinates for X: ");
              val line = scala.Console.readLine
              selectedPosition = inputToPosition(line)
            } while (!options.contains(selectedPosition))
            selectedPosition

            //options.toList(util.Random.nextInt(options.size))
            //options.toList.head
          }

        if (selectedPosition != Position(-1, -1)) {

          println("=================================")
          println("player  : " + player)
          //            println("options : " + options.size)
          //            //          options.foreach { opt =>
          //            //            println("          " + opt)
          //            //          }
          //            println(board)
          println("move    : " + selectedPosition)
          println(positionToInput(selectedPosition))
          println

          history = selectedPosition :: history
          next = play(next, player, selectedPosition, false)
          board = GoBoard(next)

          println(board)
          println("score   : " + scores(player))
        }
      }

      else {
        println("=================================")
        println("No option left for player '" + player + "'")
        history = Position(-1, -1) :: history
      }

      player = opponent(player)
      options = GoOptions(player, board)
      println
      //println(i)

    }

    println("=================================")

  }

}