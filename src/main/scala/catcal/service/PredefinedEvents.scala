package catcal.service

import catcal.domain.{ Event, EventsConfiguration, FixedDay }
import org.joda.time.MonthDay

class PredefinedEvents(
    eventsConfiguration: EventsConfiguration,
    currentYear: Int) {

  private val easter = Event(
    eventDate = FixedDay(new MonthDay(EasterDateProvider.getEasterDate(currentYear))),
    description = eventsConfiguration.easter
  )

  val events: Seq[Event] = Seq(easter)

}
