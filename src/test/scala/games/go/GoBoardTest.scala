package games.go

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import components.Positions.Position

@RunWith(classOf[JUnitRunner])
class GoBoardTest extends FunSpec {

  describe("[GoBoard]") {

    it("should parse - case 1") {
      val data = Array(
        "...",
        "...",
        "..."
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings.isEmpty)
      assert(board.layer('X').strings.isEmpty)
      assert(board.layer('.').strings ==
        Set(GoString(
          Set(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 1), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          ),
          Set(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 1), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          )
        ))
      )
    }

    it("should parse - case 2") {
      val data = Array(
        "...",
        ".O.",
        "..."
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings ==
        Set(GoString(
          Set(
            Position(1, 1)
          ),
          Set(
            Position(0, 1),
            Position(1, 0),
            Position(1, 2),
            Position(2, 1)
          )
        ))
      )
      assert(board.layer('X').strings.isEmpty)
      assert(board.layer('.').strings ==
        Set(GoString(
          Set(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          ),
          Set(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          )
        ))
      )
    }

    it("should parse - case 3") {
      val data = Array(
        "...",
        ".X.",
        "..."
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings.isEmpty)
      assert(board.layer('X').strings ==
        Set(GoString(
          Set(
            Position(1, 1)
          ),
          Set(
            Position(0, 1),
            Position(1, 0),
            Position(1, 2),
            Position(2, 1)
          )
        ))
      )
      assert(board.layer('.').strings ==
        Set(GoString(
          Set(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          ),
          Set(
            Position(0, 0), Position(0, 1), Position(0, 2),
            Position(1, 0), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          )
        ))
      )
    }

    it("should parse - case 4") {
      val data = Array(
        ".X.",
        ".X.",
        "..."
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings.isEmpty)
      assert(board.layer('X').strings ==
        Set(GoString(
          Set(
            Position(0, 1),
            Position(1, 1)
          ),
          Set(
            Position(0, 0),
            Position(0, 2),
            Position(1, 0),
            Position(1, 2),
            Position(2, 1)
          )
        ))
      )
      assert(board.layer('.').strings ==
        Set(GoString(
          Set(
            Position(0, 0), Position(0, 2),
            Position(1, 0), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          ),
          Set(
            Position(0, 0), Position(0, 2),
            Position(1, 0), Position(1, 2),
            Position(2, 0), Position(2, 1), Position(2, 2)
          )
        ))
      )
    }

    it("should parse - case 5") {
      val data = Array(
        ".XO",
        ".X.",
        "XOO"
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings ==
        Set(
          GoString(Set(Position(2, 1), Position(2, 2)), Set(Position(1, 2))),
          GoString(Set(Position(0, 2)), Set(Position(1, 2))
          )
        )
      )
      assert(board.layer('X').strings ==
        Set(
          GoString(Set(Position(0, 1), Position(1, 1)), Set(Position(0, 0), Position(1, 0), Position(1, 2))),
          GoString(Set(Position(2, 0)), Set(Position(1, 0)))
        )
      )
      assert(board.layer('.').strings ==
        Set(
          GoString(Set(Position(0, 0), Position(1, 0)), Set(Position(0, 0), Position(1, 0))),
          GoString(Set(Position(1, 2)), Set())
        )
      )
    }

    it("should parse - case 6") {
      val data = Array(
        ".XO",
        "XXO",
        "XOO"
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings ==
        Set(
          GoString(
            Set(Position(0, 2), Position(1, 2), Position(2, 1), Position(2, 2)),
            Set()
          )
        )
      )
      assert(board.layer('X').strings ==
        Set(
          GoString(
            Set(Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 0)),
            Set(Position(0, 0))
          )
        )
      )
      assert(board.layer('.').strings ==
        Set(
          GoString(Set(Position(0, 0)), Set())
        )
      )
    }

    it("should parse - case 7") {
      val data = Array(
        "XXO",
        "XXO",
        "XOO"
      )
      val board = GoBoard(data)
      assert(board.layer('O').strings ==
        Set(
          GoString(
            Set(Position(0, 2), Position(1, 2), Position(2, 1), Position(2, 2)),
            Set()
          )
        )
      )
      assert(board.layer('X').strings ==
        Set(
          GoString(
            Set(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1), Position(2, 0)),
            Set()
          )
        )
      )
      assert(board.layer('.').strings.isEmpty)
    }

  }

  it("should update - case 1") {
    val data = Array(
      "...",
      "...",
      "..."
    )
    val actualBoard = GoBoard(data).play(Position(1, 1), 'O')
    val expectedBoard = GoBoard(Array(
      "...",
      ".O.",
      "..."
    ))

    assert(actualBoard == expectedBoard)
    assert(actualBoard == expectedBoard)
  }

  it("should update - case 2") {
    val data = Array(
      "...",
      "...",
      "..."
    )
    val actualBoard = GoBoard(data).play(Position(1, 1), 'X')
    val expectedBoard = GoBoard(Array(
      "...",
      ".X.",
      "..."
    ))

    assert(actualBoard == expectedBoard)
    assert(actualBoard == expectedBoard)
  }

  it("should update - case 3") {
    val data = Array(
      ".X.",
      "...",
      "..."
    )
    val actualBoard = GoBoard(data).play(Position(1, 1), 'X')
    val expectedBoard = GoBoard(Array(
      ".X.",
      ".X.",
      "..."
    ))

    assert(actualBoard == expectedBoard)
    assert(actualBoard == expectedBoard)
  }

  it("should update - case 4") {
    val data = Array(
      ".X.",
      ".X.",
      "..O"
    )
    val actualBoard = GoBoard(data).play(Position(2, 1), 'O')
    val expectedBoard = GoBoard(Array(
      ".X.",
      ".X.",
      ".OO"
    ))

    assert(actualBoard == expectedBoard)
    assert(actualBoard == expectedBoard)
  }

  it("should update - case 5") {
    val data = Array(
      ".X.",
      ".X.",
      "XOO"
    )
    val actualBoard = GoBoard(data).play(Position(0, 2), 'O')
    val expectedBoard = GoBoard(Array(
      ".XO",
      ".X.",
      "XOO"
    ))

    assert(actualBoard == expectedBoard)
    assert(actualBoard == expectedBoard)
  }

  it("should update - case 6") {
    val data = Array(
      ".XO",
      "XXO",
      "XOO"
    )
    val actualBoard = GoBoard(data).play(Position(0, 0), 'X')
    val expectedBoard = GoBoard(Array(
      "XXO",
      "XXO",
      "XOO"
    ))

    assert(actualBoard == expectedBoard)
  }

  it("should update - case 7") {
    val data = Array(
      ".X.",
      "XOX",
      "..."
    )
    val actualBoard = GoBoard(data).play(Position(2, 1), 'X')
    val expectedBoard = GoBoard(Array(
      ".X.",
      "X.X",
      ".X."
    ))

    assert(actualBoard == expectedBoard)
  }

  it("should update - case 8") {
    val data = Array(
      "OOOO",
      "OXXO",
      "OO.O"
    )
    val actualBoard = GoBoard(data).play(Position(2, 2), 'O')
    val expectedBoard = GoBoard(Array(
      "OOOO",
      "O..O",
      "OOOO"
    ))

    assert(actualBoard == expectedBoard)
  }

  it("should update - case 9") {
    val data = Array(
      "OX",
      "O.",
      ".O"
    )
    val actualBoard = GoBoard(data).play(Position(1, 1), 'O')
    val expectedBoard = GoBoard(Array(
      "O.",
      "OO",
      ".O"
    ))

    assert(actualBoard == expectedBoard)
  }

  it("should update - case 10") {
    val data = Array(
      ".....",
      "..X..",
      ".XOX.",
      ".....",
      "....."
    )
    val actualBoard = GoBoard(data).play(Position(3, 2), 'X')
    println(actualBoard)
    val expectedBoard = GoBoard(Array(
      ".....",
      "..X..",
      ".X.X.",
      "..X..",
      "....."
    ))

    assert(actualBoard == expectedBoard)
  }

}