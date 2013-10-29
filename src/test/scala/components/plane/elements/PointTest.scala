package components.plane.elements

import org.junit.Assert._
import org.junit.Test
import org.junit.Ignore

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

  //TODO : à supprimer sauf si on veut vraiment forcer la valeur du hash
  @Ignore
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