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
import scala.collection.immutable.Stack

class ScorerSpec extends SpecificationWithJUnit with ScalaCheck {
  val initial = -(1 + 2 + 3 + 3 + 5 * 4 + 12 * 5)
  implicit val colorGen = Arbitrary(Gen.oneOf(Color.values))
  implicit val pieceGen = Arbitrary(Gen.oneOf(PieceType.values flatMap (_.iterator)))
  implicit val allPiecesGen = Arbitrary(Gen.pick(21, PieceType.values filter (_.id != 0)) map (Random.shuffle(_)))
  val pieceSequenceWithBonus = allPiecesGen.arbitrary filter hasBonus

  "initial score should be -89" in {
    val scores = Scorer(MoveHistory())
    prop { (c: Color) => scores(c) must_== initial }
  }

  "after playing all pieces score should be 15 (and initial for others)" in {
    prop {
      (pieces: Seq[PieceType]) =>
        !hasBonus(pieces) ==> {
          val allMovesPlayed = playAll(pieces map { p => Moves.getMove(Color.Blue, p.iterator.next) })
          val scores = Scorer(allMovesPlayed)
          scores(Color.Blue) must_== 15
          prop { (c2: Color) =>
            c2 != Color.Blue ==> {
              scores(c2) must_== initial
            }
          }
        }
    }
  }

  "after playing all pieces score should be 15 (check for bonus 5 points)" in {
    val prop = Prop.forAll(pieceSequenceWithBonus)(pieces => {
      val allMovesPlayed = playAll(pieces map { p => Moves.getMove(Color.Blue, p.iterator.next) })
      val scores = Scorer(allMovesPlayed)
      scores(Color.Blue) must_== 20
    })
    val params = new Test.Parameters.Default { override val maxDiscardRatio: Float = 50 }
    Test.check(params, prop).status must_== Test.Passed
  }

  "after playing all pieces score should be 15 for everyone" in {
    val moves = for {
      (color, pieces) <- Color.values map (_ -> allPiecesGen.arbitrary.filter(!hasBonus(_)).sample.get) toMap;
      p <- pieces
    } yield Moves.getMove(color, p.iterator.next)
    val history = playAll(Random.shuffle(moves))
    for (c <- Color.values) Scorer(history)(c) must_== 15
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

  private def hasBonus(moves: Seq[PieceType]) = moves.last.size == 1

  private def playAll(moves: Iterable[Move]): MoveHistory =
    moves.foldLeft(MoveHistory()) { case (h, m) => h.play(m) }
}