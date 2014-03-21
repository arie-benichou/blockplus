package components.transitions

import components.transitions.Transition._

object MyComplexTypeReduction {

  def apply(transitions: Iterable[Transition[MyComplexType]]) = {
    val insertionsById = transitions.filter(_.left.isEmpty).groupBy(_.right.get.externalId).withDefaultValue(Set(NullTransition))
    val deletionsById = transitions.filter(_.right.isEmpty).groupBy(_.left.get.externalId).withDefaultValue(Set(NullTransition))
    val retentionsById = transitions.filter(t => { t.left.isDefined & t.right.isDefined }).groupBy(_.right.get.externalId)
    val alterationsId = insertionsById.keySet.intersect(deletionsById.keySet)
    val effectiveInsertions = insertionsById.filterNot(e => alterationsId.contains(e._1)).values.flatten
    val effectiveDeletions = deletionsById.filterNot(e => alterationsId.contains(e._1)).values.flatten
    val effectiveRetentions = retentionsById.values.flatten
    val effectiveAlterations = alterationsId.map(alterationId => {
      val left = deletionsById(alterationId).iterator.next.left.get
      val right = insertionsById(alterationId).iterator.next.right.get
      Transition.Alteration(left, right)
    })
    Iterable.concat(effectiveInsertions, effectiveDeletions, effectiveRetentions, effectiveAlterations)
  }

}