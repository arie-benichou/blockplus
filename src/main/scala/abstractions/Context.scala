package abstractions

import scala.collection.immutable.Stack

object Context {

  def apply[A, B, C, D](sides: Sides[A, B], space: C)(implicit spaceMutation: (Move[A, D], C) => C, isLegal: (Move[A, D], Context[A, B, C, D]) => Boolean): Context[A, B, C, D] =
    new Context[A, B, C, D](sides.first, sides, space, spaceMutation, isLegal, Stack.empty[Move[A, D]])

}

sealed case class Context[A, B, C, D] private (
  id: A,
  sides: Sides[A, B],
  space: C,
  spaceMutation: (Move[A, D], C) => C,
  isLegal: (Move[A, D], Context[A, B, C, D]) => Boolean,
  path: Stack[Move[A, D]]) {

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
    if (move.side == id && !isTerminal && isLegal(move, this))
      copy(id = id, sides = sides(move.side, move.data), space = spaceMutation(move, space), path = path.push(move))
    else this
}