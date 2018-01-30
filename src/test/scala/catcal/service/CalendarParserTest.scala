package catcal.service

import java.io.StringReader

import catcal.domain.Errors.ParserError
import catcal.domain.{ Event, FixedDay, Movable, ParserConfiguration }
import org.joda.time.DateTimeConstants.MONDAY
import org.scalatest.{ EitherValues, FlatSpec, LoneElement, Matchers }

class CalendarParserTest extends FlatSpec with Matchers with EitherValues with LoneElement {
  behavior of "CalendarParser"

  val conf = ParserConfiguration(
    months = Seq("січня", "лютого", "березня", "квітня"),
    weekdays = Seq("понеділок", "вівторок", "середа", "четвер", "п'ятниця"),
    before = "перед",
    after = "після"
  )

  val parser = new CalendarParser(new EventParser(conf))

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
        |foobar
        |
        |7 понеділок перед Пасха
        |Початок Великого посту.
        |
        |""".stripMargin
    val result = parser.parseEventList(new StringReader(goodList)).right.value
    result._1 shouldBe List(
      Event(FixedDay("--01-01"), "Новий Рік\nСв. мч. Боніфатія."),
      Event(FixedDay("--04-02"), "Прпп. Отців мчч. убитих сарацинами в монастирі св. Сави."),
      Event(Movable(-7, MONDAY, "Пасха"), "Початок Великого посту.")
    )
    result._2.loneElement shouldBe a[ParserError]
  }

}
