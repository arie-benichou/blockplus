package components

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import components.Positions._

@RunWith(classOf[JUnitRunner])
class CellsTest extends FunSpec {

  describe("[Cells]") {

    it("should verify the following properties") {
      val positions = Positions.from((0, 0)).to((0, 1))
      val cells = Cells(positions.apply('default), 'default, 'undefined)
      assert(cells.default === 'default)
      assert(cells.undefined === 'undefined)
      assert(cells.min === positions.min)
      assert(cells.max === positions.max)
      assert(cells.positions === positions.positions)
    }

    it("should allow to filter positions with default symbol") {
      val positions = Positions.from((0, 0)).to((0, 1))
      val data = positions.apply('default)
        .updated((0, 1), 'other)
      val cells = Cells(data, 'default, 'undefined)
      assert(cells.filterDefaults() === Set(Position(0, 0)))
    }

    it("should return a symbol for a given position") {
      val positions = Positions.from((0, 0)).to((0, 1))
      val data = positions.apply('default)
        .updated((0, 1), 'other)
        .updated((0, 2), 'another)
      val cells = Cells(data, 'default, 'undefined)
      assert(cells.get(Position(0, -1)) === 'undefined)
      assert(cells.get(Position(0, 0)) === 'default)
      assert(cells.get(Position(0, 1)) === 'other)
      assert(cells.get(Position(0, 2)) === 'another)
    }

    it("should return a set of defined positions for a given predicate") {
      val positions = Positions.from((0, 0)).to((0, 1))
      val data = positions.apply('default)
        .updated((0, 1), 'other)
        .updated((0, 2), 'another)
      val cells = Cells(data, 'default, 'undefined)
      assert(cells.filterOthers() === Set(Position(0, 1), Position(0, 2)))
      assert(cells.filterOthers((_._2 == 'another)) === Set(Position(0, 2)))
      assert(cells.filter((_._1.row == 0)) === Set(Position(0, 0), Position(0, 1), Position(0, 2)))
    }

    it("should return a new instance of cells on update") {
      val positions = Positions.from((0, 0)).to((0, 1))
      val data = positions.apply('default)
      val cells = Cells(data, 'default, 'undefined)
      assert(cells.get(Position(0, -1)) === 'undefined)
      assert(cells.get(Position(0, 0)) === 'default)
      assert(cells.get(Position(0, 1)) === 'default)
      assert(cells.get(Position(0, 2)) === 'undefined)
      val updatedData = data
        .updated((0, -1), 'another)
        .updated((0, 1), 'undefined)
        .updated((0, 2), 'another)
      val updatedCells = cells(updatedData)
      assert(updatedCells.get(Position(0, -1)) === 'another)
      assert(updatedCells.get(Position(0, 0)) === 'default)
      assert(updatedCells.get(Position(0, 1)) === 'undefined)
      assert(updatedCells.get(Position(0, 2)) === 'another)
    }

  }

}