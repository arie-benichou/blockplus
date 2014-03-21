package abstractions

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AdversityTest extends FunSpec {

  describe("[Adversity]") {

    val adversity = Adversity[String]("side1", "side2")

    it("should have theses properties") {
      assert(adversity.first === "side1")
      assert(adversity.ids === List("side1", "side2"))
      assert(adversity.size === 2)
      assert(adversity.successorOf("side1") === "side2")
    }

    it("should return the succesor of one side") {
      assert(adversity.successorOf("side1") === "side2")
      assert(adversity.successorOf("side2") === "side1")
    }

  }

}