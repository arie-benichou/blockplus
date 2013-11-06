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
import games.blokus.Options
import games.blokus.Polyominos
import games.blokus.Main

object MyScalatraServlet {

  private val colorByChar = Map(
    'B' -> Color.Blue,
    'Y' -> Color.Yellow,
    'R' -> Color.Red,
    'G' -> Color.Green
  )

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

  private def lastMove(context: BlokusContext, side: Color): BlokusMove = {
    context.path.dropWhile(_.side != side).tail.dropWhile(_.side != side).head
  }

  private def score(context: BlokusContext, id: Color) = {
    val side = context.side(id)
    val pieces = side.values;
    val weight = -pieces.weight
    if (weight != 0) weight
    else if (lastMove(context, id).data.selfType != Polyominos._1) 15
    else 20
  }

  def main(args: Array[String]) {
    val ctx = Main.run(Game.context, Main.nullRenderer)
    //val ctx = Game.context
    println(score(ctx, Color.Blue))
    println(score(ctx, Color.Yellow))
    println(score(ctx, Color.Red))
    println(score(ctx, Color.Green))
    val scores = MyScalatraServlet.colorByChar.values.map(c => (c, MyScalatraServlet.score(ctx, c))).toMap
    println(scores)
  }

}

class MyScalatraServlet extends MyScalatraWebAppStack with JacksonJsonSupport with JValueResult {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  var context = Game.context

  get("/context") {
    //val ctx = Main.run(context, Main.nullRenderer)
    val ctx = context
    Map(
      "color" -> ctx.id.toString,
      "is-over" -> ctx.isTerminal.toString,
      "path" -> MyScalatraServlet.pathToString(ctx.path).split(','),
      "last-move" -> MyScalatraServlet.pathToString(ctx.path.take(1)),
      //"lights" -> ctx.space.lights(ctx.id).map(p => p.row + ":" + p.column).mkString("-"),
      "options" -> Options.get(ctx.id, ctx.space, ctx.side(ctx.id).values).map(_._3.map(p => p.row + ":" + p.column).mkString("-")),
      "scores" -> MyScalatraServlet.colorByChar.values.map(c => (c.toString, MyScalatraServlet.score(ctx, c))).toMap
    )
  }

  post("/play/:move") {
    val query = params("move")
    val head = query.head
    val tail = query.tail
    val color = MyScalatraServlet.colorByChar.get(head).get
    val positions = if (tail.isEmpty) Set.empty[Position] else tail.split('-').map(x => {
      val rowAndColumn = x.split(':').map(y => Integer.parseInt(y))
      Position(rowAndColumn(0), rowAndColumn(1))
    }).toSet
    val initialContext = context
    val path = MyScalatraServlet.pathToString(context.path)

    try {

      val instance = MyScalatraServlet.positionsToPolyomino(positions)
      val move = Move(color, instance)
      val nextContext = context.apply(move)

      if (!context.eq(nextContext)) {
        this.synchronized {
          context = MyScalatraServlet.forceNullMove(nextContext.forward)
        }
      }
      else {
        println("################################Illegal Instruction################################")
        println("path               : " + path)
        println("query              : " + query)
        println("current side       : " + initialContext.id)
        println("incoming side      : " + move.side)
        println("incoming positions : " + positions)
        println(move.data)
        println("http://localhost:8080/static/rendering/?" + path)
      }

    }

    catch {
      case e: Exception => {
        println("################################Illegal Instruction################################")
        println("path               : " + path)
        println("query              : " + query)
        println("current side       : " + initialContext.id)
        println("incoming positions : " + positions)
        println(e)
        println("http://localhost:8080/static/rendering/?" + path)
      }
    }

    redirect("/context")

  }

}