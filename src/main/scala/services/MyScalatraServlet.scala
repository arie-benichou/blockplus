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

}

class MyScalatraServlet extends MyScalatraWebAppStack with JacksonJsonSupport with JValueResult {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  var context = Game.context

  get("/context") {
    //val context = Main.run(this.context, Main.nullRenderer)
    Map(
      "color" -> context.id.toString,
      "is-over" -> context.isTerminal.toString,
      "path" -> MyScalatraServlet.pathToString(context.path).split(','),
      "last-move" -> MyScalatraServlet.pathToString(context.path.take(1))
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