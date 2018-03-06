package catcal.service

import catcal.domain.Errors.ParserError
import catcal.service.LowLevelParser._

import scala.util.parsing.combinator.RegexParsers

class LowLevelParser extends RegexParsers {

  override protected val whiteSpace = """[ \t]+""".r

  def comment: Parser[Token] = "#.*".r ^^^ Comment

  def newline: Parser[Token] = """(?s)\r?\n""".r ^^^ Newline

  def textLine: Parser[Token] = """\S.*""".r ^^ TextLine.apply

  def program: Parser[List[Token]] = phrase(rep(comment | newline | textLine))

  def parseProgram(str: String): Either[ParserError, List[Token]] = {
    parseAll(program, str) match {
      case Success(ev, _) => Right(ev)
      case NoSuccess(msg, next) => Left(ParserError(msg, str, next))
    }
  }

}

object LowLevelParser {
  sealed trait Token

  case object Comment extends Token
  case object Newline extends Token
  case class TextLine(text: String) extends Token
}
