package catcal.service.parsing

import catcal.domain.{ Movable, ParserConfiguration }
import catcal.service.parsing.Lexer._
import org.scalatest.{ EitherValues, FlatSpec, Matchers }

class LexerTest extends FlatSpec with Matchers with EitherValues {
  behavior of "Lexer"

  val conf = ParserConfiguration(
    months = Seq("січня", "лютого", "березня", "квітня"),
    weekdays = Seq("понеділок", "вівторок", "середа", "четвер", "п'ятниця"),
    before = "перед",
    after = "після"
  )

  val parser = new Lexer(conf)

  it should "parse text lines" in {
    val input = "понеділок після Пасха\n\nСвітлий Понеділок.\nОбливаний понеділок."
    parser.parseProgram(input).right.value shouldBe List(
      TextLine("понеділок після Пасха"),
      Separator,
      TextLine("Світлий Понеділок."),
      Newline,
      TextLine("Обливаний понеділок.")
    )
  }

  it should "parse dates and empty lines while skipping comments" in {
    val input = "1 понеділок після Пасха\n\n#Обливаний понеділок."
    parser.parseProgram(input).right.value shouldBe List(
      DateLine(Movable(1, 1, "Пасха")),
      Separator
    )
  }

  it should "handle newlines in the beginning and end" in {
    val input = "\r\n\n1 понеділок після Пасха\n\n#Обливаний понеділок.\n\r\n"
    parser.parseProgram(input).right.value shouldBe List(
      Separator,
      DateLine(Movable(1, 1, "Пасха")),
      Separator,
      Separator
    )
  }

  it should "handle empty input" in {
    parser.parseProgram("").right.value shouldBe Nil
  }

  it should "trim whitespace" in {
    val input = "    1 понеділок після Пасха   \n  # Світлий Понеділок.   \n   Обливаний понеділок. "
    parser.parseProgram(input).right.value shouldBe List(
      DateLine(Movable(1, 1, "Пасха")),
      Separator,
      TextLine("Обливаний понеділок.")
    )
  }

}
