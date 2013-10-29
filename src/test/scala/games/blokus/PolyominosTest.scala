package games.blokus

import org.junit.Test
import org.junit.runner.RunWith
import org.scalatest.Assertions
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PositionsTest extends FunSpec {

  describe("[Polyominos]") {

    it("should render this way") {
      val source = scala.io.Source.fromFile("./src/test/resources/games/blokus/PolyominosTest")
      val expecteds = source.getLines
      val actual = Polyominos.values.map(p => p + "\n\n" + p.instances.mkString("\n\n"))
        def isSection(x: String) = x == "-------------8<-------------"
        def assertAll(expecteds: Iterator[String], actuals: List[String]) {
          if (expecteds.isEmpty) assert(actuals.isEmpty)
          else {
            val expected = expecteds.dropWhile(isSection).takeWhile(!isSection(_)).mkString("\n")
            val actual = actuals.head
            assert(expected === actual)
            assertAll(expecteds, actuals.tail)
          }
        }
      assertAll(expecteds, actual)
      source.close()
    }

  }

}