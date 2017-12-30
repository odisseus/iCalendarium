package catcal
import catcal.domain.{ Event, EventDate, FixedDay }
import scala.util.parsing.combinator.{ PackratParsers, Parsers, RegexParsers }

class CalendarParser(conf: ParserConfiguration) extends Parsers with RegexParsers with PackratParsers {

  override protected val whiteSpace = """[ \t]+""".r

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

  private def newline = """(?s)\r?\n""".r

  private def delimiter = newline ~ newline

  private def descriptionLine = newline ~> """\S.*""".r

  private def event: PackratParser[Event] = eventDate ~ (descriptionLine +) ^^
    (x => Event(x._1, x._2.mkString("\n")))

  private def eventList: PackratParser[List[Event]] = (newline*) ~> repsep(event, delimiter) <~ (newline*)

  // temporary
  def parseFixedDay(str: String) = {
    parseAll(fixedDay, str)
  }

  // temporary
  def parseEvent(str: String) = {
    parseAll(event, str)
  }

  // temporary
  def parseEventList(str: String) = {
    parseAll(eventList, str)
  }

}
