package catcal

import org.joda.time.LocalDate

class DateCalculator {
  def calculateDate(ordinal: Int, weekday: Int, date: LocalDate): LocalDate = {
    require(ordinal != 0)
    val inc = if (ordinal > 0) 1 else -1
    val count = ordinal * inc
    Stream.iterate(date)(_.plusDays(inc)).filter(_.getDayOfWeek == weekday).dropWhile(_ == date).drop(count - 1).head
  }
}
