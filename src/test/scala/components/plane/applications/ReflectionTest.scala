package components.plane.applications

import org.junit.Assert._
import org.junit.Test

import components.plane.elements.Point
import components.plane.elements.Point.pairToPoint

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
      val actual = Reflection.reflectX(List(Point(0, 0)))(0)
      val expected = List(Point(0, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(List(Point(1, 0)))(0)
      val expected = List(Point(-1, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(List(Point(2, 0)))(0)
      val expected = List(Point(-2, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(List(Point(2, 0)))(1)
      val expected = List(Point(0, 0))
      assertEquals(expected, actual)
    }
    {
      val actual = Reflection.reflectX(List(Point(0, 1), Point(1, 0)))(2)
      val expected = List(Point(4, 1), Point(3, 0))
      assertEquals(expected, actual)
    }
  }

}