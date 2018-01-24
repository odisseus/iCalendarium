package catcal.service

import catcal.domain._
import org.joda.time.MonthDay

import scala.util.parsing.combinator.RegexParsers

class CalendarParser(conf: ParserConfiguration) extends RegexParsers {

  // Do not mix RegexParsers with PackratParsers. See https://github.com/scala/bug/issues/8080

  override protected val whiteSpace = """[ \t]+""".r

  private def int: Parser[Int] = "[0-9]+\\b".r ^^ (_.toInt)

  private def day = int ^? (
    { case x if (1 to 31) contains x => x },
    { bad: Int => s"Invalid day: $bad" }
  )

  private def indexFrom(list: Seq[String]): Parser[Int] = ("\\S+".r) ^? {
    list.zipWithIndex.toMap
  }

  private def month = indexFrom(conf.months) ^^ { _ + 1 }

  private def weekday = indexFrom(conf.weekdays) ^^ { _ + 1 }

  private def fixedDay: Parser[FixedDay] = day ~ month ^^ (x => FixedDay(new MonthDay(x._2, x._1)))

  private def movable: Parser[Movable] = int ~ weekday ~ (conf.before | conf.after) ~ descriptionLine ^^ {
    case ordinal ~ day ~ direction ~ reference => Movable(
      if (direction == conf.before) -ordinal else ordinal,
      day,
      reference
    )
  }

  private def eventDate: Parser[EventDate] = fixedDay | movable

  private def newline = """(?s)\r?\n""".r

  private def delimiter = newline ~ (newline+)

  private def descriptionLine = """\S.*""".r

  private def event = eventDate ~ ((newline ~> descriptionLine) +) ^^
    (x => Event(x._1, x._2.mkString("\n")))

  private def eventList = (newline*) ~> repsep(event, delimiter) <~ (newline*)

  // temporary
  def parseFixedDay(str: String) = {
    parseAll(fixedDay, str)
  }

  // temporary
  def parseMovable(str: String) = {
    parseAll(movable, str)
  }

  // temporary
  def parseEvent(str: String) = {
    parseAll(event, str)
  }

  // temporary
  def parseEventList(str: String) = {
    parseAll(eventList, str)
  }

  def parseEventList(in: java.io.Reader) = {
    parse(eventList, in)
  }

}
