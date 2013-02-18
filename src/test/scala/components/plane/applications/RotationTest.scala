package components.plane.applications

import org.junit.Assert._
import org.junit.Test
import components.plane.elements.Point
import components.plane.elements.Points
import components.plane.elements.RotatablePoints

class RotationTest {

  @Test
  def testRotate45Point() {
    {
      val actual = Rotation.rotate45(Point(0, 1))
      val expected = Point(1, 0)
      assertEquals(expected, actual)
    }
    {
      val actual = Rotation.rotate45(Point(6, 5))
      val expected = Point(5, -6)
      assertEquals(expected, actual)
    }
    {
      val point = Point(6, 5);
      val referential = Point(5, 5)
      val actual = Rotation.rotate45(point)(referential)
      val expected = Point(5, 4)
      assertEquals(expected, actual)
    }
  }

  @Test
  def testRotate45PointWithReferential() {
    val point = Point(6, 3);
    val referential = Point(5, 4)
    val actual = Rotation.rotate45(point)(referential)
    val expected = Point(4, 3)
    assertEquals(expected, actual)
  }

  @Test
  def testRotate45PointsWithReferential() {
    {
      val referential = Point(5, 4)
      val points = Points((6, 3));
      val actual = Rotation.rotate45(points)(referential)
      val expected = Points((4, 3))
      assertEquals(expected, actual)
    }
    {
      val referential = Point(5, 4)
      val points = Points((4, 2), (6, 3));
      val actual = Rotation.rotate45(points)(referential)
      val expected = Points((3, 5), (4, 3))
      assertEquals(expected, actual)
    }

  }

}