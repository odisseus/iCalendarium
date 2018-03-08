package catcal.service.parsing

import catcal.domain.{ Event, FixedDay, Movable }
import catcal.service.parsing.Lexer._
import org.joda.time.DateTimeConstants.MONDAY
import org.scalatest.{ EitherValues, FlatSpec, LoneElement, Matchers }

class TokenParserTest extends FlatSpec with Matchers with LoneElement with EitherValues {
  behavior of "TokenParser"

  it should "parse events with fixed date at beginning" in {
    val tokens = List(
      DateLine(FixedDay("--01-01")),
      Newline,
      TextLine("Новий Рік"),
      Newline,
      TextLine("Св. мч. Боніфатія.")
    )

    val parser = TokenParser.withDatesAtBeginning

    parser.parseTokens(tokens).right.value.loneElement.right.value shouldBe Event(
      FixedDay("--01-01"),
      "Новий Рік\nСв. мч. Боніфатія."
    )
  }

  it should "parse events with movable date at end" in {
    val tokens = List(
      TextLine("Світлий Понеділок."),
      Newline,
      TextLine("Обливаний понеділок."),
      Newline,
      DateLine(Movable(1, MONDAY, "Пасха"))
    )

    val parser = TokenParser.withDatesAtEnd

    parser.parseTokens(tokens).right.value.loneElement.right.value shouldBe Event(
      Movable(1, MONDAY, "Пасха"),
      "Світлий Понеділок.\nОбливаний понеділок."
    )
  }

  it should "handle blocks that cannot be parsed into a date" in {
    val tokens = List(
      TextLine("Світлий Понеділок."),
      Newline,
      TextLine("Обливаний понеділок.")
    )

    val parser = TokenParser.withDatesAtEnd

    parser.parseTokens(tokens).right.value.loneElement.left.value shouldBe "Світлий Понеділок.\nОбливаний понеділок."
  }

  it should "handle inputs with multiple blocks" in {
    val tokens = List(
      DateLine(FixedDay("--01-01")),
      Newline,
      TextLine("Новий Рік"),
      Newline,
      TextLine("Св. мч. Боніфатія."),
      Separator,
      TextLine("Світлий Понеділок."),
      Newline,
      TextLine("Обливаний понеділок.")
    )

    val parser = TokenParser.withDatesAtBeginning

    parser.parseTokens(tokens).right.value shouldBe (Seq(
      Right(Event(FixedDay("--01-01"), "Новий Рік\nСв. мч. Боніфатія.")),
      Left("Світлий Понеділок.\nОбливаний понеділок.")
    ))
  }

  it should "handle inputs that contain no blocks" in {
    val tokens = List(
      Newline,
      Newline,
      Newline,
      Separator,
      Separator,
      Separator,
      Newline
    )

    val parser = TokenParser.withDatesAtEnd

    parser.parseTokens(tokens).right.value shouldBe 'empty
  }

}
