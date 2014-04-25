package games.go

import scala.collection.immutable.SortedSet

import components.Positions.Ordering
import components.Positions.Position

object GoOptions {

  // TODO use adversity abstraction
  private def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  /**
   *  1) As long as there is at least one degree of freedom remaining once played
   *  2) Except if the opponent string will loose its last degree of freedom
   */
  def apply(character: Char, board: GoBoard): Set[Position] = {
    val space = board.cells.filterDefaults()
    val stringsForSpace = board.layer('.').strings // TODO parameterize
    val islands = stringsForSpace.filter(_.out.size < 1).flatMap(_.in)
    val stringsForPlayer = board.layer(character).strings.map(_.out)
    val tmp1 = stringsForPlayer.filter(_.size < 2).flatten
    val tmp2 = tmp1.filter(e => islands.contains(e))
    val suicides = tmp2.filter(e => !stringsForPlayer.exists(s => s.contains(e) && s.size > 1))
    val stringsForOpponent = board.layer(opponent(character)).strings
    val captures = stringsForOpponent.filter(_.out.size == 1).flatMap(_.out)
    val effectiveIslands = islands.diff(captures).filterNot(p => stringsForPlayer.exists(_.contains(p)))
    val options = SortedSet() ++ space -- effectiveIslands -- suicides ++ captures
    if (options.isEmpty) Set(Position(Integer.MIN_VALUE, Integer.MIN_VALUE)) else options
  }

}