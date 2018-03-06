package catcal.service

import org.scalatest.{ EitherValues, FlatSpec, Matchers }
import LowLevelParser._

class LowLevelParserTest extends FlatSpec with Matchers with EitherValues {
  behavior of "LowLevelParser"

  val parser = new LowLevelParser

  it should "parse text lines" in {
    val input = "1 понеділок після Пасха\nСвітлий Понеділок.\nОбливаний понеділок."
    parser.parseProgram(input).right.value shouldBe List(
      TextLine("1 понеділок після Пасха"),
      Newline,
      TextLine("Світлий Понеділок."),
      Newline,
      TextLine("Обливаний понеділок.")
    )
  }

  it should "parse empty lines ang comments" in {
    val input = "1 понеділок після Пасха\n\n#Обливаний понеділок."
    parser.parseProgram(input).right.value shouldBe List(
      TextLine("1 понеділок після Пасха"),
      Newline,
      Newline,
      Comment
    )
  }

}
