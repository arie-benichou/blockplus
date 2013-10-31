package abstractions

object Context {

  def apply[A, B, C, D](sides: Sides[A, B], space: C)(implicit spaceMutation: (Move[A, D], C) => C): Context[A, B, C, D] =
    new Context[A, B, C, D](sides.first, sides, space, spaceMutation)

}
// TODO add path: history of moves : Stack[Move]
sealed case class Context[A, B, C, D] private (id: A, sides: Sides[A, B], space: C, spaceMutation: (Move[A, D], C) => C) {

  lazy val isTerminal: Boolean = (sides.count == 0)
  lazy val sideToPlay: Side[B] = side(id)
  lazy val next: A = next(id)

  def side(id: A): Side[B] = sides.side(id)

  def next(id: A): A = {
    if (isTerminal) this.id else {
      val nextSideId = sides.nextTo(id)
      if (sides.side(nextSideId).isOut) next(nextSideId) else nextSideId
    }
  }

  def forward(): Context[A, B, C, D] = if (isTerminal) this else copy(next)

  def apply(move: Move[A, D]): Context[A, B, C, D] =
    if (move.side == id && !isTerminal) copy(id, sides(move.side, move.data), spaceMutation(move, space)) else this
}