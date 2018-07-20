package catcal.service

import catcal.domain.ParserConfiguration
import catcal.service.parsing.{ Lexer, TokenParser }
import org.scalatest.{ FlatSpec, Matchers }

import scala.io.Source

class EventImporterTest extends FlatSpec with Matchers {
  behavior of "EventImporter"

  val conf = ParserConfiguration(
    months = Seq("січня", "лютого", "березня", "квітня"),
    weekdays = Seq("вівторок", "п'ятниця"),
    before = "перед",
    after = "після"
  )
  val parser = new CalendarParser(new Lexer(conf), TokenParser.withDatesAtBeginning)

  val eventImporter = new EventImporter(parser)

  it should "parse a valid event list from a reader" in {
    val x = Source.fromURL(getClass.getResource("/test-fixed-days.txt"))
    val (parsedList, errors) = eventImporter.read(x.reader()).get
    parsedList should have size 7
    errors shouldBe 'empty
  }

}
