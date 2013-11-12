package abstractions

object Sides {

  def apply[A, B](adversity: Adversity[A], sides: List[Side[B]]): Sides[A, B] =
    new Sides(adversity, (adversity.ids zip sides).toMap)

}

sealed case class Sides[A, B](adversity: Adversity[A], sides: Map[A, Side[B]]) extends Iterable[(A, Side[B])] {

  lazy val count: Int = sides.values.count(_.isIn)

  val first: A = adversity.first

  def nextTo(id: A): A = adversity.successorOf(id)

  def side(id: A): Side[B] = sides.get(id).get

  def apply(id: A, data: Any): Sides[A, B] = copy(sides = sides.updated(id, side(id).apply(data)))

  def iterator = sides.iterator

}