package games.go

import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet

object GoGame {

  private def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  private def computeGlobalFreedom(board: GoBoard, character: Char): Double = {
    val s = board.layer(character).strings
    val f = s.foldLeft(0)((sum, string) => sum + string.in.size * string.out.size)
    val n = s.size
    2 * f / (n + 1)
  }

  private def evaluateBoard(character: Char, board: GoBoard, nextBoard: GoBoard): Double = {
    val n0 = board.cells.filter(_._2 == opponent(character)).size
    val n1 = nextBoard.cells.filter(_._2 == opponent(character)).size
    val globalFreedom = computeGlobalFreedom(nextBoard, character)
    val protectedLands = GoLands(character, nextBoard).size
    (1 + n0 - n1) * globalFreedom * 4 * (1 + protectedLands)
  }

  private def evaluateOption(character: Char, board: GoBoard, p: Position, level: Int): Double = {
    val nextBoard = board.play(p, character)
    val score = evaluateBoard(character, board, nextBoard)
    if (level == 0) score else {
      val opponentOptions = GoOptions(opponent(character), nextBoard)
      if (opponentOptions.isEmpty) score
      else score - evaluateOptions(opponentOptions, opponent(character), nextBoard, level - 1).firstKey
    }
  }

  private def evaluateOptions(options: Set[Position], character: Char, board: GoBoard, level: Int) = {
    val evaluations = options.map(p => (p, evaluateOption(character, board, p, level))).toMap
    val groupedEvaluations = evaluations.groupBy(_._2).mapValues(SortedSet() ++ _.keySet)
    TreeMap(groupedEvaluations.toSeq: _*)(math.Ordering.Double.reverse)
  }

  private def askForOption(inputToPosition: String => Position, options: Set[Position], nullOption: Position) = {
    var line = ""
    var selectedPosition = nullOption
    do {
      System.out.println("Enter coordinates for 'X' :");
      line = scala.Console.readLine
      if (line == "pass") selectedPosition = nullOption else selectedPosition = inputToPosition(line)
    } while (line != "pass" && !options.contains(selectedPosition))
    selectedPosition
  }

  def main(args: Array[String]) {
    val character = 'O'
    val data = Array(
      ".......",
      ".......",
      ".......",
      ".......",
      ".......",
      ".......",
      "......."
    )

    val letters = "ABCDEFGHJ".take(data(0).length())
    val columns = letters.zipWithIndex.toMap
    val numbers = (1 to data.length).toList.reverse.mkString("")
    val rows = numbers.zipWithIndex.toMap
    def inputToPosition(line: String) = Position(rows(line(1)), columns(line(0).toUpper))
    def positionToInput(p: Position) = "" + letters(p.column) + numbers(p.row)
    var player = character
    var board = GoBoard(data)
    var options = GoOptions(player, board)
    var history = List.empty[Position]
    val scores = collection.mutable.Map[Char, Double]().withDefaultValue(0)
    val nullOption = Position(-1, -1)
    while ((history.isEmpty && !options.isEmpty) || history.take(2) != List(nullOption, nullOption)) {
      if (!options.isEmpty) {
        val selectedPosition =
          if (player == 'O' /*|| player == 'X'*/ ) {
            val evaluatedOptions = if (player == 'O') evaluateOptions(options, player, board, 1)
            else evaluateOptions(options, player, board, 0)
            val shouldPassToo = { // TODO shouldNotPlay
              if (!history.isEmpty && history.head == nullOption) evaluatedOptions.head._1 < scores(player) else false
            }
            if (shouldPassToo) {
              println("=================================")
              println("Player '" + player + "'" + " has passed")
              nullOption
            }
            else {
              scores.update(player, evaluatedOptions.head._1)
              val bestOptions = evaluatedOptions.head._2
              //bestOptions.toList(util.Random.nextInt(bestOptions.size))
              bestOptions.head
            }
          }
          else {
            //askForOption(inputToPosition, options, nullOption)
            //options.toList(util.Random.nextInt(options.size))
            options.head
          }
        history = selectedPosition :: history
        if (selectedPosition != nullOption) {
          println("=================================")
          println("player  : " + player)
          println("move    : " + selectedPosition)
          println(positionToInput(selectedPosition))
          println
          board = board.play(selectedPosition, player)
          println(board)
          println("score   : " + scores(player))
        }
      }
      else {
        println("=================================")
        println("No option left for player '" + player + "'")
        history = nullOption :: history
      }
      player = opponent(player)
      options = GoOptions(player, board)
      Thread.sleep(125)
    }
    println("=================================")
    println("Game Over")
  }

}