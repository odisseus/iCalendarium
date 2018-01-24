package catcal.service

import biweekly.ICalendar
import biweekly.component.VEvent
import catcal.domain.ResolvedEvent

class EventExporter {

  private def toVEvent(event: ResolvedEvent): VEvent = {
    val vevent = new VEvent()
    vevent.setSummary(event.description)
    vevent.setDateStart(event.date.toDate, false)
    vevent
  }

  def toICalendar(events: Traversable[ResolvedEvent]): ICalendar = {
    val icalendar = new ICalendar()
    events.map(toVEvent).foreach(icalendar.addEvent)
    icalendar
  }

}
