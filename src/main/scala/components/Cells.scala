package components

import Cells._

import scala.collection.immutable.SortedSet

import components.Positions.Ordering
import components.Positions.Position

object Cells {

  type Predicate[A] = ((Position, A)) => Boolean
  type Positions = Set[Position]

  def update[A](input: Map[Position, A], default: A, undefined: A, defaults: Map[Position, A], explicitelyUndefined: Map[Position, A], others: Map[Position, A]) = {
    val (updateForDefaults, updateForNotDefaults) = input.partition(_._2 == default)
    val (updateForExplicitelyUndefined, updateForOthers) = updateForNotDefaults.partition(_._2 == undefined)
    (
      defaults.filterNot(e => updateForNotDefaults.isDefinedAt(e._1)) ++ updateForDefaults,
      explicitelyUndefined.filterNot(e => updateForDefaults.isDefinedAt(e._1) || updateForOthers.isDefinedAt(e._1)) ++ updateForExplicitelyUndefined,
      others.filterNot(e => updateForDefaults.isDefinedAt(e._1) || updateForExplicitelyUndefined.isDefinedAt(e._1)) ++ updateForOthers
    )
  }

  def apply[A](data: Map[Position, A], default: A, undefined: A): Cells[A] = {
    val (defaults, notDefaults) = data.partition(_._2 == default)
    val (explicitelyUndefined, others) = notDefaults.partition(_._2 == undefined)
    new Cells((defaults, explicitelyUndefined, others), default, undefined)
  }

}

sealed case class Cells[A] private (data: (Map[Position, A], Map[Position, A], Map[Position, A]), default: A, undefined: A) {
  private lazy val (defaults, explicitelyUndefined, others) = data
  lazy val positions: Positions = SortedSet() ++ (this.defaults.keySet ++ this.others.keySet)
  lazy val min: Position = this.positions.min
  lazy val max: Position = this.positions.max
  def get(position: Position): A = if (positions.contains(position)) this.others.getOrElse(position, this.default) else this.undefined
  def filterDefaults(p: Predicate[A]): Positions = this.defaults.filter(p).keySet
  def filterExplicitelyUndefined(p: Predicate[A]): Positions = this.explicitelyUndefined.filter(p).keySet
  def filterOthers(p: Predicate[A]): Positions = this.others.filter(p).keySet
  def filterDefined(p: Predicate[A]): Positions = this.filterDefaults(p) ++ this.filterOthers(p)
  def filter(p: Predicate[A]): Positions = this.filterDefined(p) ++ filterExplicitelyUndefined(p)
  def apply(input: Map[Position, A]): Cells[A] = copy(Cells.update(input, default, undefined, defaults, explicitelyUndefined, others))
}