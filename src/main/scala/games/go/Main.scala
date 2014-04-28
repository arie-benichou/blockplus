package games.go

import scala.annotation.tailrec
import games.go.Game.GoContext
import games.go.Game.Move
import components.Positions._

object GoMain {

  @tailrec
  private def avoidDuplicateBoard(context: GoContext, ls: List[Position]): Position = {
    if (ls.isEmpty) Game.NullOption
    else if (context.isLegal(Move(context.id, ls.head), context)) ls.head
    else avoidDuplicateBoard(context, ls.tail)
  }

  private def chooseMove(context: GoContext) = {
    val options = context.space.layer(context.id).options.intersect(context.space.mainSpaces)
    // TODO use pattern matching
    val ls = if (options.isEmpty) List(Game.NullOption)
    else if (options.size == 1) List(options.head)
    else {
      if (context.sideToPlay == context.side('O')) {
        val evaluatedOptions = Evaluation.evaluateOptions(options, context.id, context.space, 0)
        // TODO shouldPass
        val shouldPassToo = if (!context.history.isEmpty && context.lastMove == Move('X', Game.NullOption))
          evaluatedOptions.head._1 < Evaluation.evaluateBoard('O', context.space, context.space)
        else false
        if (shouldPassToo) List(Game.NullOption)
        else
          evaluatedOptions.values.flatten.toList
      }
      else options.toList
    }
    val position = avoidDuplicateBoard(context, ls)
    Move(context.id, position)
  }

  @tailrec
  private def run(context: GoContext): GoContext = if (context.isTerminal) context else run(context(chooseMove(context)))

  private def renderer(context: GoContext) {
    println("=================================")
    if (context.history.isEmpty) {
      println
      println("New Game")
      println
      println("=================================")
      println
      println(context.space)
    }
    else {
      if (context.lastMove.data != Game.NullOption) {
        println
        println("player  : " + context.lastMove.side)
        println("move    : " + context.lastMove.data)
        println
        println(context.space)
      }
      else {
        println
        println("player " + context.lastMove.side + " has passed")
        println
      }
    }
    if (context.isTerminal) {
      println("=================================")
      println
      println("Game is over !")
      println
      println("=================================")
    }
  }

  def main(args: Array[String]) {
    val t0 = System.currentTimeMillis()
    val terminalContext = run(Game.context.onNewContext(renderer))
    val t1 = System.currentTimeMillis()
    println("\nGame played in " + (t1 - t0) / 1000.0 + " seconds")
  }

}