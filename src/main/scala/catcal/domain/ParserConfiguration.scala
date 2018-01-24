package catcal.domain

import com.typesafe.config.Config
import scala.collection.JavaConverters._

case class ParserConfiguration(
  months: Seq[String],
  weekdays: Seq[String],
  before: String,
  after: String)

object ParserConfiguration {
  def apply(config: Config): ParserConfiguration = new ParserConfiguration(
    months = config.getStringList("months").asScala,
    weekdays = config.getStringList("weekdays").asScala,
    before = config.getString("before"),
    after = config.getString("after"),
  )
}