package abstractions

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SideTest extends FunSpec {

  describe("[Side]") {

    val side = Side(1)(
      (value: Int, p: Any) => value + p.asInstanceOf[Int],
      (value: Int) => value < 3
    )

    it("should have theses properties") {
      assert(side.values === 1)
      assert(side.isIn)
      assert(!side.isOut)
    }

    it("should return a new instance on update") {
      {
        val newSide = side(1)
        assert(newSide.values === 2)
        assert(newSide.isIn)
        assert(!newSide.isOut)
      }
      {

        val newSide = side(2)
        assert(newSide.values === 3)
        assert(!newSide.isIn)
        assert(newSide.isOut)
      }
    }

  }

}