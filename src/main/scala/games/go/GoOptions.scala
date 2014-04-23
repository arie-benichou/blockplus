package games.go

import components.Positions._
import scala.collection.immutable.SortedSet

object GoOptions {

  private def opponent(character: Char) = {
    if (character == 'O') 'X' else if (character == 'X') 'O' else error("Unknown Character")
  }

  /**
   *  1) As long as there is at least one degree of freedom remaining once played
   *  2) Except if the opponent string will loose its last degree of freedom
   */
  def apply(character: Char, board: GoBoard): Set[Position] = {
    val space = board.cells.filter(_._2 == '.')
    val stringsForSpace = board.layer('.').strings // TODO parameterize
    val islands = stringsForSpace.filter(_.out.size < 1).map(_.in.iterator.next)
    val stringsForPlayer = board.layer(character).strings
    val suicides = stringsForPlayer.filter { string =>
      string.out.size == 1 &&
        islands.contains(string.out.iterator.next) &&
        stringsForPlayer.count(s => s.out.contains(string.out.iterator.next)) < 2
    }.map(_.out.iterator.next)
    val stringsForOpponent = board.layer(opponent(character)).strings
    val captures = stringsForOpponent.filter(_.out.size == 1).map(_.out.iterator.next)
    val effectiveIslands = islands.diff(captures).filterNot(p => stringsForPlayer.exists(_.out.contains(p)))

    SortedSet() ++ space -- effectiveIslands -- suicides ++ captures
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
      assert(options == Set(Position(4, 0), Position(4, 1), Position(4, 2)))
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
      assert(options == Set(Position(1, 0), Position(4, 2)))
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
      assert(options.isEmpty)
    }

    {
      val data = Array(
        ".O.O.",
        "OOXOO",
        ".OOOO",
        "OXO.O",
        "..OO."
      )
      println("=================================")
      val board = GoBoard(data)
      println(board)
      println("Options for 'X' :")
      val options = GoOptions('X', board)
      options.foreach(println)
      assert(options == Set(Position(4, 0), Position(4, 1)))
    }

    {
      val data = Array(
        "OOOOOO",
        "OOO.XO",
        "O.XXOO",
        "OOOOOO"
      )
      println("=================================")
      val board = GoBoard(data)
      println(board)
      println("Options for 'X' :")
      val options = GoOptions('X', board)
      options.foreach(println)
      assert(options == Set(Position(1, 3), Position(2, 1)))
    }

    //    {
    //      val data = Array(
    //        ".O.O.O.OO",
    //        "OOOOOOOOO",
    //        ".O.XOOXX.",
    //        "XO.O..OXO",
    //        ".OOOOOOOO",
    //        "XXXOOXXXO",
    //        ".XX.XXXXO",
    //        "XXXXXXXOO",
    //        ".XXXXXOO."
    //      )
    //      println("=================================")
    //      val board = GoBoard(data)
    //      println(board)
    //      println("Options for 'X' :")
    //      val options = GoOptions('X', board)
    //      options.foreach(println)
    //      //assert(options == Set(Position(1, 3), Position(2, 1)))
    //    }

  }

}