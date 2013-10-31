package games.blokus

import components.Positions
import components.Positions.Ordering
import components.Positions.Position
import games.blokus.Game.Move
import games.blokus.Polyominos.Polyomino
import games.blokus.Game.Color
import games.blokus.Polyominos.Instances.Instance

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

  private def render(color: Color, light: Position, positions: Set[Position], instance: Instance): Unit = {
    println(""
      + "================\n"
      + color
      + " -> (" + light.row + "," + light.column + ")"
      + "\n================\n"
      + positions.mkString("\n")
      + "\n----------------\n"
      + instance
      + "\n"
    )
  }

  def main(args: Array[String]) {

    var context = Game.context
    while (!context.isTerminal) {
      val options = Options.get(context.id, context.space, context.sideToPlay.values)
      val (light, polyomino, positions) = choose(options)
      val instance = positionsToInstance(polyomino, positions)
      val move = Move(context.id, instance)
      context = context(move)
      render(context.id, light, positions, instance)
      context = context.forward
    }

    println
    println("Game Over !")
    println

    //TODO sides iterator
    context.sides.sides.foreach(side => {
      println(side._1)
      println(side._2.values.weight)
      side._2.values.polyominos.foreach(x => println("\n" + x.instances.head))
      println
      println
    })

  }

}