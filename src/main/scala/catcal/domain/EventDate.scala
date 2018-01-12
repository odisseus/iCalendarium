package catcal.domain

trait EventDate

case class FixedDay(
  day: Int,
  month: String) extends EventDate

case class Movable(
  ordinal: Int,
  weekday: String,
  reference: String) extends EventDate