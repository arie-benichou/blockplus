package components.plane.elements

import org.junit.Assert._
import org.junit.Test

// TODO test ordering
class PointTest {

  @Test
  def testCompanionObject() {
    assertEquals(Point.origin, Point(0, 0))
    assertNotEquals(Point.origin, Point(0, 1))
    assertNotEquals(Point.origin, Point(1, 0))
    assertNotEquals(Point.origin, Point(1, 1))
  }

  @Test
  def testToString() {
    val actual = Point(0, 1).toString
    val expected = "(0,1)"
    assertEquals(expected, actual)
  }

  @Test
  def testhashCode() {
    val actual = Point(0, 1).hashCode
    val expected = Point(0, 1).toString.hashCode
    assertEquals(expected, actual)
  }

  @Test
  def testEquals() {
    assertFalse(Point(1, 2) == Point(2, 1))
    assertTrue(Point(1, 2) == Point(1, 2))
  }

}