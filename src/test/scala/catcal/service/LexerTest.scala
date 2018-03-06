package catcal.service

import org.scalatest.{ EitherValues, FlatSpec, Matchers }
import Lexer._
import catcal.domain.{ Movable, ParserConfiguration }

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
    val input = "понеділок після Пасха\nСвітлий Понеділок.\nОбливаний понеділок."
    parser.parseProgram(input).right.value shouldBe List(
      TextLine("понеділок після Пасха"),
      Newline,
      TextLine("Світлий Понеділок."),
      Newline,
      TextLine("Обливаний понеділок.")
    )
  }

  it should "parse dates, empty lines and comments" in {
    val input = "1 понеділок після Пасха\n\n#Обливаний понеділок."
    parser.parseProgram(input).right.value shouldBe List(
      DateLine(Movable(1, 1, "Пасха")),
      Newline,
      Newline,
      Comment
    )
  }

}
