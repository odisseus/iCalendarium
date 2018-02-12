package catcal.service

import catcal.domain.Errors.{ DateCalculatorError, Error }
import org.joda.time.LocalDate

class DateCalculator {
  def calculateDate(ordinal: Int, weekday: Int, date: LocalDate): Either[Error, LocalDate] = {
    if (ordinal == 0) {
      Left(DateCalculatorError(s"Cannot calculate a zeroth $weekday before/after $date"))
    } else {
      val inc = if (ordinal > 0) 1 else -1
      val count = ordinal * inc
      val result = Stream.iterate(date)(_.plusDays(inc))
        .filter(_.getDayOfWeek == weekday).dropWhile(_ == date).drop(count - 1).head
      Right(result)
    }
  }
}
