package games.blokus

import components.Cells
import components.Positions._

import games.blokus.Game.Color
import games.blokus.Polyominos.Instances._

import Board._
import Board.Layer._
import Board.Layer.State._

object Board {

  private val EmptyMutation = Map.empty[Position, State]

  object Layer {

    sealed trait State

    object State {

      /**
       * State for a cell that could contain this layer's color
       */
      case object Metta extends State

      /**
       * State for a cell that can not contain this layer's color
       */
      case object Karuna extends State

      /**
       * State for a cell that contains a different layer's color
       */
      case object Mudita extends State

      /**
       * State for a cell that contains this layer's color
       */
      case object Upekkha extends State

      /**
       * State for a stateless cell
       */
      case object Nirvana extends State
    }

    def apply(data: Map[Position, State]): Layer = Layer(Cells(data, Nirvana, Mudita))

    def apply(rows: Int, columns: Int): Layer = Layer((for (r <- 0 until rows; c <- 0 until columns) yield (Position(r, c), Nirvana)).toMap)

  }

  sealed case class Layer private (cells: Cells[State]) {
    lazy val lights = cells.filterOthers(_._2 == Metta)
    lazy val selves = cells.filterOthers(_._2 == Upekkha)
    def isLight(position: Position) = cells.get(position) == Metta
    def isMutable(position: Position): Boolean = cells.get(position) match {
      case Nirvana => true
      case Metta   => true
      case _       => false
    }
    def isMutable(positions: Iterable[Position]): Boolean = positions.forall(isMutable)
    def apply(mutation: Map[Position, State]) = Layer(cells(mutation.filter(e => isMutable(e._1))))
  }

  def apply(rows: Int, columns: Int): Board = {
    val emptyLayer = Layer(rows, columns)
    val data = List(
      (Color.Blue, Position(0, 0)),
      (Color.Yellow, Position(0, columns - 1)),
      (Color.Red, Position(rows - 1, columns - 1)),
      (Color.Green, Position(rows - 1, 0))
    )
    val layers = data.foldLeft(Map[Color, Layer]()) { (map, tuple) =>
      val (color, position) = tuple
      map + ((color, emptyLayer(Map(position -> Metta))))
    }
    Board(rows, columns, layers)
  }

}

sealed case class Board private (rows: Int, columns: Int, layers: Map[Color, Layer]) {
  def isMutable(color: Color, position: Position): Boolean = layers.get(color).get.isMutable(position)
  def isMutable(color: Color, positions: Iterable[Position]): Boolean = layers.get(color).get.isMutable(positions)
  def lights(color: Color) = layers.get(color).get.lights
  def selves(color: Color) = layers.get(color).get.selves
  def isLight(color: Color, position: Position) = layers(color).isLight(position)
  def apply(color: Color, positions: Set[Position], shadows: Set[Position], lights: Set[Position]): Board = {
    val selfMutation = EmptyMutation ++ positions.map((_, Upekkha)) ++ shadows.map((_, Karuna)) ++ lights.map((_, Metta))
    val otherMutation = EmptyMutation ++ positions.map((_, Mudita))
    val newLayers = layers.keys.foldLeft(Map[Color, Layer]()) { (l, c) =>
      l + ((c, layers.get(c).get.apply(if (c == color) selfMutation else otherMutation)))
    }
    copy(layers = newLayers)
  }
}