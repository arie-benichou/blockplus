package components.transitions

import components.transitions.Transition._

trait Reduction {

  def apply[A, B](transition: Transition[A]): Iterable[Transition[B]]
  def apply[A, B](transition: Transition[A], equiv: (B, B) => Boolean): Iterable[Transition[B]]

}