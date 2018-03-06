package catcal.service

import catcal.domain.Errors.ParserError
import catcal.domain.{ EventDate, ParserConfiguration }
import catcal.service.Lexer._

import scala.util.parsing.combinator.RegexParsers

class Lexer(val conf: ParserConfiguration) extends RegexParsers with EventDateParser {

  override protected val whiteSpace = """[ \t]+""".r

  def comment: Parser[Token] = "#.*".r ^^^ Comment

  def newline: Parser[Token] = """(?s)\r?\n""".r ^^^ Newline

  def textLine: Parser[Token] = """\S.*""".r ^^ TextLine.apply

  def dateLine: Parser[Token] = (eventDate) ^^ DateLine.apply

  def program: Parser[List[Token]] = phrase(rep(comment | newline | dateLine | textLine))

  def parseProgram(str: String): Either[ParserError, List[Token]] = {
    parseAll(program, str) match {
      case Success(ev, _) => Right(ev)
      case NoSuccess(msg, next) => Left(ParserError(msg, str, next))
    }
  }

}

object Lexer {
  sealed trait Token

  case object Comment extends Token
  case object Newline extends Token
  case class DateLine(date: EventDate) extends Token
  case class TextLine(text: String) extends Token
}
