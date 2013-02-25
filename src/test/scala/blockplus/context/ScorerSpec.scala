package blockplus.context

import org.specs2.mutable._
import org.specs2.ScalaCheck
import blockplus.Color
import org.scalacheck._
import blockplus.piece.PieceTypeData
import scala.collection.JavaConversions._
import components.position.PositionInterface
import blockplus.move.Move
import blockplus.move.Moves
import blockplus.piece.PieceInterface
import blockplus.piece.PieceType
import scala.util.Random

class ScorerSpec extends SpecificationWithJUnit with ScalaCheck {
  val initial = -(1 + 2 + 3 + 3 + 5 * 4 + 12 * 5)
  implicit val colorGen = Arbitrary(Gen.oneOf(Color.values))
  implicit val pieceGen = Arbitrary(Gen.oneOf(PieceType.values flatMap (_.iterator)))
  implicit val allPiecesGen = Arbitrary(Gen.pick(21, PieceType.values filter (_.id != 0)) map (Random.shuffle(_)))

  "initial score should be -89" in {
    val scores = Scorer(MoveHistory())
    prop { (c: Color) => scores(c) must_== initial }
  }

  "after playing all pieces score should be 15" in {
    prop {
      (moves: Seq[PieceType]) =>
        val allMovesPlayed = moves.foldLeft(MoveHistory()) {
          case (hist, pieceType) => hist.play(Moves.getMove(Color.Blue, pieceType.iterator.next))
        }
        val scores = Scorer(allMovesPlayed)
        scores(Color.Blue) must_== 15
        prop { (c2: Color) =>
          c2 != Color.Blue ==> {
            scores(c2) must_== initial
          }
        }
    }
  }

  "after playing one piece score should be -89+piece.size and -89 for other colors" in {
    prop { (c: Color, p: PieceInterface) =>
      p.getId != 0 ==> {
        val scores = Scorer(MoveHistory().play(Moves.getMove(c, p)))
        scores(c) must_== initial + p.getSelfPositions.size

        prop { (c2: Color) =>
          c2 != c ==> {
            scores(c2) must_== initial
          }
        }
      }
    }
  }
}