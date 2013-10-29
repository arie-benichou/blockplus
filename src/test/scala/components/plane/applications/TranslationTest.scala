package components.plane.applications

import org.junit.Assert._
import org.junit.Test

import components.plane.elements.Point
import components.plane.elements.Point.pairToPoint

class TranslationTest {

  @Test
  def testTranslatePoint() {
    {
      val actual = Translation.translate(Point(0, 1))((0, 0))
      val expected = Point(0, 1)
      assertEquals(expected, actual)
    }
    {
      val actual = Translation.translate(Point(0, 1))((1, 0))
      val expected = Point(1, 1)
      assertEquals(expected, actual)
    }
    {
      val actual = Translation.translate(Point(0, 1))((0, 1))
      val expected = Point(0, 2)
      assertEquals(expected, actual)
    }
  }

  @Test
  def testTranslatePoints() {
    {
      val actual = Translation.translate(List(Point(0, 1)))((0, 0))
      val expected = List(Point(0, 1))
      assertEquals(expected, actual)
    }
    {
      val actual = Translation.translate(List(Point(0, 1)))((1, 0))
      val expected = List(Point(1, 1))
      assertEquals(expected, actual)
    }
    {
      val actual = Translation.translate(List(Point(0, 1)))((0, 1))
      val expected = List(Point(0, 2))
      assertEquals(expected, actual)
    }
    {
      val actual = Translation.translate(List(Point(0, 1), Point(1, 0)))((1, 2))
      val expected = List(Point(1, 3), Point(2, 2))
      assertEquals(expected, actual)
    }
  }

}