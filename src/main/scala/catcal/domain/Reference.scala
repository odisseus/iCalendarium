package catcal.domain

import org.joda.time.MonthDay

trait Reference

case class EventReference(eventName: String) extends Reference

case class FixedDayReference(monthDay: MonthDay) extends Reference
