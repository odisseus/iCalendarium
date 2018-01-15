package catcal

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
  val parser = new CalendarParser(conf)

  val eventImporter = new EventImporter(parser)

  it should "parse a valid event list from a reader" in {
    val x = Source.fromURL(getClass.getResource("/fixed-days.txt"))
    val parsedList = eventImporter.read(x.reader())
    parsedList.get.length shouldBe 7
  }

}
