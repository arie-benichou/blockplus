package games.go

object Game {

  sealed trait Color

  object Color {
    case object Black extends Color
    case object White extends Color
  }

}