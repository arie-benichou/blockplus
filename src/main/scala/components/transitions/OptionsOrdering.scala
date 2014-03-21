package components.transitions

import components.transitions.Transition._
//import games.blokus.Polyominos._
import components.Positions._

object OptionsOrdering {

  def main(args: Array[String]) {

    val options1 = Set(
      (Position(0, 0), games.blokus.Polyominos._1, Set(Position(0, 0))),
      (Position(0, 1), games.blokus.Polyominos._1, Set(Position(0, 1))),
      (Position(1, 0), games.blokus.Polyominos._1, Set(Position(1, 0))))
    val zip1 = options1.zipWithIndex
    //      zip.foreach(e => {
    //        println(e._2)
    //      })
    val map1 = zip1.toMap.withDefaultValue(-1)
    //    for ((k, v) <- map1) {
    //      println(v)
    //      println(k)
    //      println
    //    }
    println

    val options2 = Set(
      (Position(1, 1), games.blokus.Polyominos._1, Set(Position(0, 0))),
      (Position(0, 0), games.blokus.Polyominos._1, Set(Position(0, 0))))
    val zip2 = options2.zipWithIndex
    zip2.foreach(e => {
      println(e._2 + " : " + map1(e._1))
    })

    // TODO à partir d'ici: utiliser le composant de transition/reduction
    // TODO poc console sur plusieurs parties pour voir les gains en taille de données

  }

}