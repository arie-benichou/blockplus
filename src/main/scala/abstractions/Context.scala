package abstractions

import scala.collection.immutable.Stack

object Context {
  def apply[A, B, C, D](
    sides: Sides[A, B],
    space: C,
    spaceMutation: (Move[A, D], C) => C,
    isLegal: (Move[A, D], Context[A, B, C, D]) => Boolean,
    isTerminalFunction: (Context[A, B, C, D]) => Boolean,
    onNewContextFunction: Context[A, B, C, D] => Unit,
    lastMove: Move[A, D] = null): Context[A, B, C, D] =
    new Context[A, B, C, D](sides.first, sides, space, spaceMutation, isLegal, Stack[Context[A, B, C, D]](), isTerminalFunction, onNewContextFunction, lastMove)
}

sealed case class Context[A, B, C, D] private (
  id: A,
  sides: Sides[A, B],
  space: C,
  spaceMutation: (Move[A, D], C) => C,
  isLegal: (Move[A, D], Context[A, B, C, D]) => Boolean,
  history: Stack[Context[A, B, C, D]],
  isTerminalFunction: (Context[A, B, C, D]) => Boolean, onNewContextFunction: Context[A, B, C, D] => Unit,
  lastMove: Move[A, D]) {
  onNewContextFunction(this)
  lazy val isTerminal: Boolean = isTerminalFunction(this)
  lazy val sideToPlay: Side[B] = side(id)
  lazy val next: A = next(id)
  def side(id: A): Side[B] = sides.side(id)
  def next(id: A): A = {
    if (isTerminal) this.id else {
      val nextSideId = sides.nextTo(id)
      if (sides.side(nextSideId).isOut) next(nextSideId) else nextSideId
    }
  }
  //def forward(): Context[A, B, C, D] = if (isTerminal) this else copy(next)
  def apply(move: Move[A, D])(implicit ensureMoveIsLegal: Boolean = true): Context[A, B, C, D] =
    if (move.side == id && !isTerminal && (!ensureMoveIsLegal || isLegal(move, this)))
      copy(id = next, sides = sides(move.side, move.data), space = spaceMutation(move, space), lastMove = move, history = history.push(this))
    else error("Illegal Move: " + move)
  def onNewContext(f: Context[A, B, C, D] => Unit): Context[A, B, C, D] = copy(onNewContextFunction = f)
}