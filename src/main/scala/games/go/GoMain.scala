package games.go

import components.Positions.Position
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
        println("player " + lastMove.side + " has passed")
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

  private def choose(options: Set[Position]) = options.head

  private def run(context: GoContext)(implicit renderer: GoContext => Unit): GoContext = {
    renderer(context)
    if (context.isTerminal) context else run(context(Move(context.id, choose(GoOptions(context.id, context.space)))).forward)
  }

  def main(args: Array[String]) {
    run(GoGame.context)
  }

}