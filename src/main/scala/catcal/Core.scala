package catcal

import catcal.domain.{ Event, ParserConfiguration }
import catcal.service._

trait Core {
  import com.softwaremill.macwire._

  def parserConfiguration: ParserConfiguration

  lazy val calendarParser = wire[CalendarParser]

  lazy val dateCalculator = wire[DateCalculator]

  lazy val eventExporter = wire[EventExporter]

  lazy val eventImporter = wire[EventImporter]

  def eventResolver(currentYear: Int, knownEvents: Iterable[Event]) = wire[EventResolver]

}
