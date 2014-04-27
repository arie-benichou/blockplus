package games.blokus

import components.Positions
import components.Positions.Ordering
import components.Positions.Position
import games.blokus.Game.BlokusContext
import games.blokus.Game.Move
import games.blokus.Polyominos.Instances.Instance
import games.blokus.Polyominos.Polyomino

object Main {

  private object PolyominoOrdering extends Ordering[Polyomino] {
    def compare(p1: Polyomino, p2: Polyomino) = {
      val diff = p1.order - p2.order
      if (diff > 0) 1 else if (diff < 0) -1 else p1.instances.head.lights.size - p2.instances.head.lights.size
    }
  }

  private def choose(options: Set[(Position, Polyomino, Set[Position])]) = {
    val options2 = options.groupBy(_._2)
    val options3 = options2.get(options2.keySet.max(PolyominoOrdering)).get
    val options4 = options3.groupBy(_._1)
    val options5 = options4.get(options4.keySet.max).get
    options5.head
  }

  private def positionsToInstance(polyomino: Polyomino, positions: Set[Position]) = {
    val topLeftCorner = Positions.topLeftCorner(positions)
    val normalizedPositions = positions.map(_ + (Positions.Origin - topLeftCorner))
    val normalizedInstance = polyomino.instances.find(_.positions == normalizedPositions).get
    normalizedInstance.translateBy(topLeftCorner - Positions.Origin)
  }

  private def renderer(context: BlokusContext, light: Position, positions: Set[Position], instance: Instance): Unit = {
    println(""
      + "================\n"
      + context.id
      + " -> (" + light.row + "," + light.column + ")"
      + "\n================\n"
      + positions.mkString("\n")
      + "\n----------------\n"
      + instance
      + "\n"
    )
  }

  def nullRenderer(context: BlokusContext, light: Position, positions: Set[Position], instance: Instance) {}

  def run(context: BlokusContext, renderer: (BlokusContext, Position, Set[Position], Instance) => Unit): BlokusContext = {
    if (context.isTerminal) context
    else {
      val options = Options.get(context.id, context.space, context.sideToPlay.values)
      val (light, polyomino, positions) = choose(options)
      val instance = positionsToInstance(polyomino, positions)
      val move = Move(context.id, instance)
      val nextContext = context(move)
      renderer(nextContext, light, positions, instance)
      run(nextContext, renderer)
    }

  }

  def main(args: Array[String]) {

    val initialContext = Game.context
    val terminalContext = run(initialContext, renderer)

    println
    println("Game Over !")
    println

    for ((color, side) <- terminalContext.sides) {
      println(color + ":" + Game.score(terminalContext, color))
      //side.values.polyominos.foreach(x => println("\n" + x.instances.head))
    }

    /*
    var board2 = Board(20, 20)
    path.split('|').foreach(x =>
      {
        val color = colorByChar.get(x.head).get
        val tail = x.tail
        val positions =
          if (tail.isEmpty()) Set.empty[Position]
          else {
            tail.split('-').map(x => {
              val rowAndColumn = x.split(':').map(y => Integer.parseInt(y))
              Position(rowAndColumn(0), rowAndColumn(1))
            }).toSet
          }
        val instance = positionsToPolyomino(positions)
        board2 = board2.apply(color, instance.positions, instance.shadows, instance.lights)
      }
    )

      def boardToString(board: Board) = {
        board.layers.keySet.map({ color =>
          color.toString().charAt(0) + board.selves(color).map(p => p.row + ":" + p.column).mkString("", "-", "")
        }).mkString("|")
      }
      */

  }

}