package abstractions

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SidesTest extends FunSpec {

  describe("[Sides]") {

    val adversity = Adversity[String]("side1", "side2")
    val side = Side(true)(
      (value: Boolean, p: Any) => p.asInstanceOf[Boolean],
      (value: Boolean) => value
    )
    val sides = Sides(adversity, List(side, side))

    it("should have theses properties") {
      assert(sides.adversity === adversity)
      assert(sides.sides === Map("side1" -> side, "side2" -> side))
      assert(sides.count == 2)
      assert(sides.first == "side1")
    }

    it("should return the side next to a given side") {
      assert(sides.nextTo("side1") === "side2")
      assert(sides.nextTo("side2") === "side1")
    }

    it("should return the side corresponding to a given id") {
      assert(sides.side("side1") === side)
      assert(sides.side("side2") === side)
    }

    it("should return a new instance on update") {
      val newSides = sides("side1", false)
      assert(newSides.side("side1") === side(false))
      assert(newSides.side("side2") === side)
      assert(newSides.count === 1)
    }

  }

}