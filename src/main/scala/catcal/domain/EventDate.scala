package catcal.domain

import org.joda.time.MonthDay

trait EventDate

case class FixedDay(
  monthDay: MonthDay) extends EventDate

object FixedDay {
  def apply(iso: String): FixedDay = FixedDay(MonthDay.parse(iso))
}

case class Movable(
  ordinal: Int,
  weekday: Int,
  reference: String) extends EventDate