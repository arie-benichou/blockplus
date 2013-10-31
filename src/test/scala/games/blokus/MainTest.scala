package games.blokus

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

import games.blokus.Game.Color

@RunWith(classOf[JUnitRunner])
class MainTest extends FunSpec {

  describe("[Main]") {

    it("should have this result") {
      // TODO tester chemin + résultat => TODO add path of moves to Context
      val expected = Map(
        Color.Blue -> 23,
        Color.Yellow -> 20,
        Color.Red -> 7,
        Color.Green -> 9
      )
      val terminalContext = Main.run(Game.context, Main.nullRenderer)

      val actual = terminalContext.sides.sides.map(side => {
        (side._1, side._2.values.weight)
      })

      assert(expected === actual)
    }

  }

}