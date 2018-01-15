package catcal

import catcal.domain.ResolvedEvent
import org.joda.time.LocalDate
import org.scalatest.{ FlatSpec, Matchers }

class EventExporterTest extends FlatSpec with Matchers {
  behavior of "EventExporter"

  val eventExporter = new EventExporter

  it should "export events" in {
    val events = Seq(
      ResolvedEvent(LocalDate.parse("2018-01-01"), "Новий Рік\nСв. мч. Боніфатія."),
      ResolvedEvent(LocalDate.parse("2017-11-04"), "День гранчака.")
    )
    val result = eventExporter.toICalendar(events)
    result.getEvents.size() shouldBe 2
  }

}
