package catcal.domain

import com.typesafe.config.Config

case class EventsConfiguration(
  easter: String)

object EventsConfiguration {
  def apply(config: Config): EventsConfiguration = new EventsConfiguration(
    easter = config.getString("easter")
  )
}