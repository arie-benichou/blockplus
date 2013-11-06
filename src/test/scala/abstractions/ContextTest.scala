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

    val context = Context(sides, List.empty[(String, Int)])(spaceMutation, isLegal)

    val sideOut1 = Side(-1)(
      (value: Int, p: Any) => value,
      (value: Int) => value > 0
    )

    val sideOut2 = Side(-2)(
      (value: Int, p: Any) => value,
      (value: Int) => value > 0
    )

    val sidesOut = Sides(adversity, List(sideOut1, sideOut2))

    val terminalContext = Context(sidesOut, List.empty[(String, Int)])(spaceMutation, isLegal)

    it("should verify theses properties") {
      assert(context.id === "side1")
      assert(context.sides === sides)
      assert(context.space === List.empty[(String, Int)])
      assert(context.path.isEmpty)
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
      val context = Context(sides, List.empty[(String, Int)])(spaceMutation, isLegal)
      assert(!context.isTerminal)
      assert(context.next("side2") === "side4")
      assert(context.next("side1") === "side4")
    }

    it("should forward a new instance having the next side as the side to play") {
      assert(!context.isTerminal)
      assert(context.sideToPlay === side1)
      val nextContext = context.forward
      assert(nextContext.sideToPlay === side2)
      assert(nextContext.id === context.next)
      assert(nextContext.sides === context.sides)
      assert(nextContext.space === context.space)
      assert(nextContext.isTerminal === context.isTerminal)
      assert(nextContext.next === context.id)
    }

    it("should forward its own instance if terminal") {
      assert(terminalContext.isTerminal)
      assert(terminalContext.forward === terminalContext)
    }

    it("should return a new instance on update") {
      val move = Move("side1", 1)
      val newContext = context(move)
      assert(newContext.id === context.id)
      assert(newContext.sides === context.sides.apply(move.side, move.data))
      assert(newContext.space === (move.side, move.data) :: context.space)
      assert(newContext.isTerminal === false)
      assert(newContext.path === Stack(move))
      assert(newContext.sideToPlay === context.sideToPlay(1))
      assert(newContext.next === context.next)
    }

    it("should return its own instance if move is not for the side to play") {
      assert(context.id === "side1")
      val move = Move("side2", 1)
      assert(context(move) === context)
    }

    it("should return its own instance if terminal") {
      val move = Move("side1", 1)
      assert(terminalContext.isTerminal)
      assert(terminalContext(move) === terminalContext)
    }

  }

}