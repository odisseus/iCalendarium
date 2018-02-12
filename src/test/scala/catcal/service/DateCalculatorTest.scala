package catcal.service

import org.joda.time.DateTimeConstants._
import org.joda.time.LocalDate
import org.scalatest.{ EitherValues, FlatSpec, Matchers }

class DateCalculatorTest extends FlatSpec with Matchers with EitherValues {
  behavior of "DateCalculator"

  val dateCalculator = new DateCalculator

  it should "calculate dates forward" in {
    val reference = LocalDate.parse("2016-05-02")
    dateCalculator.calculateDate(1, SUNDAY, reference).right.value shouldBe LocalDate.parse("2016-05-08")
    dateCalculator.calculateDate(1, MONDAY, reference).right.value shouldBe LocalDate.parse("2016-05-09")
  }

  it should "calculate dates backward" in {
    val reference = LocalDate.parse("2016-04-30")
    dateCalculator.calculateDate(-1, SUNDAY, reference).right.value shouldBe LocalDate.parse("2016-04-24")
    dateCalculator.calculateDate(-1, SATURDAY, reference).right.value shouldBe LocalDate.parse("2016-04-23")
  }

}
