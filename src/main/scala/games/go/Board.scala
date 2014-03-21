package games.go

import components.Cells
import components.Positions.Position
import games.go.Board.Layer
import games.go.Game.Color

object Board {

  private val EmptyMutation = Map.empty[Position, CellData]

  sealed case class Layer private (cells: Cells[CellData]) {

    //    lazy val lights = cells.filter(_._2 == State.Metta)
    //    lazy val selves = cells.filter(_._2 == State.Upekkha)
    //
    //    def isLight(position: Position) = cells.get(position) == State.Metta
    //
    //    def isMutable(position: Position): Boolean = cells.get(position) match {
    //      case State.Nirvana => true
    //      case State.Metta   => true
    //      case _             => false
    //    }
    //
    //    def apply(mutation: Map[Position, State]) =
    //      new Layer(cells(mutation.filter(e => isMutable(e._1))))

  }

  def apply(rows: Int, columns: Int) = {

    //    val data = List(
    //      (Color.Blue, Position(0, 0)),
    //      (Color.Yellow, Position(0, columns - 1)),
    //      (Color.Red, Position(rows - 1, columns - 1)),
    //      (Color.Green, Position(rows - 1, 0))
    //    )
    //
    //    val layers = data.foldLeft(Map[Color, Layer]())((layers, tuple) => {
    //      val (color, position) = tuple
    //      layers + ((color, Layer(rows, columns).apply(Map(position -> State.Metta))))
    //    })
    //
    //    new Board(rows, columns, layers);

  }

}

import Board._
sealed case class Board private (rows: Int, columns: Int, layers: Map[Color, Layer]) {

  //  def isMutable(color: Color, position: Position): Boolean =
  //    layers.get(color).get.isMutable(position)
  //
  //  def isMutable(color: Color, positions: Iterable[Position]): Boolean =
  //    layers.get(color).get.isMutable(positions)
  //
  //  def lights(color: Color) = layers.get(color).get.lights
  //
  //  def selves(color: Color) = layers.get(color).get.selves
  //
  //  def isLight(color: Color, position: Position) = layers(color).isLight(position)
  //
  //  def apply(color: Color, positions: Set[Position], shadows: Set[Position], lights: Set[Position]): Board = {
  //    val selfMutation = EmptyMutation ++ positions.map((_, State.Upekkha)) ++ shadows.map((_, State.Karuna)) ++ lights.map((_, State.Metta))
  //    val otherMutation = EmptyMutation ++ positions.map((_, State.Mudita))
  //    val newLayers = layers.keys.foldLeft(Map[Color, Layer]())((l, c) =>
  //      l + ((c, layers.get(c).get.apply(if (c == color) selfMutation else otherMutation)))
  //    )
  //    copy(layers = newLayers)
  //  }

}