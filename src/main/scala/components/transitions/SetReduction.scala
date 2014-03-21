package components.transitions

import components.transitions.Transition._

object SetReduction extends Reduction {

  def apply[A, B](transition: Transition[A]) = {
    val t = transition.asInstanceOf[Transition[Set[B]]]
    val left = t.left.get
    val right = t.right.get
    val union = left.union(right)
    val map1 = union.map(
      e => {
        (e, left.contains(e), right.contains(e))
      })
    map1.map(x => {
      val (element, isInLeft, isInRight) = x
      val left = if (isInLeft) Some(element) else None
      val right = if (isInRight) Some(element) else None
      Transition.TransitionBuilder(left, right).build()
    });
  }

  def apply[A, B](transition: Transition[A], equiv: (B, B) => Boolean): Iterable[Transition[B]] = {
    class Wrap(val element: B) {
      def get = element
      override def hashCode = element.hashCode
      override def equals(other: Any) = equiv(element, other.asInstanceOf[Wrap].get)
      override def toString = element.toString
    }
    val t = transition.asInstanceOf[Transition[Set[B]]]
    val left = t.left.get.map(x => new Wrap(x))
    val right = t.right.get.map(x => new Wrap(x))
    val tuples = left.union(right).map({ e =>
      val isInLeft = left.contains(e);
      val isInRight = right.contains(e)
      (e.get.hashCode, e, isInLeft, isInRight)
    }).toList
    val dataByHashCode = tuples.groupBy(_._1)
    def dataToTransition(data: List[(Int, Wrap, Boolean, Boolean)]): Transition[B] = {
      data match {
        case head :: Nil => {
          val (_, element, isInLeft, isInRight) = head
          val left = if (isInLeft) Some(element.get) else None
          val right = if (isInRight) Some(element.get) else None
          Transition.TransitionBuilder(left, right).build()
        }
        case _ => Transition.Alteration(data.head._2.get, data.last._2.get)
      }
    }
    dataByHashCode.values.map(dataToTransition(_))
  }

}