package catcal.domain

trait EventDate

case class FixedDay(
  day: Int,
  month: String) extends EventDate