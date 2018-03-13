package catcal.service

import catcal.domain.Errors.TokenizerError
import catcal.domain.{ Errors, Event }
import catcal.service.parsing.{ Lexer, TokenParser }

class CalendarParser(lexer: Lexer, tokenParser: TokenParser) {

  def parseEventList(in: java.io.Reader): Either[Errors.Error, (List[Event], List[Errors.Error])] = {
    val blockList = for {
      tokens <- lexer.parseProgram(in)
      blocks <- tokenParser.parseTokens(tokens)
    } yield blocks

    blockList.right.map { blocks =>
      val (parsedEvents, badEntries) = blocks.partition(_.isRight)
      (parsedEvents.map(_.right.get), badEntries.map(_.left.get).map(TokenizerError.apply))
    }
  }

}
