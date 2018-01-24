package catcal.service

import catcal.domain.{ Event, FixedDay, Movable, ResolvedEvent }
import org.joda.time.DateTimeConstants._
import org.joda.time.LocalDate
import org.scalatest.{ FlatSpec, Matchers }

class EventResolverTest extends FlatSpec with Matchers {
  behavior of "EventResolver"

  val events = Seq(
    Event(FixedDay("--01-01"), "Новий Рік\nСв. мч. Боніфатія."),
    Event(Movable(1, SATURDAY, "гужового"), "День гранчака."),
    Event(Movable(-9, FRIDAY, "Новий Рік"), "День працівника гужового транспорту.")
  )

  val eventResolver = new EventResolver(
    dateCalculator = new DateCalculator,
    currentYear = 2018,
    knownEvents = events
  )

  it should "resolve fixed day events" in {
    val result = eventResolver.resolveEvent(events.head)
    result shouldBe ResolvedEvent(LocalDate.parse("2018-01-01"), "Новий Рік\nСв. мч. Боніфатія.")
  }

  it should "resolve moveable events" in {
    val result = eventResolver.resolveEvent(events(2))
    result shouldBe ResolvedEvent(LocalDate.parse("2017-11-03"), "День працівника гужового транспорту.")
  }

  it should "resolve moveable events referencing other moveable events" in {
    val result = eventResolver.resolveEvent(events(1))
    result shouldBe ResolvedEvent(LocalDate.parse("2017-11-04"), "День гранчака.")
  }

}
