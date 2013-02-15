package blockplus.context

import org.junit._
import org.junit.Assert._
import blockplus.move.Moves
import blockplus.piece.PieceType._
import blockplus.move.Move
import scala.collection.immutable.Stack
import blockplus.Color._
import org.junit.rules.ExpectedException
import java.util.NoSuchElementException

class MoveHistoryTest {

  @Test
  def testApplyNullMove() {
    val given = Moves.getNullMove(Blue)
    val actual = new MoveHistory().apply(given)
    val expected = new MoveHistory()
    assertEquals(expected, actual)
  }

  @Test
  def testApplyMove() {
    val given = Moves.getMove(Blue, PIECE1.iterator.next)
    val actual = new MoveHistory().apply(given)
    val expected = new MoveHistory(new Stack[Move].push(given))
    assertEquals(expected, actual)
  }

  @Test
  def testContainsMove() {
    assertFalse(new MoveHistory().isEmpty)
    val move = Moves.getMove(Blue, PIECE1.iterator.next)
    assertTrue(new MoveHistory().apply(move).isEmpty)
  }

  @Test
  def testContainsMoveHavingColor() {
    assertFalse(new MoveHistory().contains(Blue))
    val move = Moves.getMove(Blue, PIECE1.iterator.next)
    assertTrue(new MoveHistory().apply(move).contains(Blue))
    assertFalse(new MoveHistory().apply(move).contains(Green))
  }

  @Test(expected = classOf[NoSuchElementException])
  def testLastMoveOnEmptyHistory() {
    new MoveHistory().last
  }

  @Test
  def testLastMove() {
    {
      val move = Moves.getMove(Blue, PIECE1.iterator.next)
      val actual = new MoveHistory().apply(move).last
      val expected = move
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Blue, PIECE1.iterator.next)
      val move2 = Moves.getMove(Yellow, PIECE1.iterator.next)
      val move3 = Moves.getMove(Red, PIECE1.iterator.next)
      val move4 = Moves.getMove(Green, PIECE1.iterator.next)

      val actual = new MoveHistory()
        .apply(move1)
        .apply(move2)
        .apply(move3)
        .apply(move4)
        .last

      val expected = move4

      assertEquals(expected, actual)
    }
  }

  @Test(expected = classOf[NoSuchElementException])
  def testLastMoveColorOnHistoryWithoutMoveOfThisColor() {
    val move = Moves.getMove(Blue, PIECE1.iterator.next)
    new MoveHistory().apply(move).last(Green)
  }

  @Test
  def testLastMoveHavingColor() {
    {
      val move1 = Moves.getMove(Blue, PIECE1.iterator.next)
      val actual = new MoveHistory().apply(move1).last(Blue)
      val expected = move1
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Blue, PIECE1.iterator.next)
      val move2 = Moves.getMove(Green, PIECE1.iterator.next)
      val actual = new MoveHistory()
        .apply(move1)
        .apply(move2)
        .last(Blue)
      val expected = move1
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Blue, PIECE1.iterator.next)
      val move2 = Moves.getMove(Blue, PIECE2.iterator.next)
      val move3 = Moves.getMove(Green, PIECE1.iterator.next)
      val actual = new MoveHistory()
        .apply(move1)
        .apply(move2)
        .apply(move3)
        .last(Blue)
      val expected = move2
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Green, PIECE1.iterator.next)
      val move2 = Moves.getMove(Blue, PIECE1.iterator.next)
      val move3 = Moves.getMove(Blue, PIECE2.iterator.next)
      val move4 = Moves.getMove(Blue, PIECE3.iterator.next)
      val actual = new MoveHistory()
        .apply(move1)
        .apply(move2)
        .apply(move3)
        .apply(move4)
        .last(Blue)
      val expected = move4
      assertEquals(expected, actual)
    }
  }

  @Test
  def testToString() {
    val move = Moves.getMove(Blue, PIECE1.iterator.next)
    val actual = new MoveHistory().apply(move).toString
    val expected = "Stack(Move{color=Blue, piece=PieceComposite{id=1, positions=(0, 0)}})"
    assertEquals(expected, actual)
  }

}