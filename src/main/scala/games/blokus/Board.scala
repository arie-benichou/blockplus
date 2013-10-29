package games.blokus

import components.Cells
import components.Positions.Position
import games.blokus.Board.Layer
import games.blokus.Board.Layer.State
import games.blokus.Game.Color
import games.blokus.Polyominos.Instances._

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

    def apply(rows: Int, columns: Int): Layer =
      Layer(Cells(rows, columns, State.Nirvana, State.Mudita, Map.empty[Position, State]))

  }

  sealed case class Layer private (cells: Cells[State]) {

    lazy val lights = cells.filter(_._2 == State.Metta)
    lazy val selves = cells.filter(_._2 == State.Upekkha)

    def isMutable(position: Position): Boolean = cells.get(position) match {
      case State.Nirvana => true
      case State.Metta   => true
      case _             => false
    }

    def isMutable(positions: Iterable[Position]): Boolean =
      positions.forall(isMutable)

    def apply(mutation: Map[Position, State]) =
      new Layer(cells.apply(mutation.filter(e => isMutable(e._1))))

  }

  def apply(rows: Int, columns: Int) = {

    val data = List(
      (Color.Blue, Position(0, 0)),
      (Color.Yellow, Position(0, columns - 1)),
      (Color.Red, Position(rows - 1, columns - 1)),
      (Color.Green, Position(rows - 1, 0))
    )

    val layers = data.foldLeft(Map[Color, Layer]())((layers, tuple) => {
      val (color, position) = tuple
      layers + ((color, Layer(rows, columns).apply(Map(position -> State.Metta))))
    })

    new Board(rows, columns, layers);

  }

}

import Board._
sealed case class Board private (rows: Int, columns: Int, layers: Map[Color, Layer]) {

  def isMutable(color: Color, position: Position): Boolean =
    layers.get(color).get.isMutable(position)

  def isMutable(color: Color, positions: Iterable[Position]): Boolean =
    layers.get(color).get.isMutable(positions)

  def lights(color: Color) = layers.get(color).get.lights

  def selves(color: Color) = layers.get(color).get.selves

  def apply(color: Color, positions: Set[Position], shadows: Set[Position], lights: Set[Position]): Board = {
    val selfMutation = EmptyMutation ++ positions.map((_, State.Upekkha)) ++ shadows.map((_, State.Karuna)) ++ lights.map((_, State.Metta))
    val otherMutation = EmptyMutation ++ positions.map((_, State.Mudita))
    val newLayers = layers.keys.foldLeft(Map[Color, Layer]())((l, c) =>
      l + ((c, layers.get(c).get.apply(if (c == color) selfMutation else otherMutation)))
    )
    copy(layers = newLayers)
  }

}