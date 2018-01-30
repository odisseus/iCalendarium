package catcal.service

import java.io.StringReader

import org.scalatest.{ EitherValues, FlatSpec, FunSuite, Matchers }

class InputTokenizerTest extends FlatSpec with Matchers with EitherValues {
  behavior of "InputTokenizer"

  it should "split an input into separate entries" in {
    val input =
      """
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

    val result = InputTokenizer.entries(new StringReader(input))

    result.right.value should have size 4
    result.right.value should contain("foobar")

  }

}
