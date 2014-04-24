package components

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import components.Positions.Position

@RunWith(classOf[JUnitRunner])
class CellsTest extends FunSpec {

  /*

  describe("[Cells]") {

    it("should have a dimension, an initial symbol, an undefined symbol, and data") {
      val cells = Cells(1, 2, 'initial, 'undefined)
      assert(cells.rows === 1)
      assert(cells.columns === 2)
      assert(cells.initial === 'initial)
      assert(cells.undefined === 'undefined)
      assert(cells.data === Map())
    }

    it("should return a set of defined positions...") {
      val cells = Cells(1, 2, 'initial, 'undefined, Map(Position(0, 0) -> 'defined))
      assert(cells.definedPositions === Set(Position(0, 0)))
    }

    it("... and filter out defined positions with initial symbol") {
      val data = Map(
        Position(0, 0) -> 'defined,
        Position(0, 1) -> 'initial,
        Position(1, 0) -> 'initial
      )
      val cells = Cells(1, 2, 'initial, 'undefined, data)
      assert(cells.definedPositions === Set(Position(0, 0)))
    }

    it("should return a symbol for a given position") {
      val data = Map(
        Position(0, 1) -> 'defined,
        Position(0, 2) -> 'extended
      )
      val cells = Cells(1, 2, 'initial, 'undefined, data)
      assert(cells.get(Position(0, -1)) === 'undefined)
      assert(cells.get(Position(0, 0)) === 'initial)
      assert(cells.get(Position(0, 1)) === 'defined)
      assert(cells.get(Position(0, 2)) === 'extended)
    }

    it("should return a set of defined positions for a given predicate") {
      val data = Map(
        Position(0, 1) -> 'defined,
        Position(1, 1) -> 'defined,
        Position(0, 2) -> 'extended
      )
      val cells = Cells(1, 2, 'initial, 'undefined, data)
      assert(cells.filter((_._2 == 'defined)) === Set(Position(0, 1), Position(1, 1)))
      assert(cells.filter((_._2 == 'extended)) === Set(Position(0, 2)))
      assert(cells.filter((_._1.row == 0)) === Set(Position(0, 1), Position(0, 2)))
    }

    it("should return a new instance of cells on update") {
      val cells = Cells(1, 2, 'initial, 'undefined)
      assert(cells.get(Position(0, -1)) === 'undefined)
      assert(cells.get(Position(0, 0)) === 'initial)
      assert(cells.get(Position(0, 1)) === 'initial)
      assert(cells.get(Position(0, 2)) === 'undefined)
      val data = Map(
        Position(0, -1) -> 'extended,
        Position(0, 1) -> 'undefined,
        Position(0, 2) -> 'extended
      )
      val updatedCells = cells(data)
      assert(updatedCells.get(Position(0, -1)) === 'extended)
      assert(updatedCells.get(Position(0, 0)) === 'initial)
      assert(updatedCells.get(Position(0, 1)) === 'undefined)
      assert(updatedCells.get(Position(0, 2)) === 'extended)
    }

  }
  */

}