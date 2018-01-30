package catcal.domain

object Errors {

  sealed abstract trait Error {

    val message: String

    def throwException = throw new Exception(message)

  }

  case class ParserError(input: String) extends Error {
    override val message: String = s"Failed to parse: $input"
  }

  case class TokenizerError(message: String) extends Error

}
