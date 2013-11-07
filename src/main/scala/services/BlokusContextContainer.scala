package services

import scala.Array.canBuildFrom
import scala.collection.immutable.Stack

import org.json4s.DefaultFormats
import org.json4s.Formats
import org.scalatra.json.JValueResult
import org.scalatra.json.JacksonJsonSupport

import components.Positions
import components.Positions.Position
import games.blokus.Game
import games.blokus.Game.BlokusContext
import games.blokus.Game.BlokusMove
import games.blokus.Game.Color
import games.blokus.Game.Move
import games.blokus.Main
import games.blokus.Options
import games.blokus.Polyominos

object BlokusContextContainer {

  private val colorByChar = Map(
    'B' -> Color.Blue,
    'Y' -> Color.Yellow,
    'R' -> Color.Red,
    'G' -> Color.Green
  )

  private def parseIncomingMoveData(incomingMoveData: String) = {
    val head = incomingMoveData.head
    val color = colorByChar.get(head).get
    val tail = incomingMoveData.tail
    val positions = if (tail.isEmpty) Set.empty[Position] else tail.split('-').map(x => {
      val data = x.split(':').map(y => Integer.parseInt(y))
      Position(data(0), data(1))
    }).toSet
    (color, positions)
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
  ).mkString(",")

  private def forceNullMove(context: BlokusContext): BlokusContext = {
    if (context.isTerminal) context
    else {
      val options = Options.get(context.id, context.space, context.side(context.id).values)
      if (options == Options.Null) {
        val move = Move(context.id, Polyominos._0.instances.head.translateBy((0, 0)))
        println(context.id + " is out now")
        forceNullMove(context.apply(move).forward)
      }
      else context
    }
  }

  private def isNullMove(move: BlokusMove) = move.data.selfType == Polyominos._0

  private def lastMove(context: BlokusContext, side: Color): BlokusMove = {
    val path = context.path.dropWhile(_.side != side)
    if (isNullMove(path.head))
      path.tail.dropWhile(_.side != side).head
    else path.head
  }

  private def isSpecialMove(move: BlokusMove) = move.data.selfType == Polyominos._1

  private def score(context: BlokusContext, id: Color) = {
    val side = context.side(id)
    val pieces = side.values;
    val weight = -pieces.weight
    if (weight != 0) weight
    else if (isSpecialMove(lastMove(context, id))) 20
    else 15
  }

  def main(args: Array[String]) {
    val ctx = Main.run(Game.context, Main.nullRenderer)
    //val ctx = Game.context
    println(lastMove(ctx, Color.Blue))
    println(score(ctx, Color.Blue))
  }

}

class BlokusContextContainer extends GameContextContainer with JacksonJsonSupport with JValueResult {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  var context = Game.context

  get("/context") {
    val ctx = context
    Map(
      "color" -> ctx.id.toString,
      "is-over" -> ctx.isTerminal.toString,
      "path" -> BlokusContextContainer.pathToString(ctx.path).split(','),
      "last-move" -> BlokusContextContainer.pathToString(ctx.path.take(1)),
      "options" -> Options.get(ctx.id, ctx.space, ctx.side(ctx.id).values).map(_._3.map(p => p.row + ":" + p.column).mkString("-")),
      "scores" -> BlokusContextContainer.colorByChar.values.map(c => (c.toString, BlokusContextContainer.score(ctx, c))).toMap
    )
  }

  post("/play/:move") {
    val ctx = context
    val (color, positions) = BlokusContextContainer.parseIncomingMoveData(params("move"))
    try {
      val instance = BlokusContextContainer.positionsToPolyomino(positions)
      val move = Move(color, instance)
      val nextContext = ctx.apply(move)
      if (!ctx.eq(nextContext)) {
        this.synchronized {
          context = BlokusContextContainer.forceNullMove(nextContext.forward)
        }
      }
      else {
        val path = BlokusContextContainer.pathToString(ctx.path)
        println("################################Illegal Instruction################################")
        println("path               : " + path)
        println("query              : " + params("move"))
        println("current side       : " + ctx.id)
        println("incoming side      : " + move.side)
        println("incoming positions : " + positions)
        println(move.data)
        println("http://localhost:8080/static/rendering/?" + path)
      }
    }
    catch {
      case e: Exception => {
        val path = BlokusContextContainer.pathToString(ctx.path)
        println("################################Illegal Instruction################################")
        println("path               : " + path)
        println("query              : " + params("move"))
        println("current side       : " + ctx.id)
        println("incoming positions : " + positions)
        println(e)
        println("http://localhost:8080/static/rendering/?" + path)
      }
    }
    redirect("/context")
  }

}