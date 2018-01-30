package catcal.service

import catcal.domain.Errors.TokenizerError

import scala.util.parsing.combinator.RegexParsers

object InputTokenizer extends RegexParsers {

  override protected val whiteSpace = """[ \t]+""".r

  private def newline = """(?s)\r?\n""".r

  private def textLine = """\S.*""".r

  private def delimiter = newline ~ (newline+)

  private def text = rep1sep(textLine, newline) ^^ (lines => lines.mkString("\n"))

  private def textEntries = (newline*) ~> repsep(text, delimiter) <~ (newline*)

  def entries(in: java.io.Reader): Either[TokenizerError, List[String]] = {
    parseAll(textEntries, in) match {
      case Success(entries, _) => Right(entries)
      case NoSuccess(msg, x) => Left(TokenizerError(msg))
    }
  }

}
