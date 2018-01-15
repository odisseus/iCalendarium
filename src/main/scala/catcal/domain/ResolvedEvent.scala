package catcal.domain

import org.joda.time.LocalDate

case class ResolvedEvent(
  date: LocalDate,
  description: String)
