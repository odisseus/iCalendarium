package catcal.service

import org.joda.time.DateTimeConstants._
import org.joda.time.LocalDate
import org.scalatest.{ FlatSpec, Matchers }

class DateCalculatorTest extends FlatSpec with Matchers {
  behavior of "DateCalculator"

  val dateCalculator = new DateCalculator

  it should "calculate dates forward" in {
    val reference = LocalDate.parse("2016-05-02")
    dateCalculator.calculateDate(1, SUNDAY, reference) shouldBe LocalDate.parse("2016-05-08")
    dateCalculator.calculateDate(1, MONDAY, reference) shouldBe LocalDate.parse("2016-05-09")
  }

  it should "calculate dates backward" in {
    val reference = LocalDate.parse("2016-04-30")
    dateCalculator.calculateDate(-1, SUNDAY, reference) shouldBe LocalDate.parse("2016-04-24")
    dateCalculator.calculateDate(-1, SATURDAY, reference) shouldBe LocalDate.parse("2016-04-23")
  }

}
