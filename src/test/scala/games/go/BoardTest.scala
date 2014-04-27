package games.go

import org.junit.runner.RunWith

import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

import components.Positions._

import games.go.Board.Strings._

@RunWith(classOf[JUnitRunner])
class BoardTest extends FunSpec {

  describe("[Board Parsing]") {

    it("should parse - case 1") {
      val data = Array(
        "...",
        "...",
        "..."
      )
      val board = Board(data)
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
      val board = Board(data)
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
      val board = Board(data)
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
      val board = Board(data)
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
      val board = Board(data)
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
      val board = Board(data)
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
      val board = Board(data)
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

  describe("[Board Update]") {

    it("should update - case 1") {
      val data = Array(
        "...",
        "...",
        "..."
      )
      val actualBoard = Board(data).play(Position(1, 1), 'O')
      val expectedBoard = Board(Array(
        "...",
        ".O.",
        "..."
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 2") {
      val data = Array(
        "...",
        "...",
        "..."
      )
      val actualBoard = Board(data).play(Position(1, 1), 'X')
      val expectedBoard = Board(Array(
        "...",
        ".X.",
        "..."
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 3") {
      val data = Array(
        ".X.",
        "...",
        "..."
      )
      val actualBoard = Board(data).play(Position(1, 1), 'X')
      val expectedBoard = Board(Array(
        ".X.",
        ".X.",
        "..."
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 4") {
      val data = Array(
        ".X.",
        ".X.",
        "..O"
      )
      val actualBoard = Board(data).play(Position(2, 1), 'O')
      val expectedBoard = Board(Array(
        ".X.",
        ".X.",
        ".OO"
      ))
      assert(actualBoard.layer('.').strings == expectedBoard.layer('.').strings)
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 5") {
      val data = Array(
        ".X.",
        ".X.",
        "XOO"
      )
      val actualBoard = Board(data).play(Position(0, 2), 'O')
      val expectedBoard = Board(Array(
        ".XO",
        ".X.",
        "XOO"
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 6") {
      val data = Array(
        "X.O",
        "XXO",
        "XOO"
      )
      val actualBoard = Board(data).play(Position(0, 1), 'X')
      val expectedBoard = Board(Array(
        "XX.",
        "XX.",
        "X.."
      ))
      assert(actualBoard.layer('.').strings == expectedBoard.layer('.').strings)
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 7") {
      val data = Array(
        ".X.",
        "XOX",
        "..."
      )
      val actualBoard = Board(data).play(Position(2, 1), 'X')
      val expectedBoard = Board(Array(
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
      val actualBoard = Board(data).play(Position(2, 2), 'O')
      val expectedBoard = Board(Array(
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
      val actualBoard = Board(data).play(Position(1, 1), 'O')
      val expectedBoard = Board(Array(
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
      val actualBoard = Board(data).play(Position(3, 2), 'X')
      val expectedBoard = Board(Array(
        ".....",
        "..X..",
        ".X.X.",
        "..X..",
        "....."
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 11") {
      val data = Array(
        "......",
        "..OO..",
        ".X..X.",
        ".X....",
        "......"
      )
      val actualBoard = Board(data).play(Position(4, 2), 'X')
      val expectedBoard = Board(Array(
        "......",
        "..OO..",
        ".X..X.",
        ".X....",
        "..X..."
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 12") {
      val data = Array(
        "XXX...XXX",
        "X.XX.XX.X",
        "X.......X",
        "X.XX.XX.X",
        "XXX...XXX"
      )
      val actualBoard = Board(data).play(Position(2, 4), 'O')
      val expectedBoard = Board(Array(
        "XXX...XXX",
        "X.XX.XX.X",
        "X...O...X",
        "X.XX.XX.X",
        "XXX...XXX"
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 13") {
      val data = Array(
        ".........",
        "....O....",
        "...XO....",
        "....OX...",
        "....O....",
        "...OO....",
        "..X..X...",
        "..X......",
        "........."
      )
      val actualBoard = Board(data).play(Position(8, 3), 'X')
      val expectedBoard = Board(Array(
        ".........",
        "....O....",
        "...XO....",
        "....OX...",
        "....O....",
        "...OO....",
        "..X..X...",
        "..X......",
        "...X....."
      ))
      assert(actualBoard == expectedBoard)
    }

    it("should update - case 14") {
      val data = Array(
        ".........",
        ".X.......",
        "..X......",
        "...X.....",
        "....OOO..",
        ".........",
        ".........",
        ".........",
        "........."
      )
      val actualBoard = Board(data).play(Position(4, 7), 'O')
      val expectedBoard = Board(Array(
        ".........",
        ".X.......",
        "..X......",
        "...X.....",
        "....OOOO.",
        ".........",
        ".........",
        ".........",
        "........."
      ))
      assert(actualBoard == expectedBoard)

    }
  }

  describe("[Board Lands]") {

    it("lands - case 1") {
      val data = Array(
        "OO.OO",
        ".OOO.",
        "OO.OO",
        "O.O.O",
        "OO.OO"
      )

      val board = Board(data)
      val lands = board.layer('O').lands
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

    it("lands - case 2") {
      val data = Array(
        ".OOX.",
        "OOOX.",
        "XXX.."
      )
      val board = Board(data)
      val lands = board.layer('O').lands
      assert(lands.isEmpty)
    }
  }

  describe("[Board Options]") {

    it("options - case 1") {
      val data = Array(
        "OOX",
        ".OX",
        "OOX",
        "XXX",
        "..."
      )
      val board = Board(data)
      val options = board.layer('O').options
      assert(options == Set(Position(4, 0), Position(4, 1), Position(4, 2)))
    }

    it("options - case 2") {
      val data = Array(
        "XXO",
        ".XO",
        "XXO",
        "OOO",
        "XX."
      )
      val board = Board(data)
      val options = board.layer('O').options
      assert(options == Set(Position(1, 0), Position(4, 2)))
    }

    it("options - case 3") {
      val data = Array(
        "XX",
        ".X",
        "XX",
        ".X",
        "XX"
      )
      val board = Board(data)
      val options = board.layer('O').options
      assert(options == Set())
    }

    it("options - case 4") {
      val data = Array(
        ".O.O.",
        "OOXOO",
        ".OOOO",
        "OXO.O",
        "..OO."
      )
      val board = Board(data)
      val options = board.layer('X').options
      assert(options == Set(Position(4, 0), Position(4, 1)))
    }

    it("options - case 5") {
      val data = Array(
        "OOOOOO",
        "OOO.XO",
        "O.XXOO",
        "OOOOOO"
      )
      val board = Board(data)
      val options = board.layer('X').options
      assert(options == Set(Position(1, 3), Position(2, 1)))
    }

    it("options - case 6") {
      val data = Array(
        "OOOOOO",
        "OOO.XO",
        "O.XXOO",
        "OOOOOO"
      )
      val board = Board(data)
      val options = board.layer('X').options
      assert(options == Set(Position(1, 3), Position(2, 1)))
    }

    it("options - case 7") {
      val data = Array(
        "O.OOOO",
        "OOOX.O",
        "OOOOXO"
      )
      val board = Board(data)
      val options = board.layer('X').options
      assert(options == Set())
    }
  }

}