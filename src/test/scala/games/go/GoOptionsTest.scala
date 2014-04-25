package games.go

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import components.Positions.Position

@RunWith(classOf[JUnitRunner])
class GoOptionsTest extends FunSuite {

  test("options - case 1") {
    val data = Array(
      "OOX",
      ".OX",
      "OOX",
      "XXX",
      "..."
    )
    val board = GoBoard(data)
    val options = GoOptions('O', board)
    assert(options == Set(Position(4, 0), Position(4, 1), Position(4, 2)))
  }

  test("options - case 2") {
    val data = Array(
      "XXO",
      ".XO",
      "XXO",
      "OOO",
      "XX."
    )
    val board = GoBoard(data)
    val options = GoOptions('O', board)
    assert(options == Set(Position(1, 0), Position(4, 2)))
  }

  test("options - case 3") {
    val data = Array(
      "XX",
      ".X",
      "XX",
      ".X",
      "XX"
    )
    val board = GoBoard(data)
    val options = GoOptions('O', board)
    assert(options == Set(GoGame.NullOption))
  }

  test("options - case 4") {
    val data = Array(
      ".O.O.",
      "OOXOO",
      ".OOOO",
      "OXO.O",
      "..OO."
    )
    val board = GoBoard(data)
    val options = GoOptions('X', board)
    assert(options == Set(Position(4, 0), Position(4, 1)))
  }

  test("options - case 5") {
    val data = Array(
      "OOOOOO",
      "OOO.XO",
      "O.XXOO",
      "OOOOOO"
    )
    val board = GoBoard(data)
    val options = GoOptions('X', board)
    assert(options == Set(Position(1, 3), Position(2, 1)))
  }

  test("options - case 6") {
    val data = Array(
      "OOOOOO",
      "OOO.XO",
      "O.XXOO",
      "OOOOOO"
    )
    val board = GoBoard(data)
    val options = GoOptions('X', board)
    assert(options == Set(Position(1, 3), Position(2, 1)))
  }

  test("options - case 7") {
    val data = Array(
      "O.OOOO",
      "OOOX.O",
      "OOOOXO"
    )
    val board = GoBoard(data)
    val options = GoOptions('X', board)
    assert(options == Set(GoGame.NullOption))
  }

}