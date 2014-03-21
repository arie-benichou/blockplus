package games.go

import components.Positions.Position

sealed case class CellData(connexions: Set[Position], id: Int, freedom: Int) {

}