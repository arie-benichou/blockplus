package services

import org.json4s.DefaultFormats
import org.json4s.Formats
import org.scalatra.json.JValueResult
import org.scalatra.json.JacksonJsonSupport

import components.Positions.Position
import games.blokus.Game
import games.blokus.Game.BlokusContext
import games.blokus.Game.BlokusMove
import games.blokus.Game.Color
import games.blokus.Game.Move
import games.blokus.Options

object BlokusContextContainer {

  private val colorByChar = Map(
    'B' -> Color.Blue,
    'Y' -> Color.Yellow,
    'R' -> Color.Red,
    'G' -> Color.Green)

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

  private def moveToString(move: BlokusMove) =
    if (move == null) "" else move.side.toString().charAt(0) + (move.data.positions.map(p => p.row + ":" + p.column)).mkString("-")

  private def pathToString(ctx: BlokusContext) = {
    val stack = ctx.history.map { ctx => moveToString(ctx.lastMove) }
    (if (ctx.lastMove != null) stack.push(moveToString(ctx.lastMove)) else stack).mkString(",")
  }

}

class BlokusContextContainer extends GameContextContainer with JacksonJsonSupport with JValueResult {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  var previousContext: BlokusContext = null; // TODO NullContext
  var currentContext = Game.context

  get("/context") {
    val ctx = currentContext
    Map(
      "color" -> ctx.id.toString,
      "is-over" -> ctx.isTerminal.toString,
      "path" -> BlokusContextContainer.pathToString(ctx).split(','),
      "last-move" -> BlokusContextContainer.moveToString(ctx.lastMove),
      "options" -> Options.get(ctx.id, ctx.space, ctx.side(ctx.id).values).map(_._3.map(p => p.row + ":" + p.column).mkString("-")),
      "scores" -> ctx.sides.map(e => (e._1.toString, Game.score(ctx, e._1))).toMap)
  }

  post("/play/:move") {
    val ctx = currentContext
    val (color, positions) = BlokusContextContainer.parseIncomingMoveData(params("move"))
    try {
      val instance = Game.positionsToPolyomino(positions)
      val move = Move(color, instance)
      val nextContext = ctx.apply(move)
      this.synchronized {
        previousContext = currentContext;
        // TODO memoize context options
        val options = Options.get(previousContext.id, previousContext.space, previousContext.side(previousContext.id).values)
        currentContext = Game.forceNullMove(nextContext)
      }
    }
    catch {
      case e: Exception => {
        val path = BlokusContextContainer.pathToString(ctx)
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