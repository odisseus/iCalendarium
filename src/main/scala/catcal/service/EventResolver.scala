package catcal.service

import catcal.domain.{ Event, FixedDay, Movable, ResolvedEvent }

class EventResolver(
    dateCalculator: DateCalculator,
    currentYear: Int,
    knownEvents: Iterable[Event]) {

  //FIXME rewrite to tail recursion and use memoization
  def resolveEvent(event: Event): ResolvedEvent = event.eventDate match {
    case FixedDay(monthDay) => ResolvedEvent(
      date = monthDay.toLocalDate(currentYear),
      description = event.description
    )
    case Movable(ordinal, weekday, reference) => {
      val referencedEvent = find(reference)
      val resolvedReference = resolveEvent(referencedEvent)
      val date = dateCalculator.calculateDate(ordinal, weekday, resolvedReference.date)
      ResolvedEvent(
        date = date,
        description = event.description
      )
    }
  }

  private def find(reference: String) = {
    knownEvents.find(_.description.contains(reference))
      .getOrElse(throw new IllegalStateException(s"Bad reference: $reference"))
  }

}
