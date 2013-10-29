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
import scala.collection.JavaConversions._

class MoveHistoryTest {

  @Test
  def testApplyNullMove() {
    val given = Moves.getNullMove(Blue)
    val actual = MoveHistory().play(given)
    val expected = MoveHistory()
    assertEquals(expected, actual)
  }

  @Test
  def testApplyMove() {
    val given = Moves.getMove(Blue, PIECE1.head)
    val actual = MoveHistory().play(given)
    val expected = MoveHistory(Stack(given))
    assertEquals(expected, actual)
  }

  @Test
  def testContainsMove() {
    assertFalse(MoveHistory().isEmpty)
    val move = Moves.getMove(Blue, PIECE1.head)
    assertTrue(MoveHistory().play(move).isEmpty)
  }

  @Test
  def testContainsMoveHavingColor() {
    assertFalse(MoveHistory().contains(Blue))
    val move = Moves.getMove(Blue, PIECE1.head)
    assertTrue(MoveHistory().play(move).contains(Blue))
    assertFalse(MoveHistory().play(move).contains(Green))
  }

  @Test(expected = classOf[NoSuchElementException])
  def testLastMoveOnEmptyHistory() {
    MoveHistory().last
  }

  @Test
  def testLastMove() {
    {
      val move = Moves.getMove(Blue, PIECE1.head)
      val actual = MoveHistory().play(move).last
      val expected = move
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Blue, PIECE1.head)
      val move2 = Moves.getMove(Yellow, PIECE1.head)
      val move3 = Moves.getMove(Red, PIECE1.head)
      val move4 = Moves.getMove(Green, PIECE1.head)

      val actual = MoveHistory()
        .play(move1)
        .play(move2)
        .play(move3)
        .play(move4)
        .last

      val expected = move4

      assertEquals(expected, actual)
    }
  }

  @Test(expected = classOf[NoSuchElementException])
  def testLastMoveColorOnHistoryWithoutMoveOfThisColor() {
    val move = Moves.getMove(Blue, PIECE1.head)
    MoveHistory().play(move).last(Green)
  }

  @Test
  def testLastMoveHavingColor() {
    {
      val move1 = Moves.getMove(Blue, PIECE1.head)
      val actual = MoveHistory().play(move1).last(Blue)
      val expected = move1
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Blue, PIECE1.head)
      val move2 = Moves.getMove(Green, PIECE1.head)
      val actual = MoveHistory()
        .play(move1)
        .play(move2)
        .last(Blue)
      val expected = move1
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Blue, PIECE1.head)
      val move2 = Moves.getMove(Blue, PIECE2.head)
      val move3 = Moves.getMove(Green, PIECE1.head)
      val actual = MoveHistory()
        .play(move1)
        .play(move2)
        .play(move3)
        .last(Blue)
      val expected = move2
      assertEquals(expected, actual)
    }
    {
      val move1 = Moves.getMove(Green, PIECE1.head)
      val move2 = Moves.getMove(Blue, PIECE1.head)
      val move3 = Moves.getMove(Blue, PIECE2.head)
      val move4 = Moves.getMove(Blue, PIECE3.head)
      val actual = MoveHistory()
        .play(move1)
        .play(move2)
        .play(move3)
        .play(move4)
        .last(Blue)
      val expected = move4
      assertEquals(expected, actual)
    }
  }

  @Test
  def testToString() {
    val move = Moves.getMove(Blue, PIECE1.head)
    val actual = MoveHistory().play(move).toString
    val expected = "Stack(Move{color=Blue, piece=PieceComposite{id=1, positions=(0, 0)}})"
    assertEquals(expected, actual)
  }

}