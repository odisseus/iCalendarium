package catcal

import catcal.domain.{ Event, FixedDay, Movable }
import org.joda.time.DateTimeConstants._
import org.scalatest.{ FlatSpec, Matchers }

class CalendarParserTest extends FlatSpec with Matchers {
  behavior of "CalendarParser"

  val conf = ParserConfiguration(
    months = Seq("січня", "лютого", "березня", "квітня"),
    weekdays = Seq("понеділок", "вівторок", "середа", "четвер", "п'ятниця"),
    before = "перед",
    after = "після"
  )
  val parser = new CalendarParser(conf)

  it should "parse fixed days" in {
    parser.parseFixedDay("28 січня").get shouldBe FixedDay("--01-28")
    parser.parseFixedDay("8\tквітня").get shouldBe FixedDay("--04-08")
    parser.parseFixedDay("08 квітня").get shouldBe FixedDay("--04-08")
    parser.parseFixedDay("028 січня").get shouldBe FixedDay("--01-28")
    parser.parseFixedDay("8 fooo") shouldBe 'empty
    parser.parseFixedDay("38 січня") shouldBe 'empty
    parser.parseFixedDay("28січня") shouldBe 'empty
    parser.parseFixedDay("28") shouldBe 'empty
    parser.parseFixedDay("січня") shouldBe 'empty
  }

  it should "parse fixed date events" in {
    val event = "1 січня\nНовий Рік\nСв. мч. Боніфатія."
    parser.parseEvent(event).get shouldBe Event(
      FixedDay("--01-01"),
      "Новий Рік\nСв. мч. Боніфатія."
    )
  }

  it should "parse movable references" in {
    parser.parseMovable("9 п'ятниця перед Новий Рік").get shouldBe Movable(-9, FRIDAY, "Новий Рік")
  }

  it should "parse movable events" in {
    val event = "1 понеділок після Пасха\nСвітлий Понеділок.\nОбливаний понеділок."
    parser.parseEvent(event).get shouldBe Event(
      Movable(1, MONDAY, "Пасха"),
      "Світлий Понеділок.\nОбливаний понеділок."
    )
  }

  it should "parse lists of fixed date and movable events" in {
    val goodList =
      """
        |
        |1 січня
        |Новий Рік
        |Св. мч. Боніфатія.
        |
        |2 квітня
        |Прпп. Отців мчч. убитих сарацинами в монастирі св. Сави.
        |
        |7 понеділок перед Пасха
        |Початок Великого посту.
        |
        |""".stripMargin
    parser.parseEventList(goodList).get shouldBe List(
      Event(FixedDay("--01-01"), "Новий Рік\nСв. мч. Боніфатія."),
      Event(FixedDay("--04-02"), "Прпп. Отців мчч. убитих сарацинами в монастирі св. Сави."),
      Event(Movable(-7, MONDAY, "Пасха"), "Початок Великого посту.")
    )
  }

}
