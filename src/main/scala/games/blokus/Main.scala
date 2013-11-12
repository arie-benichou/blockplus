package games.blokus

import components.Positions
import components.Positions.Ordering
import components.Positions.Position
import games.blokus.Game._
import games.blokus.Polyominos.Polyomino
import games.blokus.Game.Color
import games.blokus.Polyominos.Instances.Instance
import abstractions.Context
import scala.collection.immutable.Stack

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

  private def positionsToPolyomino(positions: Set[Position]) = {
    val topLeftCorner = Positions.topLeftCorner(positions)
    val normalizedPositions = positions.map(_ + (Positions.Origin - topLeftCorner))
    val polyomino = Polyominos.values.find(p => p.order == positions.size && p.instances.exists(_.positions == normalizedPositions)).get
    val normalizedInstance = polyomino.instances.find(_.positions == normalizedPositions).get
    normalizedInstance.translateBy(topLeftCorner - Positions.Origin)
  }

  private def pathToString(path: Stack[BlokusMove]) = path.map(move =>
    move.side.toString().charAt(0) + (move.data.positions.map(p => p.row + ":" + p.column)).mkString("-")
  ).mkString("|")

  private def renderer(context: BlokusContext, light: Position, positions: Set[Position], instance: Instance): Unit = {
    println(""
      + "================\n"
      //+ pathToString(context.path) + "\n"
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
      run(nextContext.forward, renderer)
    }

  }

  def main(args: Array[String]) {

    val initialContext = Game.context
    val terminalContext = run(initialContext, renderer)

    println
    println("Game Over !")
    println

    for ((color, side) <- terminalContext.sides) {
      println(color)
      println(side.values.weight)
      side.values.polyominos.foreach(x => println("\n" + x.instances.head))
      println
      println
    }

    val path = pathToString(terminalContext.path)
    println(path)

    //    val colorByChar = Map(
    //      'B' -> Color.Blue,
    //      'Y' -> Color.Yellow,
    //      'R' -> Color.Red,
    //      'G' -> Color.Green
    //    )

    //    var board2 = Board(20, 20)
    //    path.split('|').foreach(x =>
    //      {
    //        val color = colorByChar.get(x.head).get
    //        val tail = x.tail
    //        val positions =
    //          if (tail.isEmpty()) Set.empty[Position]
    //          else {
    //            tail.split('-').map(x => {
    //              val rowAndColumn = x.split(':').map(y => Integer.parseInt(y))
    //              Position(rowAndColumn(0), rowAndColumn(1))
    //            }).toSet
    //          }
    //        val instance = positionsToPolyomino(positions)
    //        board2 = board2.apply(color, instance.positions, instance.shadows, instance.lights)
    //      }
    //    )

    //    println(terminalContext.space == board2)
    //    println(terminalContext.space.layers(Color.Blue).cells.definedPositions == board2.layers(Color.Blue).cells.definedPositions)
    //    println(terminalContext.space.layers(Color.Blue).cells.data == board2.layers(Color.Blue).cells.data)
    //    println(terminalContext.space.layers(Color.Blue).cells.data.keySet == board2.layers(Color.Blue).cells.data.keySet)
    //    println(terminalContext.space.layers(Color.Blue).cells.data.values == board2.layers(Color.Blue).cells.data.values)
    //    val set1 = terminalContext.space.layers(Color.Blue).cells.data.toSet
    //    val set2 = board2.layers(Color.Blue).cells.data.toSet
    //    println(set1.diff(set2))

    //      def boardToString(board: Board) = {
    //        board.layers.keySet.map({ color =>
    //          color.toString().charAt(0) + board.selves(color).map(p => p.row + ":" + p.column).mkString("", "-", "")
    //        }).mkString("|")
    //      }

    //    println(boardToString(terminalContext.space))
    //    println(boardToString(board2))
    //
    //    println(boardToString(terminalContext.space) == boardToString(board2))
  }

}