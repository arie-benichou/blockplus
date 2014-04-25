package games.go

import components.Positions.Position

object GoLands {

  // TODO inject
  private def opponent(character: Char) = if (character == 'O') 'X' else 'O'

  def apply(character: Char, board: GoBoard): Set[Position] = {
    val space = board.cells.filterDefaults()
    val stringsForSpace = board.layer('.').strings // TODO parameterize
    val islands = stringsForSpace.filter(_.out.isEmpty).flatMap(_.in)
    val stringsForPlayer = board.layer(character).strings
    val stringsForOpponent = board.layer(opponent(character)).strings
    val capturables = stringsForPlayer.filter(_.out.size == 1).flatMap(_.out)
    val effectiveIslands = islands.diff(capturables).filter { p =>
      stringsForPlayer.exists(_.out.contains(p)) && !stringsForOpponent.exists(_.out.contains(p))
    }
    effectiveIslands
  }

}