package components.plane.elements

import org.junit.Assert._
import org.junit.Test

import components.plane.elements.Point.pairToPoint

// TODO test iterator
class RotatablePointsTest {

  @Test
  def testCompanionObject() {
    assertEquals(RotatablePoints(), RotatablePoints(Points.empty)(Point.origin))
    assertEquals(RotatablePoints(), RotatablePoints(Points.empty))
    assertEquals(RotatablePoints(), RotatablePoints())
    assertEquals(RotatablePoints(Point(0, 1), Point(1, 0)), RotatablePoints((0, 1), (1, 0)))
  }

  @Test
  def testToString() {
    {
      val expected = "RotatablePoints{referential=(0,0), points={}}"
      val actual = RotatablePoints().toString
      assertEquals(expected, actual)
    }
    {
      val expected = "RotatablePoints{referential=(0,0), points={(1,2)}}"
      val actual = RotatablePoints(Points((1, 2))).toString
      assertEquals(expected, actual)
    }
  }

  @Test
  def testhashCode() {
    {
      val expected = 2026751562.toString
      val actual = RotatablePoints().hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = 1271941395.toString
      val actual = RotatablePoints((0, 0)).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = -1618707262.toString
      val actual = RotatablePoints((0, 1), (1, 0)).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = -1618707262.toString
      val actual = RotatablePoints((1, 0), (0, 1)).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = -1618707262.toString
      val actual = RotatablePoints((1, 0), (0, 1))(0, 0).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = 1560664708.toString
      val actual = RotatablePoints((1, 0), (0, 1))(1, 1).hashCode.toString
      assertEquals(expected, actual)
    }
  }

  @Test
  def testEquals() {
    assertTrue(RotatablePoints((0, 1), (1, 0))(0, 0) == RotatablePoints((0, 1), (1, 0))(0, 0))
    assertTrue(RotatablePoints((0, 1), (1, 0))(0, 0) == RotatablePoints((1, 0), (0, 1))(0, 0))
    assertTrue(RotatablePoints((0, 1), (1, 0))(0, 0) == RotatablePoints((1, 0), (0, 1)))
    assertTrue(RotatablePoints((0, 1), (1, 0))(0, 0) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 1), (1, 0))(1, 0) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 1), (1, 0))(0, 1) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 1), (1, 0))(1, 1) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 0), (1, 0))(0, 0) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((1, 1), (1, 0))(0, 0) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 1), (0, 1))(0, 0) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 1), (0, 0))(0, 0) == RotatablePoints((0, 1), (1, 0)))
    assertFalse(RotatablePoints((0, 1), (1, 1))(0, 0) == RotatablePoints((0, 1), (1, 0)))
  }

}