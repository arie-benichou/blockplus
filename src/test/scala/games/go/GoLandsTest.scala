package games.go

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import components.Positions.Position

@RunWith(classOf[JUnitRunner])
class GoLandsTest extends FunSuite {

  test("lands - case 1") {
    val data = Array(
      "OO.OO",
      ".OOO.",
      "OO.OO",
      "O.O.O",
      "OO.OO"
    )

    val board = GoBoard(data)
    val lands = GoLands('O', board)
    assert(lands == Set(
      Position(0, 2),
      Position(1, 0),
      Position(1, 4),
      Position(2, 2),
      Position(3, 1),
      Position(3, 3),
      Position(4, 2)
    ))
  }

  test("lands - case 2") {
    val data = Array(
      ".OOX.",
      "OOOX.",
      "XXX.."
    )
    val board = GoBoard(data)
    val lands = GoLands('O', board)
    assert(lands.isEmpty)
  }

}