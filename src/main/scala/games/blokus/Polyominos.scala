package games.blokus

import components.Cells
import components.Positions
import components.Positions._
import scala.collection.immutable.SortedSet
import scala.collection.mutable.ListBuffer
import games.blokus.Polyominos.Instances._

object Polyominos {

  private object Rendering {

    private def dimension(positions: Set[Position]) = {
      val (maxRowIndex, maxColumnIndex) = positions.foldLeft((0, 0))((t, p) => (Math.max(t._1, p.row), Math.max(t._2, p.column)))
      (maxRowIndex + 1, maxColumnIndex + 1)
    }

    private def renderCell(array: Array[Array[Char]], position: Position, value: Char)(offset: (Int, Int)) =
      array(position.row + offset._1)(position.column + offset._2) = value

    private def render(
      positions: Set[Position], shadows: Set[Position], lights: Set[Position], connectors: Set[Position])(padding: (Int, Int))(blank: Char) = {
      val (rows, columns) = dimension(positions)
      val (rowOffset, columnOffset) = padding
      val (maxRow, maxColumn) = (rows + 2 * rowOffset, columns + 2 * columnOffset)
      val array = Array.ofDim[Char](maxRow, maxColumn)
      for (row <- 0 until maxRow) for (column <- 0 until maxColumn) array(row)(column) = blank
      positions.foreach(position => renderCell(array, position, 'O')(padding))
      shadows.foreach(renderCell(array, _, '•')(padding))
      lights.foreach(renderCell(array, _, '¤')(padding))
      connectors.foreach(renderCell(array, _, '0')(padding))
      array
    }

    private def toString(data: Array[Array[Char]]) =
      (0 until data.length).foldLeft("")((rendering, row) => rendering + "\n" + new String(data(row))).tail

    def apply(polyomino: Polyomino) = toString(render(
      polyomino.instances.head.positions,
      polyomino.instances.head.shadows,
      polyomino.instances.head.lights,
      polyomino.instances.head.connectors
    )(1, 1)('.'))

    def apply(instance: NormalizedInstance) = toString(render(
      instance.positions, Set.empty, Set.empty, Set.empty
    )(0, 0)('.'))

  }

  object Instances {

    private def rotate90(position: Position) = Position(-position.column, position.row)
    private def rotate90(positions: Set[Position]): Set[Position] = positions.map(rotate90(_))

    private def mirror(position: Position): Position = Position(position.row, -position.column)
    private def mirror(positions: Set[Position]): Set[Position] = positions.map(mirror(_))

    private def normalize(instance: Instance): NormalizedInstance = {
      if (instance.isNormalized) {
        NormalizedInstance(instance.selfType,
          instance.positions,
          instance.shadows,
          instance.lights,
          instance.connectors
        )
      }
      else {
        val vector = Positions.Origin - instance.topLeftCorner
        NormalizedInstance(instance.selfType,
          Positions.translateBy(vector, instance.positions),
          Positions.translateBy(vector, instance.shadows),
          Positions.translateBy(vector, instance.lights),
          Positions.translateBy(vector, instance.connectors)
        )
      }
    }

    private def computeDistinctRotations(
      instance: Instance, buffer: ListBuffer[NormalizedInstance], instances: Set[NormalizedInstance]): Set[NormalizedInstance] = {
      val normalizedInstance = instance.normalized
      if (instances.contains(normalizedInstance)) instances
      else computeDistinctRotations(instance.rotate, buffer += normalizedInstance, instances + normalizedInstance)
    }

    def apply(selfType: Polyomino, cells: Cells[Char]): List[NormalizedInstance] = {
      val positions: Set[Position] = cells.definedPositions
      val sides = positions.flatMap(_.*(Directions.Sides))
      val corners = positions.flatMap(_.*(Directions.Corners))
      val shadows = sides.diff(positions)
      val lights = corners.diff(sides)
      val connectors = positions.intersect(lights.flatMap(p => Directions.Corners.map(p + _)))
      val head = Instance(selfType, positions, shadows, lights, connectors)
      val buffer = ListBuffer[NormalizedInstance]()
      computeDistinctRotations(head.flip, buffer, computeDistinctRotations(head, buffer, Set()))
      buffer.toList
    }

    sealed case class Instance(selfType: Polyomino, positions: Set[Position], shadows: Set[Position], lights: Set[Position], connectors: Set[Position]) {
      lazy val topLeftCorner: Position = Positions.topLeftCorner(positions)
      lazy val isNormalized: Boolean = (topLeftCorner == Positions.Origin)
      lazy val normalized = Instances.normalize(this)
      lazy val rendering = Rendering(this.normalized)
      def flip: Instance = Instance(selfType, mirror(positions), mirror(shadows), mirror(lights), mirror(connectors))
      def rotate: Instance = Instance(selfType, rotate90(positions), rotate90(shadows), rotate90(lights), rotate90(connectors))
      override def toString = rendering
    }

    sealed case class NormalizedInstance(selfType: Polyomino, positions: Set[Position], shadows: Set[Position], lights: Set[Position], connectors: Set[Position]) {
      lazy val rendering = Rendering(this)
      def translateBy(vector: (Int, Int)): Instance =
        Instance(
          selfType,
          positions.map(_ + vector),
          shadows.map(_ + vector),
          lights.map(_ + vector),
          connectors.map(_ + vector)
        )
      override def toString = rendering
    }

  }

  private object Polyomino {
    def apply(data: Array[String]): Cells[Char] = {
      val rows = data.length
      val columns = if (rows == 0) 0 else data(0).length
      val cells = Cells(rows, columns, ' ', '?', Map.empty[Position, Char])
      val mutations = Map[Position, Char]() ++ (for {
        row <- 0 until rows
        column <- 0 until columns
      } yield (Position(row, column), data(row).charAt(column)))
      cells.apply(mutations)
    }
  }

  sealed trait Polyomino { self =>
    val data: Array[String]
    private lazy val cells = Polyomino(data)
    lazy val order: Int = cells.definedPositions.size
    lazy val instances: List[NormalizedInstance] = Instances(self, cells)
    private lazy val rendering = Rendering(self)
    override def toString = rendering
  }

  case object _0 extends Polyomino {
    val data = Array(
      " "
    )
  }
  case object _1 extends Polyomino {
    val data = Array(
      "O"
    )
  }
  case object _2 extends Polyomino {
    val data = Array(
      "OO"
    )
  }
  case object _3 extends Polyomino {
    val data = Array(
      "OOO"
    )
  }
  case object _4 extends Polyomino {
    val data = Array(
      "OO",
      "O "
    )
  }
  case object _5 extends Polyomino {
    val data = Array(
      "OOOO"
    )
  }
  case object _6 extends Polyomino {
    val data = Array(
      "OOO",
      "O   "
    )
  }
  case object _7 extends Polyomino {
    val data = Array(
      "OOO",
      " O "
    )
  }
  case object _8 extends Polyomino {
    val data = Array(
      "OO",
      "OO"
    )
  }
  case object _9 extends Polyomino {
    val data = Array(
      "OO ",
      " OO"
    )
  }
  case object _10 extends Polyomino {
    val data = Array(
      "OOOO",
      "O   "
    )
  }
  case object _11 extends Polyomino {
    val data = Array(
      "OOOOO"
    )
  }
  case object _12 extends Polyomino {
    val data = Array(
      "OO  ",
      " OOO"
    )
  }
  case object _13 extends Polyomino {
    val data = Array(
      "OOO",
      " OO"
    )
  }
  case object _14 extends Polyomino {
    val data = Array(
      "O O",
      "OOO"
    )
  }
  case object _15 extends Polyomino {
    val data = Array(
      " O  ",
      "OOOO"
    )
  }
  case object _16 extends Polyomino {
    val data = Array(
      "O  ",
      "OOO",
      "O  "
    )
  }
  case object _17 extends Polyomino {
    val data = Array(
      "OOO",
      "O  ",
      "O  "
    )
  }
  case object _18 extends Polyomino {
    val data = Array(
      "OO ",
      " OO",
      "  O"
    )
  }
  case object _19 extends Polyomino {
    val data = Array(
      "O  ",
      "OOO",
      "  O"
    )
  }
  case object _20 extends Polyomino {
    val data = Array(
      "O  ",
      "OOO",
      " O "
    )
  }
  case object _21 extends Polyomino {
    val data = Array(
      " O ",
      "OOO",
      " O "
    )
  }

  val values = List(
    Polyominos._0,
    Polyominos._1, Polyominos._2, Polyominos._3,
    Polyominos._4, Polyominos._5, Polyominos._6,
    Polyominos._7, Polyominos._8, Polyominos._9,
    Polyominos._10, Polyominos._11, Polyominos._12,
    Polyominos._13, Polyominos._14, Polyominos._15,
    Polyominos._16, Polyominos._17, Polyominos._18,
    Polyominos._19, Polyominos._20, Polyominos._21
  )

  def main(args: Array[String]) {
    Polyominos.values.foreach {
      p =>
        println("-------------8<-------------")
        println(p)
      //p.instances.foreach(i => println("\n" + i))
    }
  }

}