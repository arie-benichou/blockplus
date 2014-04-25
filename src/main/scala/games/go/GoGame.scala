package games.go

import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer

import abstractions.Adversity
import abstractions.Context
import abstractions.Side
import abstractions.Sides

import components.Positions.Directions
import components.Positions.Ordering
import components.Positions.Position

object GoGame {

  val Board9X9 = Array(
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

  val Board19X19 = Array(
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "...................",
    "..................."
  )

  type GoContext = Context[Char, Char, GoBoard, Position]
  type GoMove = abstractions.Move[Char, Position]

  sealed case class Move(side: Char, data: Position) extends abstractions.Move[Char, Position]

  val NullOption = Position(Integer.MIN_VALUE, Integer.MIN_VALUE)

  private def f1(value: Char, e: Any) = value
  private def f2(value: Char) = true

  private val side1 = Side('O')(f1, f2)
  private val side2 = Side('X')(f1, f2)

  private val sides = Sides(Adversity('O', 'X'), List(side1, side2))

  private def application(move: GoMove, space: GoBoard) = space.play(move.data, move.side)

  // TODO détecter les cycles
  private def isLegal(move: GoMove, context: GoContext) =
    move.data == NullOption || // TODO enlever après debug
      GoOptions(context.id, context.space).contains(move.data) // TODO optimisable

  private def isTerminal(context: GoContext) =
    (context.path.size > 2 && context.path.take(2).toSet == Set(Move('O', NullOption), Move('X', NullOption)))

  val context: GoContext = Context(sides, GoBoard(Board9X9), application, isLegal, isTerminal)

  // TODO use adversity abstraction
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
    val protectedLands = GoLands(character, nextBoard).size
    (1 + n0 - n1) * globalFreedom * 4 * (1 + protectedLands)
  }

  private def reduce(board: GoBoard) = {
    val buffer = ListBuffer[Position]()
    for (row <- 0 to board.cells.max.row) {
      for (column <- 0 to board.cells.max.column) {
        val p = Position(row, column)
        if (board.cells.get(p) == '.') {
          val neighbours = (p * Directions.AllAround).filter(board.cells.get(_) != '?') // TODO
          val n = neighbours.count(board.cells.get(_) == '.')
          if (n != neighbours.size) buffer += p
        }
      }
    }
    buffer.toSet
  }

  private def evaluateOption(character: Char, board: GoBoard, p: Position, level: Int): Double = {
    val nextBoard = board.play(p, character)
    val score = evaluateBoard(character, board, nextBoard)
    if (level == 0) score else {
      val opponentOptions = GoOptions(opponent(character), nextBoard).intersect(reduce(nextBoard))
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