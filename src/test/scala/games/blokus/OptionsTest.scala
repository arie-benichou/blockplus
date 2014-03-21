package games.blokus

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import components.Positions.Position
import games.blokus.Game.Color
import games.blokus.Game.Pieces
import games.blokus.Polyominos.Polyomino
import games.blokus.Polyominos.Instances.NormalizedInstance

@RunWith(classOf[JUnitRunner])
class OptionsTest extends FunSpec {

  describe("[Options]") {

    it("should define a null option") {
      assert(Options.NullOption === ((Position(-1, -1), Polyominos._0, Set.empty)))
    }

    it("should define a null object") {
      assert(Options.Null === Set(Options.NullOption))
    }

    it("should return the null object if pieces contains solely the null piece") {
      val color = Color.Blue
      val board = Board(2, 2)
      assert(Options.get(color, board, Pieces()) === Set())
      assert(Options.get(color, board, Pieces(Polyominos._0)) === Options.Null)
    }

    it("should not return the null option if another option exists") {
      val color = Color.Blue
      val board = Board(2, 2)
      val pieces = Pieces(Polyominos._0, Polyominos._1)
      val options = Options.get(color, board, pieces)
      assert(options != Set())
      assert(options != Options.Null)
      assert(options.size === 1)
      assert(options.head === (Position(0, 0), Polyominos._1, Set(Position(0, 0))))
    }

    it("should return the null option if another option does not exist") {
      val color = Color.Blue
      val board = Board(2, 2)
      val pieces = Pieces(Polyominos._0, Polyominos._3)
      assert(Options.get(color, board, Pieces(Polyominos._0, Polyominos._2)) != Options.Null)
      assert(Options.get(color, board, Pieces(Polyominos._0, Polyominos._3)) === Options.Null)
    }

    it("should return legal options (1)") {
      val color = Color.Blue
      val board = Board(2, 2)
      val lights = board.lights(Color.Blue)
      assert(lights.size === 1)
      val light = lights.head
      assert(light === Position(0, 0))
      val pieces = Pieces(Polyominos._1)
      val instance = Polyominos._1.instances.head
      assert(instance.positions === Set(Position(0, 0)))
      assert(instance.connectors.size === 1)
      val connector = instance.connectors.head
      assert(connector === Position(0, 0))
      val translation = instance.positions.map(_ + (light - connector));
      assert(board.isMutable(color, translation))
      val expected = (light, Polyominos._1, translation)
      val options = Options.get(color, board, pieces)
      assert(options.size === 1)
      val actual = options.head
      assert(expected === actual)
    }

    it("should return legal options (2)") {
      val board = Board(2, 2);
      val color = Color.Blue
      val lights = board.lights(color)
      assert(lights.size === 1)
      val light = lights.head
      assert(light === Position(0, 0))
      val pieces = Pieces(Polyominos._1)
      val expected = (light, Polyominos._1, Set(Position(0, 0)))
      val options = Options.get(color, board, pieces)
      assert(options.size === 1)
      val actual = options.head
      assert(expected === actual)
    }

    it("should return legal options (3)") {
      val board = Board(2, 2);
      val color = Color.Blue
      val lights = board.lights(color)
      assert(lights.size === 1)
      val light = lights.head
      assert(light === Position(0, 0))
      val pieces = Pieces(Polyominos._2)
      val expected = Set(
        (light, Polyominos._2, Set(Position(0, 0), Position(0, 1))),
        (light, Polyominos._2, Set(Position(0, 0), Position(1, 0)))
      )
      val actual = Options.get(color, board, pieces)
      assert(expected === actual)
    }

    it("should return legal options (4)") {
      val board = Board(2, 2);
      val color = Color.Blue
      val lights = board.lights(color)
      assert(lights.size === 1)
      val light = lights.head
      assert(light === Position(0, 0))
      val pieces = Pieces(Polyominos._1, Polyominos._2)
      val expected = Set(
        (light, Polyominos._1, Set(Position(0, 0))),
        (light, Polyominos._2, Set(Position(0, 0), Position(0, 1))),
        (light, Polyominos._2, Set(Position(0, 0), Position(1, 0)))
      )
      val actual = Options.get(color, board, pieces)
      assert(expected === actual)
    }

    it("should return legal options (5)") {
      val board = Board(2, 2);
      val color = Color.Blue
      val lights = board.lights(color)
      assert(lights.size === 1)
      val light = lights.head
      assert(light === Position(0, 0))
      val pieces = Pieces(Polyominos._1, Polyominos._2, Polyominos._3)
      val expected = Set(
        (light, Polyominos._1, Set(Position(0, 0))),
        (light, Polyominos._2, Set(Position(0, 0), Position(0, 1))),
        (light, Polyominos._2, Set(Position(0, 0), Position(1, 0)))
      )
      val actual = Options.get(color, board, pieces)
      assert(expected === actual)
    }

    it("should return legal options (6)") {
      val tmp = Board(2, 2);
      val color = Color.Blue
      val lights = Set(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1))
      val board = tmp(color, Set(), Set(), lights)
      val pieces = Pieces(Polyominos._1)
      val expected = Set(
        (Position(0, 0), Polyominos._1, Set(Position(0, 0))),
        (Position(0, 1), Polyominos._1, Set(Position(0, 1))),
        (Position(1, 0), Polyominos._1, Set(Position(1, 0))),
        (Position(1, 1), Polyominos._1, Set(Position(1, 1)))
      )
      val actual = Options.get(color, board, pieces)
      assert(expected === actual)
    }

    it("should return legal options (7)") {
      val board = Board(3, 3);
      val pieces = Pieces(Polyominos._0, Polyominos._1, Polyominos._2, Polyominos._3, Polyominos._5);
      {
        val color = Color.Blue
        val expected = Set(
          (Position(0, 0), Polyominos._1, Set(Position(0, 0))),
          (Position(0, 0), Polyominos._2, Set(Position(0, 0), Position(0, 1))),
          (Position(0, 0), Polyominos._2, Set(Position(0, 0), Position(1, 0))),
          (Position(0, 0), Polyominos._3, Set(Position(0, 0), Position(0, 1), Position(0, 2))),
          (Position(0, 0), Polyominos._3, Set(Position(0, 0), Position(1, 0), Position(2, 0)))
        )
        val actual = Options.get(color, board, pieces)
        assert(expected === actual)
      }

      {
        val color = Color.Yellow
        val expected = Set(
          (Position(0, 2), Polyominos._1, Set(Position(0, 2))),
          (Position(0, 2), Polyominos._2, Set(Position(0, 2), Position(0, 1))),
          (Position(0, 2), Polyominos._2, Set(Position(0, 2), Position(1, 2))),
          (Position(0, 2), Polyominos._3, Set(Position(0, 2), Position(0, 1), Position(0, 0))),
          (Position(0, 2), Polyominos._3, Set(Position(0, 2), Position(1, 2), Position(2, 2)))
        )
        val actual = Options.get(color, board, pieces)
        assert(expected === actual)
      }

      {
        val color = Color.Red
        val expected = Set(
          (Position(2, 2), Polyominos._1, Set(Position(2, 2))),
          (Position(2, 2), Polyominos._2, Set(Position(2, 2), Position(2, 1))),
          (Position(2, 2), Polyominos._2, Set(Position(2, 2), Position(1, 2))),
          (Position(2, 2), Polyominos._3, Set(Position(2, 2), Position(2, 1), Position(2, 0))),
          (Position(2, 2), Polyominos._3, Set(Position(2, 2), Position(1, 2), Position(0, 2)))
        )
        val actual = Options.get(color, board, pieces)
        assert(expected === actual)
      }

      {
        val color = Color.Green
        val expected = Set(
          (Position(2, 0), Polyominos._1, Set(Position(2, 0))),
          (Position(2, 0), Polyominos._2, Set(Position(2, 0), Position(2, 1))),
          (Position(2, 0), Polyominos._2, Set(Position(2, 0), Position(1, 0))),
          (Position(2, 0), Polyominos._3, Set(Position(2, 0), Position(2, 1), Position(2, 2))),
          (Position(2, 0), Polyominos._3, Set(Position(2, 0), Position(1, 0), Position(0, 0)))
        )
        val actual = Options.get(color, board, pieces)
        assert(expected === actual)
      }

    }

  }

}