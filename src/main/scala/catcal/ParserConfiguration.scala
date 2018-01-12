package catcal

case class ParserConfiguration(
  months: Seq[String],
  weekdays: Seq[String],
  before: String,
  after: String)