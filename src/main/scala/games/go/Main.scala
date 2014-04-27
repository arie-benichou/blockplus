package games.go

import scala.annotation.tailrec
import games.go.Game.GoContext
import games.go.Game.Move
import components.Positions._

object GoMain {

  private implicit def renderer(context: GoContext) {
    println("=================================")
    if (context.history.isEmpty) {
      println("\nNew Game\n")
      println("=================================")
      println
      println(context.space)
    }
    else {
      val lastMove = context.lastMove
      if (lastMove.data == Game.NullOption) {
        println
        println("player " + lastMove.side + " has passed")
        println
      }
      else {
        println
        println("player  : " + lastMove.side)
        println("move    : " + lastMove.data)
        println
        println(context.space)
      }
    }
    if (context.isTerminal) {
      println("=================================")
      println("\nGame is over !\n")
      println("=================================")
    }
  }

  def avoidDuplicateBoard(context: GoContext, ls: List[Position]): Position = {
    if (ls.isEmpty) Game.NullOption
    else if (context.isLegal(Move(context.id, ls.head), context)) ls.head
    else avoidDuplicateBoard(context, ls.tail)
  }

  private def choose(context: GoContext) = {
    val options = context.space.layer(context.id).options.intersect(context.space.mainSpaces)
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
        else evaluatedOptions.values.flatten.toList
      }
      else options.toList
    }
    avoidDuplicateBoard(context, ls)
  }

  @tailrec
  private def run(context: GoContext)(implicit renderer: GoContext => Unit): GoContext = {
    if (context.isTerminal) context else run(context(Move(context.id, choose(context))))
  }

  def main(args: Array[String]) {
    val t0 = System.currentTimeMillis()
    val terminalContext = run(Game.context.onNewContext(renderer))
    val t1 = System.currentTimeMillis()
    println("\nGame played in " + (t1 - t0) / 1000.0 + " seconds")
  }

}