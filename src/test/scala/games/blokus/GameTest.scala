package games.blokus

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import components.Positions.Position
import games.blokus.Game.Color
import games.blokus.Game.Pieces
import games.blokus.Polyominos.Polyomino
import games.blokus.Polyominos.Instances.NormalizedInstance
import abstractions.Side
import games.blokus.Polyominos.Instances.Instance
import games.blokus.Game.Move

@RunWith(classOf[JUnitRunner])
class GameTest extends FunSpec {

  describe("[Game]") {

    val context = Game.context
    val value = Pieces(Polyominos.values: _*)

    it("should verify theses properties") {

      assert(context.isTerminal === false)
      assert(context.sides.count === 4)

      assert(context.id == Color.Blue)
      assert(context.forward.id === Color.Yellow)
      assert(context.forward.forward.id === Color.Red)
      assert(context.forward.forward.forward.id === Color.Green)
      assert(context.forward.forward.forward.forward.id === Color.Blue)

      assert(context.side(Color.Blue).values === value)
      assert(context.side(Color.Yellow).values === value)
      assert(context.side(Color.Red).values === value)
      assert(context.side(Color.Green).values === value)

      assert(context.side(Color.Blue).values.weight === 89)

    }

    it("should verify theses behaviours") {

      assert(context.sideToPlay.isIn)
      assert(context.space.selves(context.id).isEmpty);

      {
        val p1 = Polyominos._1.instances.head.translateBy((0, 0))
        assert(context.sideToPlay.values.contains(p1.selfType))
        val move = Move(Color.Blue, p1)
        val newContext = context.apply(move)
        assert(!newContext.sideToPlay.values.contains(p1.selfType))
        assert(!newContext.space.selves(context.id).isEmpty)
        assert(newContext.sideToPlay.isIn)
      }

      {
        val p0 = Polyominos._0.instances.head.translateBy((0, 0))
        assert(context.sideToPlay.values.contains(p0.selfType))
        assert(context.space.selves(context.id).isEmpty)
        val move = Move(Color.Blue, p0)
        val newContext = context.apply(move)
        assert(!newContext.sideToPlay.values.contains(p0.selfType))
        assert(newContext.space.selves(context.id).isEmpty)
        assert(newContext.sideToPlay.isOut)
      }

    }

  }

}