package games.go

import scala.annotation.tailrec

import games.go.GoGame.GoContext
import games.go.GoGame.Move

object GoMain {

  private implicit def renderer(context: GoContext) = {
    println("=================================")
    if (context.path.isEmpty) {
      println("\nNew Game\n")
      println("=================================")
      println
      println(context.space)
    }
    else {
      val lastMove = context.path.head
      if (lastMove.data == GoGame.NullOption) {
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

  private def choose(context: GoContext) = {
    val options = context.space.layer(context.id).options.intersect(context.space.mainSpaces)
    if (options.isEmpty) GoGame.NullOption
    else if (options.size == 1) options.head
    else {
      if (context.sideToPlay == context.side('O')) {
        val evaluatedOptions = GoEvaluation.evaluateOptions(options, context.id, context.space, 0)
        // TODO shouldPass
        val shouldPassToo = if (!context.path.isEmpty && context.path.head == Move('X', GoGame.NullOption))
          evaluatedOptions.head._1 < GoEvaluation.evaluateBoard('O', context.space, context.space)
        else false
        if (shouldPassToo) GoGame.NullOption
        else {
          val bestOptions = evaluatedOptions.head._2
          bestOptions.head //.toList(util.Random.nextInt(bestOptions.size))
        }
      }
      else options.head //.toList(util.Random.nextInt(options.size))
    }
  }

  @tailrec
  private def run(context: GoContext)(implicit renderer: GoContext => Unit): GoContext = {
    renderer(context)
    if (context.isTerminal) context else run(context(Move(context.id, choose(context))).forward)
  }

  def main(args: Array[String]) {
    val terminalContext = run(GoGame.context)
  }

}