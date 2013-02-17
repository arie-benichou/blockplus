package components.plane

import org.junit.Assert._
import org.junit.Test
import scala.collection.immutable.TreeSet

class PointsTest {

  @Test
  def testCompanionObject() {
    assertSame(Points(), Points.empty)
    assertNotSame(Points(Point(0, 0)), Points.empty)
    assertNotSame(Points((0, 0)), Points.empty)
  }

  @Test
  def testToString() {
    {
      val expected = "{(0,0)}"
      val actual = Points(Point()).toString
      assertEquals(expected, actual)
    }
    {
      val expected = "{(0,0),(0,1)}"
      val actual = Points(Point(), Point(0, 1)).toString
      assertEquals(expected, actual)
    }
    {
      val expected = "{(0,0),(0,1)}"
      val actual = Points(Point(0, 1), Point()).toString
      assertEquals(expected, actual)
    }
    {
      val expected = "{(0,1),(1,0)}"
      val actual = Points(Point(1, 0), Point(0, 1)).toString
      assertEquals(expected, actual)
    }
    {
      val expected = "{(1,0),(2,1)}"
      val actual = Points(Point(2, 1), Point(1, 0)).toString
      assertEquals(expected, actual)
    }
  }

  @Test
  def testhashCode() {
    {
      val expected = 835491922.toString
      val actual = Points().hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = 1373063695.toString
      val actual = Points(Point()).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = -506270346.toString
      val actual = Points(Point(0, 1)).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = 1463071986.toString
      val actual = Points(Point(0, 1), Point(1, 0)).hashCode.toString
      assertEquals(expected, actual)
    }
    {
      val expected = 1463071986.toString
      val actual = Points(Point(1, 0), Point(0, 1)).hashCode.toString
      assertEquals(expected, actual)
    }
  }

  @Test
  def testEquals() {
    assertFalse(Points((1, 2)) == Points((2, 1)))
    assertTrue(Points((1, 2)) == Points((1, 2)))
    assertTrue(Points((1, 2), (3, 4)) == Points((3, 4), (1, 2)))
  }

}