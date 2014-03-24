package games.go

import components.Positions._
import scala.collection.immutable.SortedSet

object GoLands {

  private def opponent(character: Char) = {
    if (character == 'O') 'X' else if (character == 'X') 'O' else error("Unknown Character")
  }

  def apply(character: Char, board: GoBoard): Set[Position] = {
    val space = board.cells.filter(_._2 == '.')
    val stringsForSpace = board.layer('.').strings // TODO parameterize
    val islands = stringsForSpace.filter(_.out.size < 1).map(_.in.iterator.next)
    val stringsForPlayer = board.layer(character).strings
    val stringsForOpponent = board.layer(opponent(character)).strings
    val capturables = stringsForPlayer.filter(_.out.size == 1).map(_.out.iterator.next)
    val effectiveIslands = islands.diff(capturables).filter { p =>
      stringsForPlayer.exists(_.out.contains(p)) &&
        !stringsForOpponent.exists(_.out.contains(p))
    }
    SortedSet() ++ effectiveIslands
  }

  // TODO extract tests
  def main(args: Array[String]) {

    // TODO extract method
    {
      val data = Array(
        "OO.OO",
        ".OOO.",
        "OO.OO",
        "O.O.O",
        "OO.OO"
      )

      val board = GoBoard(data)
      val lands = GoLands('O', board)

      println("=================================")
      println
      println(board)
      println("Lands for 'O' :\n")
      lands.foreach { land =>
        println(" " + land)
      }
      println
    }

    {
      val data = Array(
        ".OOX.",
        "OOOX.",
        "XXX.."
      )

      val board = GoBoard(data)
      val lands = GoLands('O', board)

      println("=================================")
      println
      println(board)
      println("Lands for 'O' :\n")
      lands.foreach { land =>
        println(" " + land)
      }
      println
    }

    println("=================================")

  }

}