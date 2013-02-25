package blockplus.context

import org.specs2.mutable._
import org.specs2.ScalaCheck
import blockplus.Color
import org.scalacheck._

class ScorerSpec extends SpecificationWithJUnit with ScalaCheck {
  val initial = -(1 + 2 + 3 + 3 + 5 * 4 + 12 * 5)
  implicit val colorGen = Arbitrary(Gen.oneOf(Color.values))

  "initial score should be -89" in {
    val scores = Scorer(MoveHistory())
    prop { (c: Color) => scores(c) must_== initial }
  }
}