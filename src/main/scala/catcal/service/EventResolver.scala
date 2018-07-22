package catcal.service

import catcal.domain.Errors.{ Error, ResolverError }
import catcal.domain._
import org.joda.time.LocalDate

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
      referencedDate <- resolveDate(reference)
      date <- dateCalculator.calculateDate(ordinal, weekday, referencedDate)
    } yield ResolvedEvent(
      date = date,
      description = event.description
    )
  }

  def resolveAll(): (Iterable[ResolvedEvent], Iterable[Error]) = {
    val resolutionResults = knownEvents.map(resolveEvent).groupBy(_.isRight)
    val good = resolutionResults.get(true).getOrElse(Seq.empty).map(_.right.get)
    val bad = resolutionResults.get(false).getOrElse(Seq.empty).map(_.left.get)
    (good, bad)
  }

  private def resolveDate(reference: Reference): Either[Error, LocalDate] = {
    reference match {
      case EventReference(eventName) =>
        for {
          referencedEvent <- find(eventName)
          resolvedReference <- resolveEvent(referencedEvent)
        } yield resolvedReference.date
      case FixedDayReference(monthDay) => Right(monthDay.toLocalDate(currentYear))
    }
  }

  private def find(eventName: String): Either[Error, Event] = {
    knownEvents.find(_.description.contains(eventName)).toRight(left = ResolverError(eventName))
  }

}