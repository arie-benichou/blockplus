package abstractions

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MoveTest extends FunSpec {

  describe("[Move]") {

    it("should have a side id and some data") {
      sealed case class Move(side: Int, data: String) extends abstractions.Move[Int, String]
      val move = Move(0, "zero")
      assert(move.side === 0)
      assert(move.data === "zero")
    }

  }

}