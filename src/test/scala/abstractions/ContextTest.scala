package abstractions

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable.Stack
@RunWith(classOf[JUnitRunner])
class ContextTest extends FunSpec {

  describe("[Context]") {

    val adversity = Adversity("side1", "side2")

    val side1 = Side(0)(
      (value: Int, p: Any) => value + p.asInstanceOf[Int],
      (value: Int) => value < 4
    )

    val side2 = Side(1)(
      (value: Int, p: Any) => value + p.asInstanceOf[Int],
      (value: Int) => value < 4
    )

    val sides = Sides(adversity, List(side1, side2))

    sealed case class Move(side: String, data: Int) extends abstractions.Move[String, Int]

    def spaceMutation(move: abstractions.Move[String, Int], space: List[(String, Int)]) =
      (move.side, move.data) :: space

    def isLegal(move: abstractions.Move[String, Int], context: Context[String, Int, List[(String, Int)], Int]) = true
    def isTerminal(context: Context[String, Int, List[(String, Int)], Int]) = context.sides.count == 0
    def onUpdate(context: Context[String, Int, List[(String, Int)], Int]) = ()

    val context = Context(sides, List.empty[(String, Int)], spaceMutation, isLegal, isTerminal, onUpdate)

    val sideOut1 = Side(-1)(
      (value: Int, p: Any) => value,
      (value: Int) => value > 0
    )

    val sideOut2 = Side(-2)(
      (value: Int, p: Any) => value,
      (value: Int) => value > 0
    )

    val sidesOut = Sides(adversity, List(sideOut1, sideOut2))

    val terminalContext = Context(sidesOut, List.empty[(String, Int)], spaceMutation, isLegal, isTerminal, onUpdate)

    it("should verify theses properties") {
      assert(context.id === "side1")
      assert(context.sides === sides)
      assert(context.space === List.empty[(String, Int)])
      assert(context.history.isEmpty)
      assert(context.isTerminal === false)
      assert(context.sideToPlay === side1)
      assert(context.next === "side2")
    }

    it("should return a side for a given side id") {
      assert(context.side("side1") === side1)
      assert(context.side("side2") === side2)
    }

    it("should return the side next to a given side") {
      assert(context.next("side1") === "side2")
      assert(context.next("side2") === "side1")
    }

    it("should return the current side if in terminal context") {
      assert(terminalContext.isTerminal)
      assert(terminalContext.next("side1") === "side1")
      assert(terminalContext.next("side2") === "side1")
      assert(terminalContext.next("unkown side") === "side1")
    }

    it("should return the next side that is not outside") {
      val adversity = Adversity("side1", "side2", "side3", "side4")
      val inside = Side(1)(
        (value: Int, p: Any) => value,
        (value: Int) => value != 0
      )
      val outside = Side(0)(
        (value: Int, p: Any) => value,
        (value: Int) => value != 0
      )
      val sides = Sides(adversity, List(inside, outside, outside, inside))
      val context = Context(sides, List.empty[(String, Int)], spaceMutation, isLegal, isTerminal, onUpdate)
      assert(!context.isTerminal)
      assert(context.next("side2") === "side4")
      assert(context.next("side1") === "side4")
    }

    it("should return a new instance on update") {
      val move = Move("side1", 1)
      val newContext = context(move)
      assert(newContext.id === context.next)
      assert(newContext.sides === context.sides(move.side, move.data))
      assert(newContext.space === (move.side, move.data) :: context.space)
      assert(newContext.isTerminal === false)
      assert(newContext.history === Stack(context))
      assert(newContext.sideToPlay === newContext.side("side2"))
      assert(newContext.next === context.id)
    }

    it("should throw an exception if move is not for the side to play") {
      assert(context.id === "side1")
      val move = Move("side2", 1)
      intercept[RuntimeException] {
        context(move)
      }
    }

    it("should throw an exception if terminal") {
      val move = Move("side1", 1)
      assert(terminalContext.isTerminal)
      intercept[RuntimeException] {
        terminalContext(move)
      }
    }

  }

}
