package catcal.service.parsing

import catcal.domain.Errors.ParserError
import catcal.domain.{ EventDate, ParserConfiguration }
import catcal.service.EventDateParser
import catcal.service.parsing.Lexer._
import scala.util.parsing.combinator.RegexParsers

class Lexer(val conf: ParserConfiguration) extends RegexParsers with EventDateParser {

  override protected val whiteSpace = """[ \t]+""".r

  def comment: Parser[Unit] = "#.*".r ^^^ ()

  def newline: Parser[Token] = """(?s)\r?\n""".r ^^^ Newline

  def separator: Parser[Token] = (newline ~ (comment ?) ~ newline) ^^^ Separator

  def textLine: Parser[Token] = """\S(.*\S)?""".r ^^ TextLine.apply

  def dateLine: Parser[Token] = (eventDate) ^^ DateLine.apply

  def program: Parser[List[Token]] = phrase(
    (comment ?) ~> repsep(separator | newline | dateLine | textLine, (comment ?)) <~ (comment ?)
  )

  private def toEither(result: ParseResult[List[Token]]) = result match {
    case Success(ev, _) => Right(ev)
    case NoSuccess(msg, next) => Left(ParserError(msg, "", next))
  }

  def parseProgram(str: String): Either[ParserError, List[Token]] = {
    toEither(parseAll(program, str))
  }

  def parseProgram(rdr: java.io.Reader): Either[ParserError, List[Token]] = {
    toEither(parseAll(program, rdr))
  }

}

object Lexer {
  sealed trait Token

  case object Newline extends Token
  case object Separator extends Token
  case class DateLine(date: EventDate) extends Token
  case class TextLine(text: String) extends Token
}
