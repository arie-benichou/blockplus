package blockplus

import org.junit._
import org.junit.Assert._

class MyScalaClassTest {

  @Test
  def testEcho() {
    val given = "AlloPapaTangoCharly";
    val expected = given;
    val actual = new MyScalaClass().echo(given);
    assertEquals(expected, actual);
  }

}