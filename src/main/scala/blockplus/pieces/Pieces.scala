package blockplus.pieces

import scala.collection.immutable.Map
import com.google.common.base.Stopwatch
import java.util.concurrent.TimeUnit

object Pieces {

  val data = Map(
    1 -> PieceGraph((0, 0)),
    2 -> PieceGraph((0, 0), (0, 1)),
    3 -> PieceGraph((0, 0), (0, 1), (0, 2))(0, 1),
    4 -> PieceGraph((0, 0), (0, 1), (1, 0)),
    5 -> PieceGraph((0, 0), (0, 1), (0, 2), (0, 3))(0, 1),
    6 -> PieceGraph((0, 0), (0, 1), (0, 2), (1, 0)),
    7 -> PieceGraph((0, 0), (0, 1), (0, 2), (1, 1))(0, 1),
    8 -> PieceGraph((0, 0), (0, 1), (1, 0), (1, 1)),
    9 -> PieceGraph((0, 0), (0, 1), (1, 1), (1, 2))(1, 1),
    10 -> PieceGraph((0, 0), (0, 1), (0, 2), (0, 3), (1, 0))(0, 1),
    11 -> PieceGraph((0, 0), (0, 1), (0, 2), (0, 3), (0, 4))(0, 2),
    12 -> PieceGraph((0, 0), (0, 1), (1, 1), (1, 2), (1, 3))(1, 1),
    13 -> PieceGraph((0, 0), (0, 1), (0, 2), (1, 1), (1, 2))(1, 1),
    14 -> PieceGraph((0, 0), (0, 1), (1, 1), (2, 0), (2, 1))(1, 1),
    15 -> PieceGraph((0, 0), (1, 0), (1, 1), (2, 0), (3, 0))(1, 0),
    16 -> PieceGraph((0, 0), (1, 0), (1, 1), (2, 0), (3, 0))(1, 0),
    17 -> PieceGraph((0, 0), (0, 1), (0, 2), (1, 0), (2, 0)),
    18 -> PieceGraph((0, 0), (0, 1), (1, 1), (1, 2), (2, 2))(1, 1),
    19 -> PieceGraph((0, 0), (1, 0), (1, 1), (1, 2), (2, 2))(1, 1),
    20 -> PieceGraph((0, 0), (1, 0), (1, 1), (1, 2), (2, 1))(1, 1),
    21 -> PieceGraph((0, 1), (1, 0), (1, 1), (1, 2), (2, 1))(1, 1)
  ).withDefault(Int => PieceGraph())

  def main(args: Array[String]) {

    /*
    val piece = Pieces.data(6)
    val pieceTemplates = PieceTemplates(piece)
    println(pieceTemplates)
    pieceTemplates.instances.foreach(println)
    println()
    val p1 = PieceInstance((2, 8), (2, 9), (2, 10), (3, 8))
    println(p1)
    val p2 = pieceTemplates.instances.toList.head
    println(p2)
    println(p1 == p2)
    */

    val stopwatch = new Stopwatch().start()
    for (n <- (0 to 21)) {
      for (x <- (0 until 20)) {
        for (y <- (0 until 20)) {
          PieceTemplates(Pieces.data(n)).on((x, y))
          //PieceTemplates(Pieces.data(n)).on((x, y)).instances
        }
      }
      //Thread.sleep(500)
    }
    //Thread.sleep(2000)
    println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS))
    println(PieceTemplates._instances)
    println(PieceInstance._instances)
    /**
     *
     *  full set && lazy values not computed yet (8 400 instances of PieceTemplates)
     *   Space: ~ 40 Mo
     *   Time : ~ 0.75 s
     *
     *  full set && lazy values computed (67 200  instances of PieceInstance)
     *   Space: ~ 270 Mo
     *   Time : ~ 2 s
     */
  }

}