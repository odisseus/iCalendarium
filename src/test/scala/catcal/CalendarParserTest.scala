package catcal

import org.scalatest.{FlatSpec, Matchers}

class CalendarParserTest extends FlatSpec with Matchers{
  behavior of "CalendarParser"

  it should "parse fixed days" in {
    val conf = new ParserConfiguration(months = Seq("січня", "квітня"))
    val parser = new CalendarParser(conf)
    parser.parseFixedDay("28 січня") shouldBe 'successful
    parser.parseFixedDay("8\tквітня") shouldBe 'successful
    parser.parseFixedDay("08 квітня") shouldBe 'successful
    parser.parseFixedDay("028 січня") shouldBe 'successful
    parser.parseFixedDay("8 fooo") shouldBe 'empty
    parser.parseFixedDay("38 січня") shouldBe 'empty
    parser.parseFixedDay("28січня") shouldBe 'empty
    parser.parseFixedDay("28") shouldBe 'empty
    parser.parseFixedDay("січня") shouldBe 'empty
  }

}
