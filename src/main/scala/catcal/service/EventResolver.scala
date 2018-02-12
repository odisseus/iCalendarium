package catcal.service

import catcal.domain.Errors.{ Error, ResolverError }
import catcal.domain.{ Event, FixedDay, Movable, ResolvedEvent }

class EventResolver(
    dateCalculator: DateCalculator,
    currentYear: Int,
    knownEvents: Iterable[Event]) {

  //FIXME make this method private
  //FIXME rewrite to tail recursion and use memoization
  def resolveEvent(event: Event): Either[Error, ResolvedEvent] = event.eventDate match {
    case FixedDay(monthDay) => try {
      Right(ResolvedEvent(
        date = monthDay.toLocalDate(currentYear),
        description = event.description
      ))
    } catch {
      case e: org.joda.time.IllegalFieldValueException => Left(ResolverError(e.getMessage))
    }
    case Movable(ordinal, weekday, reference) => for {
      referencedEvent <- find(reference)
      resolvedReference <- resolveEvent(referencedEvent)
      date <- dateCalculator.calculateDate(ordinal, weekday, resolvedReference.date)
    } yield ResolvedEvent(
      date = date,
      description = event.description
    )
  }

  def resolveAll(): Iterable[ResolvedEvent] = {
    //TODO Do not swallow errors
    knownEvents.map(resolveEvent).flatMap(_.toOption)
  }

  private def find(reference: String): Either[Error, Event] = {
    knownEvents.find(_.description.contains(reference)).toRight(left = ResolverError(reference))
  }

}