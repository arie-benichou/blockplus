package components

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

import components.Positions.Ordering
import components.Positions.Position

@RunWith(classOf[JUnitRunner])
class PositionsTest extends FunSpec {

  describe("[Position]") {

    it("should have a row and a column") {
      val position = Position(1, 2)
      assert(position.row === 1)
      assert(position.column === 2)
    }

    it("should return a position for : _ + [Direction]") {
      val position = Position(1, 2)
      val vector = (-2, 1)
      assert(position + vector === Position(-1, 3))
    }

    it("should return a vector for : _ - [Position]") {
      val position1 = Position(1, 2)
      val position2 = Position(-1, 3)
      assert(position1 - position2 === (2, -1))
    }

    it("should return a set of positions for : _ * Set[Direction]") {
      val position = Position(0, 0)
      assert(position * Set((1, 2), (3, 4)) === Set(Position(1, 2), Position(3, 4)))
    }

  }

  describe("[Positions]") {

    it("should return the top left corner of the bounding box for a given set of positions") {
      val positions = Set(Position(1, 2), Position(2, 1))
      assert(Positions.topLeftCorner(positions) === Position(1, 1))
    }

    it("should return a set of translated positions for a given vector and a given set of positions") {
      val positions = Set(Position(1, 2), Position(3, 4))
      val vector = (-1, 1)
      assert(Positions.translateBy(vector, positions) === Set(Position(0, 3), Position(2, 5)))
    }

    it("should define an natural ordering beetween two positions...") {
      assert(Positions.Ordering.compare(Position(0, 0), Position(1, 0)) === -1)
      assert(Positions.Ordering.compare(Position(1, 0), Position(0, 0)) === 1)
      assert(Positions.Ordering.compare(Position(0, 0), Position(0, 1)) === -1)
      assert(Positions.Ordering.compare(Position(0, 1), Position(0, 0)) === 1)
      assert(Positions.Ordering.compare(Position(0, 0), Position(0, 0)) === 0)
    }

    it("... and this natural ordering should be implicit") {
      val unsortedList = List(Position(1, 0), Position(0, 0))
      val sortedList = unsortedList.sorted
      assert(unsortedList != sortedList)
      val expected = List(Position(0, 0), Position(1, 0))
      assert(expected === sortedList)
    }

    it("should define an origin") {
      assert(Positions.Origin === Position(0, 0))
    }

    it("should be able to create a set of positions from bounds") {
      val positions = Positions.from((0, 0)).to((1, 1))
      assert(positions.min === Position(0, 0))
      assert(positions.max === Position(1, 1))
      assert(positions('.') === Map(
        Position(0, 0) -> '.',
        Position(0, 1) -> '.',
        Position(1, 0) -> '.',
        Position(1, 1) -> '.'
      ))
    }

  }

}