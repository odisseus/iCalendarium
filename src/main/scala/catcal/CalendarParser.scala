package catcal
import catcal.domain.{ Event, EventDate, FixedDay }
import scala.util.parsing.combinator.{ PackratParsers, Parsers, RegexParsers }

class CalendarParser(conf: ParserConfiguration) extends Parsers with RegexParsers with PackratParsers {

  private def int: Parser[Int] = "[0-9]+\\b".r ^^ (_.toInt)

  private def day = int ^? (
    { case x if (1 to 31) contains x => x },
    { bad: Int => s"Invalid day: $bad" }
  )

  private def month = "\\S+".r ^? {
    case str if conf.months.contains(str) => str
  }

  private def fixedDay: Parser[FixedDay] = day ~ month ^^ (x => FixedDay(x._1, x._2))

  private def eventDate: Parser[EventDate] = fixedDay

  private def emptyLines: Parser[String] = "^\\s*$"

  private def event: PackratParser[Event] = eventDate ~ """(?s).*""".r ^^ (x => Event(x._1, x._2))

  // temporary
  def parseFixedDay(str: String) = {
    parseAll(fixedDay, str)
  }

  // temporary
  def parseEvent(str: String) = {
    parseAll(event, str)
  }

}
