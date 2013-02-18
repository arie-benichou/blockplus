package components.plane.applications

import org.junit.Assert._
import org.junit.Test

import components.plane.elements.Point
import components.plane.elements.Point.pairToPoint
import components.plane.elements.Points

class ReflectionTest {

  @Test
  def testReflectXPoint() {
    {
      val actual = Reflection.reflectX(Point(0, 0))(0)
      val expected = Point(0, 0)
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Point(1, 0))(0)
      val expected = Point(-1, 0)
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Point(2, 0))(0)
      val expected = Point(-2, 0)
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Point(2, 0))(1)
      val expected = Point(0, 0)
      assertEquals(expected, actual)
    }
  }

  @Test
  def testReflectXPoints() {
    {
      val actual = Reflection.reflectX(Points((0, 0)))(0)
      val expected = Points((0, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Points((1, 0)))(0)
      val expected = Points((-1, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Points((2, 0)))(0)
      val expected = Points((-2, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Points((2, 0)))(1)
      val expected = Points((0, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(Points((0, 1), (1, 0)))(2)
      val expected = Points((3, 0), (4, 1))
      assertEquals(expected, actual)
    }
  }

}