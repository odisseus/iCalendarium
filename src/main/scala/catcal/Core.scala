package catcal

import catcal.domain.{ Event, EventsConfiguration, ParserConfiguration }
import catcal.service._
import catcal.service.parsing._

trait Core {
  import com.softwaremill.macwire._

  def parserConfiguration: ParserConfiguration

  def eventsConfiguration: EventsConfiguration

  lazy val tokenParser: TokenParser = TokenParser.withDatesAtEnd

  lazy val lexer: Lexer = wire[Lexer]

  lazy val calendarParser = wire[CalendarParser]

  lazy val dateCalculator = wire[DateCalculator]

  lazy val eventExporter = wire[EventExporter]

  lazy val eventImporter = wire[EventImporter]

  def predefinedEvents(currentYear: Int) = wire[PredefinedEvents]

  def eventResolver(currentYear: Int, knownEvents: Iterable[Event]) = wire[EventResolver]

}
