package catcal.service

import java.io.StringReader

import catcal.domain.Errors.{ ParserError, TokenizerError }
import catcal.domain.{ Event, FixedDay, Movable, ParserConfiguration }
import catcal.service.parsing._
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

  val parser = new CalendarParser(new Lexer(conf), TokenParser.withDatesAtEnd)

  it should "parse lists of fixed date and movable events" in {
    val eventList =
      """
        |
        |Новий Рік
        |Св. мч. Боніфатія.
        |1 січня
        |
        |Прпп. Отців мчч. убитих сарацинами в монастирі св. Сави.
        |2 квітня
        |
        |foobar
        |
        |Початок Великого посту.
        |7 понеділок перед Пасха
        |
        |""".stripMargin
    val (parsed, failed) = parser.parseEventList(new StringReader(eventList)).right.value
    parsed shouldBe List(
      Event(FixedDay("--01-01"), "Новий Рік\nСв. мч. Боніфатія."),
      Event(FixedDay("--04-02"), "Прпп. Отців мчч. убитих сарацинами в монастирі св. Сави."),
      Event(Movable(-7, MONDAY, "Пасха"), "Початок Великого посту.")
    )
    failed.loneElement shouldBe a[TokenizerError]
  }

}
