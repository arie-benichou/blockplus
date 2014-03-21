package components.transitions

import components.transitions.Transition._
import components.Positions._

object Main2 {

  def main(args: Array[String]) {

    val left = Set(
      (Position(0, 0), games.blokus.Polyominos._1, Set(Position(0, 0))),
      (Position(0, 1), games.blokus.Polyominos._1, Set(Position(0, 1))),
      (Position(1, 0), games.blokus.Polyominos._1, Set(Position(1, 0))))

    val right = Set(
      (Position(1, 2), games.blokus.Polyominos._1, Set(Position(1, 0), Position(0, 1))),
      (Position(1, 1), games.blokus.Polyominos._1, Set(Position(0, 0), Position(0, 1))),
      (Position(0, 0), games.blokus.Polyominos._1, Set(Position(0, 0))))

    val transition = Transition.from(left).to(right)
    val reduction1 = SetReduction(transition)

    val zip = left.zipWithIndex
    val previousState = zip.toMap.withDefaultValue(-1)

    for ((k, v) <- previousState) {
      println("ordinal : " + v)
      println("data    : " + k)
      println("--------------------------------------")
    }

    val newState = scala.collection.mutable.Map() ++ previousState

    reduction1.foreach(e => {
      // TODO use Scala Pattern Matching
      val t = e.getClass().getSimpleName();
      println(t)
      if (t == "Insertion") {
        println(e.right)
        // TODO ? avoir un offset pour gérer les ids des éléments à insérer
      } else if (t == "Deletion") {
        println(previousState(e.left.get))
      } else {
        println("(implicite)")
      }
      println
    })

    println("--------------------------------------")

    println("simulation côté serveur: ")

    println("--------------------------------------")
    val deletions = for {
      e <- reduction1 if (e.getClass().getSimpleName() == "Deletion")
    } yield previousState(e.left.get)
    println("deletions: " + deletions)

    //    val insertions = for {
    //      e <- reduction1 if (e.getClass().getSimpleName() == "Insertion")
    //    } yield e.right
    //    println("insertions: ") // + insertions)

    val insertions = for {
      e <- reduction1 if (e.getClass().getSimpleName() == "Insertion")
    } yield e.right
    print("insertions: ") // + insertions)

    val insertions2 = insertions.map(e => {
      val opt: (Position, games.blokus.Polyominos.Polyomino, Set[Position]) = e.get
      opt._3
    })

    val output = insertions2.map(_.map(p => "(" + p.row + "," + p.column + ")").mkString("|"))
    println(output)

    println("--------------------------------------")

    println("simulation côté client: ")

    println("--------------------------------------")

    // TODO ? "defragmenter" les index
    val offset = previousState.size
    var counter = 0

    reduction1.foreach(e => {
      val t = e.getClass().getSimpleName();
      if (t == "Insertion") {
        newState.put(e.right.get, offset + counter)
        counter = counter + 1
      } else if (t == "Deletion") {
        newState.remove(e.left.get)
      } else {
      }
    })

    for ((k, v) <- newState) {
      println("ordinal : " + v)
      println("data    : " + k)
      println("--------------------------------------")
    }

  }

}