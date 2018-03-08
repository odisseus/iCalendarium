package catcal.service.parsing

import catcal.domain.Errors.ParserError
import catcal.domain.{ Event, EventDate }
import catcal.service.parsing.Lexer._
import catcal.service.parsing.TokenParser.BlockList
import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{ NoPosition, Position, Reader }

sealed trait TokenParser extends Parsers {

  override type Elem = Token

  private class TokenReader(tokens: List[Token]) extends Reader[Elem] {
    override def first: Token = tokens.head

    override def rest: Reader[Token] = new TokenReader(tokens.tail)

    override def pos: Position = NoPosition

    override def atEnd: Boolean = tokens.isEmpty
  }

  protected def newline: Parser[Unit] = Newline ^^^ ()

  protected def dateLine: Parser[EventDate] = accept("dateLine", { case DateLine(eventDate) => eventDate })

  private def textLine: Parser[String] = accept("textLine", { case TextLine(text) => text })

  protected def eventBlock: Parser[Event]

  protected def textBlock: Parser[String] = (rep1sep(textLine, newline)) ^^ (_.mkString("\n"))

  private def block: Parser[Either[String, Event]] = (eventBlock ^^ Right.apply) | (textBlock ^^ Left.apply)

  private def separator: Parser[Unit] = Separator ^^^ ()

  private def program: Parser[BlockList] = phrase(
    ((newline | separator) *) ~> repsep(block, separator) <~ ((newline | separator) *)
  )

  def parseTokens(tokens: List[Token]): Either[ParserError, BlockList] = {
    val reader = new TokenReader(tokens)
    program(reader) match {
      case Success(ev, _) => Right(ev)
      case NoSuccess(msg, next) => Left(ParserError(msg, "", next))
    }
  }

}

object TokenParser {

  type BlockList = List[Either[String, Event]]

  def withDatesAtBeginning = new TokenParser {

    override def eventBlock: Parser[Event] = ((dateLine <~ newline) ~ textBlock) ^^ {
      case ~(eventDate, description) => Event(eventDate, description)
    }

  }

  def withDatesAtEnd = new TokenParser {

    override def eventBlock: Parser[Event] = ((textBlock <~ newline) ~ dateLine) ^^ {
      case ~(description, eventDate) => Event(eventDate, description)
    }

  }

}

