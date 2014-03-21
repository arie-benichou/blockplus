package components.transitions

object Transition {

  trait Transition[+A] {
    def left: Option[A]
    def right: Option[A]
  }

  case object NullTransition extends Transition[Nothing] {
    val left = None
    val right = None
  }

  implicit def someWrapper[A](x: A) = Some(x)

  case class TransitionBuilder[A](left: Option[A] = None, right: Option[A] = None) extends Transition[A] {
    def build(): Transition[A] = this match {
      case TransitionBuilder(None, Some(x)) => Insertion(x)
      case TransitionBuilder(Some(x), None) => Deletion(x)
      case TransitionBuilder(Some(x), Some(y)) if x == y => Retention(x, y)
      case TransitionBuilder(Some(x), Some(y)) if x != y => Alteration(x, y)
      case _ => NullTransition
    }
  }

  case class from[A](left: Option[A]) {
    def to(right: Option[A]) = TransitionBuilder(left, right).build()
  }

  case class Insertion[A](rightSide: A) extends Transition[A] {
    val left = None
    val right = Some(rightSide)
  }

  case class Deletion[A](leftSide: A) extends Transition[A] {
    val left = Some(leftSide)
    val right = None
  }

  case class Retention[A](leftSide: A, rightSide: A) extends Transition[A] {
    val left = Some(leftSide)
    val right = Some(rightSide)
  }

  case class Alteration[A](leftSide: A, rightSide: A) extends Transition[A] {
    val left = Some(leftSide)
    val right = Some(rightSide)
  }

}