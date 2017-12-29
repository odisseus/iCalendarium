package catcal

import catcal.domain.{ Event, FixedDay }
import org.scalatest.{ FlatSpec, Matchers }

class CalendarParserTest extends FlatSpec with Matchers {
  behavior of "CalendarParser"

  val conf = ParserConfiguration(months = Seq("січня", "квітня"))
  val parser = new CalendarParser(conf)

  it should "parse fixed days" in {
    parser.parseFixedDay("28 січня").get shouldBe FixedDay(28, "січня")
    parser.parseFixedDay("8\tквітня").get shouldBe FixedDay(8, "квітня")
    parser.parseFixedDay("08 квітня").get shouldBe FixedDay(8, "квітня")
    parser.parseFixedDay("028 січня").get shouldBe FixedDay(28, "січня")
    parser.parseFixedDay("8 fooo") shouldBe 'empty
    parser.parseFixedDay("38 січня") shouldBe 'empty
    parser.parseFixedDay("28січня") shouldBe 'empty
    parser.parseFixedDay("28") shouldBe 'empty
    parser.parseFixedDay("січня") shouldBe 'empty
  }

  it should "parse fixed date events" in {
    val goodEvent =
      """
        |1 січня
        |Новий Рік
        |Іменини Григорія""".stripMargin
    parser.parseEvent(goodEvent).get shouldBe Event(
      FixedDay(1, "січня"),
      "Новий Рік\nІменини Григорія"
    )
  }

}
