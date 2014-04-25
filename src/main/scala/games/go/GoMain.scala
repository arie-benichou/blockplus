package games.go

import games.go.GoGame._
import components.Positions._
import scala.collection.immutable.TreeMap
import scala.collection.immutable.SortedSet
import scala.collection.mutable.ListBuffer

object GoMain {

  private def renderer(context: GoContext, move: GoMove) = {
    //println(context.id + " -> (" + move.data.row + "," + move.data.column + ")")
    println(context.space)
    Thread.sleep(1 * 300)
  }

  private def choose(options: Set[Position]) = options.head

  private def run(context: GoContext, renderer: (GoContext, GoMove) => Unit): GoContext = {
    if (context.isTerminal) context
    else {
      val options = GoOptions(context.id, context.space)
      val move = if (context.path.size < 10)
        Move(context.id, choose(options))
      else
        Move(context.id, GoGame.NullOption)
      val nextContext = context(move)
      renderer(nextContext, move)
      run(nextContext.forward, renderer)
    }
  }

  def main(args: Array[String]) {
    val initialContext = GoGame.context
    val terminalContext = run(initialContext, renderer)
    println("\nGame Over !\n")
  }

}