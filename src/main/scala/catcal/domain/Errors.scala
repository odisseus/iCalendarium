package catcal.domain

import scala.util.parsing.input.Reader

object Errors {

  sealed abstract trait Error {

    val message: String

    def throwException = throw new Exception(message)

  }

  case class ParserError(msg: String, input: String, next: Reader[_]) extends Error {
    override val message: String =
      s"""
         |Parser failure at line ${next.pos.line}, column ${next.pos.column}: $msg
         |${next.pos.longString}
       """.stripMargin
  }

  case class TokenizerError(message: String) extends Error

  case class ResolverError(message: String) extends Error

  case class DateCalculatorError(message: String) extends Error

}
