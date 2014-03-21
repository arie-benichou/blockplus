package components.transitions

import components.transitions.Transition._

object Main1 {

  def main(args: Array[String]) {

    val left = Set(
      MyComplexType(1, Map("value1" -> "some value")),
      MyComplexType(2, Map("value1" -> "another value")),
      MyComplexType(3, Map("value1" -> "some value")),
      MyComplexType(4, Map("value1" -> "some value")))

    val right = Set(
      MyComplexType(1, Map("value1" -> "some other value")),
      MyComplexType(2, Map("value1" -> "yet another value")),
      MyComplexType(3, Map("value1" -> "some value")),
      MyComplexType(5, Map("value1" -> "some value")))

    val transition = Transition.from(left).to(right)
    val reduction1 = SetReduction(transition)
    reduction1.foreach(println)

    println();

    val reduction2 = MyComplexTypeReduction(reduction1);
    reduction2.foreach(println)
  }

}