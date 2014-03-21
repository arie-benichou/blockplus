package abstractions

object Adversity {

  def apply[A](ids: A*): Adversity[A] = new Adversity[A](ids.toList)

}

sealed case class Adversity[A] private (ids: List[A]) {

  val size: Int = ids.size

  lazy val first: A = IdBynaturalId.get(0).get

  private val nids = (0 until size)
  private val naturalIdById = (ids zip nids).toMap
  private val IdBynaturalId = (nids zip ids).toMap
  private def successorOf(i: Int) = (i + 1) % size

  def successorOf(id: A): A = IdBynaturalId.get(successorOf(naturalIdById.get(id).get)).get

}