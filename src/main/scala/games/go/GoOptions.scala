package games.go

import components.Positions._
import scala.collection.immutable.SortedSet

object GoOptions {

  /**
   *  1) As long as there is at least one degree of freedom remaining once played
   *  2) Except if the opponent string will loose its last degree of freedom
   */
  def apply(character: Char, board: GoBoard): Set[Position] = {
    val space = board.cells.filter(_._2 == '.')
    val stringsForSpace = board.layer('.').strings
    val islands = stringsForSpace.filter(_.out.size < 1).map(_.in.iterator.next)
    val stringsForO = board.layer('O').strings
    val suicides = stringsForO.filter(_.out.size == 1).map(_.out.iterator.next)
    val stringsForX = board.layer('X').strings
    val captures = stringsForX.filter(_.out.size == 1).map(_.out.iterator.next)
    SortedSet() ++ space -- islands -- suicides ++ captures
  }

  // TODO extract tests
  def main(args: Array[String]) {

    // TODO extract method
    {
      val data = Array(
        "OOX",
        ".OX",
        "OOX",
        "XXX",
        "..."
      )
      println("=================================")
      val board = GoBoard(data)
      println(board)
      println("Options for 'O' :")
      val options = GoOptions('O', board)
      options.foreach(println)
    }

    {
      val data = Array(
        "XXO",
        ".XO",
        "XXO",
        "OOO",
        "XX."
      )
      println("=================================")
      val board = GoBoard(data)
      println(board)
      println("Options for 'O' :")
      val options = GoOptions('O', board)
      options.foreach(println)
    }

    {
      val data = Array(
        "XX",
        ".X",
        "XX",
        ".X",
        "XX"
      )
      println("=================================")
      val board = GoBoard(data)
      println(board)
      println("Options for 'O' :")
      val options = GoOptions('O', board)
      options.foreach(println)
    }

  }

}