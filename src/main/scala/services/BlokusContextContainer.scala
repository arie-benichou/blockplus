package services

import scala.collection.immutable.Stack

import org.json4s.DefaultFormats
import org.json4s.Formats
import org.scalatra.json.JValueResult
import org.scalatra.json.JacksonJsonSupport

import components.Positions.Position
import games.blokus.Game
import games.blokus.Game.BlokusMove
import games.blokus.Game.Color
import games.blokus.Game.Move
import games.blokus.Options

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

  private def pathToString(path: Stack[BlokusMove]) = path.map(move =>
    move.side.toString().charAt(0) + (move.data.positions.map(p => p.row + ":" + p.column)).mkString("-")
  ).mkString(",")

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
      "scores" -> ctx.sides.map(e => (e._1.toString, Game.score(ctx, e._1))).toMap
    )
  }

  post("/play/:move") {
    val ctx = context
    val (color, positions) = BlokusContextContainer.parseIncomingMoveData(params("move"))
    try {
      val instance = Game.positionsToPolyomino(positions)
      val move = Move(color, instance)
      val nextContext = ctx.apply(move)
      if (!ctx.eq(nextContext)) {
        this.synchronized {
          context = Game.forceNullMove(nextContext.forward)
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
        println("http://localhost:8080/angular-seed-master/app/#/rendering?data=" + path)
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
        println("http://localhost:8080/angular-seed-master/app/#/rendering?data=" + path)
      }
    }
    redirect("/context")
  }

}