package games.blokus

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

import components.Cells
import components.Positions.Position

import games.blokus.Board.Layer
import games.blokus.Board.Layer.State
import games.blokus.Game.Color

@RunWith(classOf[JUnitRunner])
class BoardTest extends FunSpec {

  describe("[Layer]") {

    it("should handle cells") {
      val layer = Layer(1, 2)
      assert(layer.cells === Cells(1, 2, State.Nirvana, State.Mudita))
      assert(layer.lights === Set.empty)
      assert(layer.selves === Set.empty)
    }

    it("should handle mutability for cells") {
      val layer = Layer(1, 2)
      assert(!layer.isMutable(Position(-1, 0)))
      assert(layer.isMutable(Position(0, 0)))
      assert(layer.isMutable(Position(0, 1)))
      assert(!layer.isMutable(Position(1, 0)))
      assert(!layer.isMutable(Set(Position(-1, 0), Position(0, 0))))
      assert(layer.isMutable(Set(Position(0, 0), Position(0, 1))))
      assert(!layer.isMutable(Set(Position(0, 1), Position(1, 0))))
    }

    it("should return a new layer on update") {
      val layer = Layer(1, 2)
      assert(layer.lights.isEmpty)
      assert(layer.selves.isEmpty)
      val newLayer = layer(Map(
        Position(0, 0) -> State.Metta,
        Position(0, 1) -> State.Upekkha
      ))
      assert(newLayer != layer)
      assert(newLayer.lights == Set(Position(0, 0)))
      assert(newLayer.selves == Set(Position(0, 1)))
    }

    it("should not allow update on an immutable location") {
      val layer = Layer(1, 3)
      assert(layer.isMutable(Position(0, 0)))
      assert(layer.isMutable(Position(0, 1)))
      assert(layer.isMutable(Position(0, 2)));
      {
        val newLayer = layer(Map(
          Position(0, 0) -> State.Metta,
          Position(0, 1) -> State.Nirvana
        ))
        assert(newLayer.isMutable(Position(0, 0)))
        assert(newLayer.isMutable(Position(0, 1)))
        assert(newLayer.isMutable(Position(0, 2)))
      }
      {
        val newLayer = layer(Map(
          Position(0, 0) -> State.Karuna,
          Position(0, 1) -> State.Mudita,
          Position(0, 2) -> State.Upekkha
        ))
        assert(!newLayer.isMutable(Position(0, 0)))
        assert(!newLayer.isMutable(Position(0, 1)))
        assert(!newLayer.isMutable(Position(0, 2)))
      }
    }

  }

  describe("[Board]") {

    it("should handle layers") {
      val rows = 2
      val columns = 3
      val board = Board(rows, columns)
      assert(board.rows === rows)
      assert(board.columns === columns)
      assert(board.selves(Color.Blue) === Set())
      assert(board.selves(Color.Yellow) === Set())
      assert(board.selves(Color.Red) === Set())
      assert(board.selves(Color.Green) === Set())
      assert(board.lights(Color.Blue) === Set(Position(0, 0)))
      assert(board.lights(Color.Yellow) === Set(Position(0, columns - 1)))
      assert(board.lights(Color.Red) === Set(Position(rows - 1, columns - 1)))
      assert(board.lights(Color.Green) === Set(Position(rows - 1, 0)))
    }

    it("should determine if a position, or a set of positions, reference only mutable location(s)") {
      val board = Board(2, 2)
      assert(!board.isMutable(Color.Blue, Position(-1, 0)))
      assert(board.isMutable(Color.Blue, Position(0, 0)))
      assert(board.isMutable(Color.Blue, Position(0, 1)))
      assert(board.isMutable(Color.Blue, Position(1, 0)))
      assert(board.isMutable(Color.Blue, Position(1, 1)))
      assert(!board.isMutable(Color.Blue, Position(2, 0)))
      assert(board.isMutable(Color.Blue, Set(Position(0, 0), Position(0, 1))))
      assert(!board.isMutable(Color.Blue, Set(Position(0, 0), Position(2, 0))))
    }

    it("should return a new board on update") {

      val board = Board(2, 2)

      assert(board.layers(Color.Blue).cells.get(Position(0, 0)) === State.Metta)
      assert(board.layers(Color.Blue).cells.get(Position(0, 1)) === State.Nirvana)
      assert(board.layers(Color.Blue).cells.get(Position(1, 0)) === State.Nirvana)
      assert(board.layers(Color.Blue).cells.get(Position(1, 1)) === State.Nirvana)
      assert(board.isMutable(Color.Blue, Position(0, 0)))
      assert(board.isMutable(Color.Blue, Position(0, 1)))

      assert(board.layers(Color.Yellow).cells.get(Position(0, 0)) === State.Nirvana)
      assert(board.layers(Color.Yellow).cells.get(Position(0, 1)) === State.Metta)
      assert(board.layers(Color.Yellow).cells.get(Position(1, 0)) === State.Nirvana)
      assert(board.layers(Color.Yellow).cells.get(Position(1, 1)) === State.Nirvana)
      assert(board.isMutable(Color.Yellow, Position(0, 0)))

      assert(board.layers(Color.Red).cells.get(Position(0, 0)) === State.Nirvana)
      assert(board.layers(Color.Red).cells.get(Position(0, 1)) === State.Nirvana)
      assert(board.layers(Color.Red).cells.get(Position(1, 0)) === State.Nirvana)
      assert(board.layers(Color.Red).cells.get(Position(1, 1)) === State.Metta)
      assert(board.isMutable(Color.Red, Position(0, 0)))

      assert(board.layers(Color.Green).cells.get(Position(0, 0)) === State.Nirvana)
      assert(board.layers(Color.Green).cells.get(Position(0, 1)) === State.Nirvana)
      assert(board.layers(Color.Green).cells.get(Position(1, 0)) === State.Metta)
      assert(board.layers(Color.Green).cells.get(Position(1, 1)) === State.Nirvana)
      assert(board.isMutable(Color.Green, Position(0, 0)))

      val newBoard = board(Color.Blue, Set(Position(0, 0)), Set(Position(0, 1)), Set(Position(1, 1)))
      assert(board != newBoard)

      assert(newBoard.layers(Color.Blue).cells.get(Position(0, 0)) === State.Upekkha)
      assert(newBoard.layers(Color.Blue).cells.get(Position(0, 1)) === State.Karuna)
      assert(newBoard.layers(Color.Blue).cells.get(Position(1, 0)) === State.Nirvana)
      assert(newBoard.layers(Color.Blue).cells.get(Position(1, 1)) === State.Metta)
      assert(!newBoard.isMutable(Color.Blue, Position(0, 0)))
      assert(!newBoard.isMutable(Color.Blue, Position(0, 1)))
      assert(newBoard.isMutable(Color.Blue, Position(1, 0)))
      assert(newBoard.isMutable(Color.Yellow, Position(1, 1)))

      assert(newBoard.layers(Color.Yellow).cells.get(Position(0, 0)) === State.Mudita)
      assert(newBoard.layers(Color.Yellow).cells.get(Position(0, 1)) === State.Metta)
      assert(newBoard.layers(Color.Yellow).cells.get(Position(1, 0)) === State.Nirvana)
      assert(newBoard.layers(Color.Yellow).cells.get(Position(1, 1)) === State.Nirvana)
      assert(!newBoard.isMutable(Color.Yellow, Position(0, 0)))
      assert(newBoard.isMutable(Color.Yellow, Position(0, 1)))
      assert(newBoard.isMutable(Color.Yellow, Position(1, 0)))
      assert(newBoard.isMutable(Color.Yellow, Position(1, 1)))

      assert(newBoard.layers(Color.Red).cells.get(Position(0, 0)) === State.Mudita)
      assert(newBoard.layers(Color.Red).cells.get(Position(0, 1)) === State.Nirvana)
      assert(newBoard.layers(Color.Red).cells.get(Position(1, 0)) === State.Nirvana)
      assert(newBoard.layers(Color.Red).cells.get(Position(1, 1)) === State.Metta)
      assert(!newBoard.isMutable(Color.Red, Position(0, 0)))
      assert(newBoard.isMutable(Color.Red, Position(0, 1)))
      assert(newBoard.isMutable(Color.Red, Position(1, 0)))
      assert(newBoard.isMutable(Color.Red, Position(1, 1)))

      assert(newBoard.layers(Color.Green).cells.get(Position(0, 0)) === State.Mudita)
      assert(newBoard.layers(Color.Green).cells.get(Position(0, 1)) === State.Nirvana)
      assert(newBoard.layers(Color.Green).cells.get(Position(1, 0)) === State.Metta)
      assert(newBoard.layers(Color.Green).cells.get(Position(1, 1)) === State.Nirvana)
      assert(!newBoard.isMutable(Color.Green, Position(0, 0)))
      assert(newBoard.isMutable(Color.Green, Position(0, 1)))
      assert(newBoard.isMutable(Color.Green, Position(1, 0)))
      assert(newBoard.isMutable(Color.Green, Position(1, 1)))
    }

  }

}