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
    SortedSet() ++ space
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

      val board = GoBoard(data)
      println(board)

      println("Strings for 'O'\n")
      val strings = board.layer('O').strings
      strings.foreach { string =>
        println(string)
        if (string.out.size == 1) {
          println("suicide: ")
          println(string.out.iterator.next + "\n")
        }
      }

      println("Options for 'O'\n")
      val options = GoOptions('O', board)
      options.foreach(println)
    }

    /*
    {
      val data = Array(
        "XXO",
        ".XO",
        "XXO",
        "OOO",
        "..."
      )

      val board = GoBoard(data)
      println(board)

      println("Strings for 'O'\n")
      val strings = board.layer('O').strings
      strings.foreach(println)

      println("Options for 'O'\n")
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

      val board = GoBoard(data)
      println(board)

      println("Strings for 'O'\n")
      val strings = board.layer('O').strings
      strings.foreach(println)

      println("Options for 'O'\n")
      val options = GoOptions('O', board)
      options.foreach(println)
    }
    */

  }

}