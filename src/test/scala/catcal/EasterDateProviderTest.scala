package catcal

import org.joda.time.{ DateTime, DateTimeConstants, LocalDate }
import org.scalatest.{ FlatSpec, Matchers }

class EasterDateProviderTest extends FlatSpec with Matchers {
  behavior of "CalendarParser"

  "the Easter date" should "always be Sunday" in {
    (2016 to 2022).foreach { year =>
      val date = EasterDateProvider.getEasterDate(year)
      date.getDayOfWeek shouldBe DateTimeConstants.SUNDAY
    }
  }

}